package org.washcode.washpang.domain.inquiry.dto

import java.sql.Timestamp

class InquiryDto private constructor(){

    data class InquiryAddReqDTO(
        val inquiryContent: String
    )

    data class InquiryReplyReqDTO(
        val inquiryId: Int,
        val replyContent: String
    )
    data class InquiryResDTO(
        val inquiryId: Int,
        val email: String,
        val inquiryContent: String,
        val replyContent: String,
        val createdAt: Timestamp
    )
}