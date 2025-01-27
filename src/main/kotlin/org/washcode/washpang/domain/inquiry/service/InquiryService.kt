package org.washcode.washpang.domain.inquiry.service

import org.springframework.stereotype.Service
import org.washcode.washpang.domain.inquiry.dto.InquiryDto
import org.washcode.washpang.domain.inquiry.entity.Inquiry
import org.washcode.washpang.domain.inquiry.repository.InquiryRepository
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.domain.user.exception.NoUserDataException
import org.washcode.washpang.domain.user.repository.UserRepository
import org.washcode.washpang.global.exception.ErrorCode
import org.washcode.washpang.global.exception.ResponseResult

@Service
class InquiryService(
    private val userRepository: UserRepository,
    private val laundryShopRepository: LaundryShopRepository,
    private val inquiryRepository: InquiryRepository
) {
    //문의사항 저장하기
    fun upsertInquiry(dto: InquiryDto, id: Int): ResponseResult {
        try {
            val user = userRepository.findById(id)
                ?: throw NoUserDataException()
            val laundryShop = laundryShopRepository.findById(dto.laundryId)

            val inquiry = inquiryRepository.findByUserId(user.id)
                ?.apply {
                    inquiryContent = dto.inquiryContent
                    replyContent = dto.replyContent
                    title = dto.title
                } ?: Inquiry(
                    user = user,
                    laundryShop = laundryShop,
                    inquiryContent = dto.inquiryContent,
                    replyContent = dto.replyContent,
                    title = dto.title
                )

            val saveInquiry = inquiryRepository.save(inquiry)

            return ResponseResult(saveInquiry)
        } catch (e: NoUserDataException) {
            return ResponseResult(ErrorCode.FAIL_TO_FIND_USER)
        }
    }
}