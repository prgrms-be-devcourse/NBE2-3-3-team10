package org.washcode.washpang.domain.review.controller

import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.review.dto.ReviewDto
import org.washcode.washpang.domain.review.entity.Review
import org.washcode.washpang.domain.review.service.ReviewService
import org.washcode.washpang.global.exception.ErrorCode
import org.washcode.washpang.global.exception.ResponseResult

@RestController
@RequestMapping("/api/review")
class ReviewController(private val reviewService: ReviewService) {
    //리뷰작성
    @PostMapping("/createReview")
    fun createReview(@RequestBody reviewReq: ReviewDto.ReviewReq): Review {
        return reviewService.createReview(reviewReq)
    }

    //리뷰 목록 조회
    @GetMapping("/lists")
    fun reviewList(): List<Review> {
        return reviewService.getReviewAll()
    }

    @GetMapping("/laundry-shop/{laundryShopId}")
    fun getReviewsByLaundryShop(@PathVariable laundryShopId: Long): List<Review> {
        return reviewService.getReviewsByLaundryShop(laundryShopId)
    }

    @DeleteMapping("/deleteReview/{reviewId}")
    fun deleteReview(@PathVariable reviewId: Int):ResponseResult {
        return try {
            reviewService.deleteReview(reviewId)
            ResponseResult(ErrorCode.SUCCESS)
        }catch (e:IllegalArgumentException){
            ResponseResult(ErrorCode.BAD_REQUEST)
        }

    }
}