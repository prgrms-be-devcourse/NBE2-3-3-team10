package org.washcode.washpang.domain.inquiry.dto

import java.sql.Timestamp

data class InquiryDto (
    val title: String,
    val inquiryContent: String,
    val replyContent: String
)
