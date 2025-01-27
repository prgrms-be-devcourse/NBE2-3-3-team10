package org.washcode.washpang.domain.review.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.washcode.washpang.domain.review.dto.ReviewDto
import org.washcode.washpang.domain.review.entity.Review
import org.washcode.washpang.domain.review.service.ReviewService

@RestController
@RequestMapping("/api/review")
class ReviewController(private val reviewService: ReviewService) {

    @PostMapping("/createReview")
    fun createReview(@RequestBody reviewReq: ReviewDto.ReviewReq): Review {
        return reviewService.createReview(reviewReq)
    }


}