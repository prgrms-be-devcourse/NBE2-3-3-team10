package org.washcode.washpang.domain.review.dto

class ReviewDto {
    // 리뷰 삭제 (고객)
    data class ReviewDeleteReq(
        val reviewId: Int
    )

    // 리뷰 등록 (고객)
    data class ReviewReq(
        val pickupId: Int,
        val laundryShopId: Int,
        val content: String
    )

    // 리뷰 조회
    data class ReviewRes(
        val pickupId: Int,
        val userName: String,
        val shopName: String,
        val content: String
    )
}