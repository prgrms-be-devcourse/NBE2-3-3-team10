package org.washcode.washpang.global.domain.kakaopay.client

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.washcode.washpang.domain.order.repository.PaymentRepository
import org.washcode.washpang.global.domain.kakaopay.redis.db.KakaoPayPgTokenRepository
import org.washcode.washpang.global.domain.kakaopay.redis.db.KakaoPaymentInfoRepository
import org.washcode.washpang.domain.order.service.OrderService
import org.washcode.washpang.global.domain.kakaopay.dto.KakaoPayDto
import org.washcode.washpang.global.domain.kakaopay.exception.KakaoPayApproveFeignException
import org.washcode.washpang.global.domain.kakaopay.feign.KakaoPayApiClient
import org.washcode.washpang.global.domain.kakaopay.feign.KakaoPayApproveRedisErrorException
import org.washcode.washpang.global.domain.kakaopay.redis.entity.KakaoPaymentInfo
import org.washcode.washpang.global.exception.ErrorCode
import org.washcode.washpang.global.exception.ResponseResult
import org.springframework.transaction.annotation.Transactional

@Service
class KakaoPayClient(
    private val paymentRepository: PaymentRepository,
    private val kakaoPaymentInfoRepository: KakaoPaymentInfoRepository,
    private val kakaoPayPgTokenRepository: KakaoPayPgTokenRepository,
    private val kakaoPayApiClient: KakaoPayApiClient,
    private val orderService: OrderService,
) {
    // Log 사용을 위한 선언
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    private val cid: String = "TC0ONETIME"

    @Value("\${kakaopay.key.client-secret}")
    private val clientSecret = ""

    @Value("\${kakaopay.key.secret-dev}")
    private val secretDev = ""

    fun payReady(userId: Int, dto: KakaoPayDto.ReqDto): ResponseResult {
        val reqDto = KakaoPayDto.ReadyReq(cid, clientSecret, dto.paymentId, userId.toString(), dto.name, dto.quantity, dto.totalPrice)
        var resBody: KakaoPayDto.ReadyRes? = null

        try {
            resBody = kakaoPayApiClient.ready("SECRET_KEY $secretDev", reqDto)
                ?: throw NullPointerException("카카오페이 ReadyRes가 null 입니다.")
        } catch (e: Exception) {
            log.error("=================================================================")
            log.error("카카오페이 Ready Fail")
            log.error(e.message)

            return ResponseResult(ErrorCode.KAKAOPAY_READY_ERROR)
        }

        // Redis에 Tid 저장
        try {
            // Redis Key & Value 생성
            val redisKey = "$cid:$userId"
            val paymentInfo = KakaoPaymentInfo(redisKey, dto.paymentId.toInt(), resBody.tid)
            kakaoPaymentInfoRepository.save(paymentInfo)

            return ResponseResult("결제 준비 완료")
        } catch (e: Exception) {
            log.error("=================================================================")
            log.error("카카오페이 Tid Redis에 저장 실패")
            log.error(e.message)

            return ResponseResult(ErrorCode.KAKAOPAY_READY_ERROR)
        }
    }

    @Transactional
    fun payCompleted(userId: Int, token: String): ResponseResult {
        val payInfo: KakaoPaymentInfo?
        val pgToken: String?

        try {
            val payInfo = kakaoPaymentInfoRepository.findById("$cid:$userId")
                .orElseThrow { throw KakaoPayApproveRedisErrorException() }
            kakaoPaymentInfoRepository.delete(payInfo)

            val reqDto = KakaoPayDto.ApproveReq(cid, payInfo.tid, payInfo.partnerOrderId, userId, token)

            val resBody = kakaoPayApiClient.approve("SECRET_KEY $secretDev", reqDto)
                ?: throw KakaoPayApproveFeignException()

            val payment = paymentRepository.findById(payInfo.partnerOrderId)
                ?: throw Exception("결제 정보를 찾을 수 없습니다.")
            payment.updateKakaoPayData(resBody)
            orderService.updatePaymentStatusComplete(token)

            val result = mapOf(
                "aid" to resBody.aid,
                "approvedAt" to resBody.approvedAt
            )

            return ResponseResult(result)
        } catch (e: KakaoPayApproveRedisErrorException) {
            log.error("=================================================================")
            log.error("카카오페이 Redis Tid 조회 실패")
            log.error(e.message)

            return ResponseResult(ErrorCode.KAKAOPAY_READY_ERROR)
        } catch (e: KakaoPayApproveFeignException) {
            log.error("=================================================================")
            log.error(e.message)

            return ResponseResult(ErrorCode.KAKAOPAY_READY_ERROR)
        } catch (e: Exception) {
            log.error("=================================================================")
            log.error("카카오페이 결제 승인 실패")
            log.error(e.message)

            return ResponseResult(ErrorCode.KAKAOPAY_READY_ERROR)
        }
    }
}