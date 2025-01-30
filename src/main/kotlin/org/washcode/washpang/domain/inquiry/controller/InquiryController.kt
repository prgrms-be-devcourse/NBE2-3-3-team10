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
    @Operation(summary = "userId로 문의사항 조회", description = "사용자 문의사항 조회 API 입니다.")
    fun getInquiryByUserId(
        /*@AuthenticationPrincipal int id*/
    ): ResponseResult {
        val id: Int = 1

        return ResponseResult(inquiryService.findInquiryByUserId(id))
    }

    @GetMapping("/lists/{laundryId}")
    @Operation(summary = "세탁소ID로 문의사항 조회", description = "세탁소 문의사항 조회 API 입니다.")
    fun getInquiryByLaundryId(
        @PathVariable("laundryId") laundryId: Int
    ): ResponseResult {
        return ResponseResult(inquiryService.findInquiryByLaundryId(laundryId))
    }

    @DeleteMapping("/delete/{inquiryId}")
    @Operation(summary = "문의사항 삭제", description = "문의사항 삭제 API 입니다.")
    fun deleteInquiry(
        @PathVariable("inquiryId") inquiryId: Int,
        /*, AuthenticationPrincipal int id */
    ):ResponseResult {
        return ResponseResult(inquiryService.deleteInquiry(inquiryId))
    }

    @PostMapping("/reply/{inquiryId}")
    @Operation(summary = "문의사항 답글", description = "문의사항 답글 API 입니다.")
    fun replyInquiry(
        @RequestBody dto: InquiryDto,
        @PathVariable("inquiryId") inquiryId: Int
    /*, @AuthenticationPrincipal int id*/
    ): ResponseResult {
        //val id: Int = 1

        return ResponseResult(inquiryService.replyInquiry(dto, inquiryId))
    }

    @GetMapping("/{inquiryId}")
    @Operation(summary = "문의사항 상세 조회", description = "문의사항 상세 조회 API 입니다.")
    fun getInquiry(
        @PathVariable("inquiryId") inquiryId: Int
    ): ResponseResult {

        return ResponseResult(inquiryService.findInquiryById(inquiryId))
    }

}