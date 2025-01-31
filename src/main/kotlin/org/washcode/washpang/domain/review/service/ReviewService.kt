package org.washcode.washpang.domain.review.service

import org.springframework.stereotype.Service
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.domain.pickup.repository.PickupRepository
import org.washcode.washpang.domain.review.dto.ReviewDto
import org.washcode.washpang.domain.review.entity.Review
import org.washcode.washpang.domain.review.exception.FailToFindReviewException
import org.washcode.washpang.domain.review.repository.ReviewRepository

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val pickupRepository: PickupRepository,
    private val laundryShopRepository: LaundryShopRepository
) {
    // 리뷰 등록
    fun createReview(reviewReq: ReviewDto.ReviewReq): Review {
        val pickup = pickupRepository.findById(reviewReq.pickupId.toLong())
            .orElseThrow { IllegalArgumentException("Pickup not found with id") }

        val laundryShop = laundryShopRepository.findById(reviewReq.laundryShopId.toLong())
            .orElseThrow { IllegalArgumentException("Laundry Shop not found with id") }

        val review = Review().apply {
            this.pickup = pickup
            this.laundryShop = laundryShop
            this.content = reviewReq.content
        }

        return reviewRepository.save(review)
    }

    //리뷰 목록 조회(전체)
    fun getReviewAll():List<ReviewDto.ReviewRes>{
        val reviews = reviewRepository.findAll()
        return reviews.map{ review->
            ReviewDto.ReviewRes(
                pickupId = review.pickup.id,
                userName = review.pickup.user.name,
                shopName = review.laundryShop.shopName,
                content = review.content  // Review 객체에서 content를 가져옴
            )
        }
    }

    //세탁소 리뷰 목록 조회
    fun getReviewsByLaundryShop(laundryShopId: Long): List<Review>{
        return reviewRepository.findByLaundryShopId(laundryShopId)
    }

    /// 리뷰 삭제
    fun deleteReview(reviewId: Int) {
        val review = reviewRepository.findById(reviewId.toLong())
            .orElseThrow { IllegalArgumentException("Review not found with id") }

        // 리뷰 작성자가 삭제 요청자와 일치하는지 확인(임시)
        val userId = getCurrentUserId() // 현재 로그인한 사용자 ID 가져오는 메소드
        if (review.pickup.user.id.toLong() != userId) {
            throw IllegalArgumentException("This review does not belong to the current user.")
        }

        reviewRepository.delete(review)
    }

    // 현재 로그인한 사용자 ID 가져오는 메소드 (임시)
    private fun getCurrentUserId(): Long {
        return 1L // 예시로 사용자 ID가 1인 경우
    }

}