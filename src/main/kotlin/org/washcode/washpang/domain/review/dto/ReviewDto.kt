package org.washcode.washpang.domain.review.dto

class ReviewDto {
    // 리뷰 삭제 (고객)
    data class ReviewDeleteReq(
        val reviewId: Int = 0
    )

    // 리뷰 등록 (고객)
    data class ReviewReq(
        val pickupId: Int = 0,
        val laundryShopId: Int = 0,
        val content: String? = null
    )

    // 리뷰 조회
    data class ReviewRes(
        var pickupId: Int = 0,
        var userName: String = "",
        var content: String = ""
    )
}