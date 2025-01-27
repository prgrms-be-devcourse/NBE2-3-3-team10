package org.washcode.washpang.domain.review.service

import org.springframework.stereotype.Service
import org.washcode.washpang.domain.laundryshop.exception.FailToFindfLaundryShopException
import org.washcode.washpang.domain.laundryshop.repository.LaundryShopRepository
import org.washcode.washpang.domain.pickup.repository.PickupRepository
import org.washcode.washpang.domain.review.dto.ReviewDto
import org.washcode.washpang.domain.review.entity.Review
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


}