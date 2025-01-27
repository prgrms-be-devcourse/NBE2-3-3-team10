package org.washcode.washpang.domain.inquiry.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.washcode.washpang.domain.inquiry.dto.InquiryDto
import org.washcode.washpang.domain.inquiry.service.InquiryService
import org.washcode.washpang.global.exception.ResponseResult

@RestController
@RequestMapping("/api/inquiry")
class InquiryController(
    private val inquiryService: InquiryService
) {

    @PostMapping("/insert")
    @Operation(summary = "문의사항 저장", description = "문의사항 저장 API 입니다.")
    fun insertInquiry(
        @RequestBody dto: InquiryDto
        /*, @AuthenticationPrincipal int id*/
    ): ResponseResult {
        val id: Int = 1

        return ResponseResult(inquiryService.insertInquiry(dto, id))
    }

    @PostMapping("/update")
    @Operation(summary = "문의사항 수정", description = "문의사항 수정 API 입니다.")
    fun updateInquiry(
        @RequestBody dto: InquiryDto
        /*, @AuthenticationPrincipal int id*/
    ): ResponseResult {
        val id: Int = 1

        return ResponseResult(inquiryService.updateInquiry(dto, id))
    }

    @GetMapping("/lists")
    fun getInquiryByUserId(
        /*@AuthenticationPrincipal int id*/
    ): ResponseResult {
        val id: Int = 1

        return ResponseResult(inquiryService.findInquiryByUserId(id))
    }

    @GetMapping("/lists/{laundryId}")
    fun getInquiryByLaundryId(
        @RequestParam("laundryId") laundryId: Int
    ): ResponseResult {
        return ResponseResult(inquiryService.findInquiryByLaundryId(laundryId))
    }


}