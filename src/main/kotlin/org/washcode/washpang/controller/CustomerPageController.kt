package org.washcode.washpang.controller

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
class CustomerPageController {
    @RequestMapping("/main")
    fun main(): String { return "customer/main" }

    @RequestMapping("/mypage")
    fun myPage(): String { return "customer/my-page" }

    @RequestMapping("/myInfo")
    fun myInfo(): String { return "customer/my-info" }

    @RequestMapping("/myInfoModify")
    fun myInfoModify(): String { return "customer/my-info-modify-page" }

    @RequestMapping("/order/{laundryId}")
    fun order(@PathVariable("laundryId") laundryId: Int, model: Model): String{
        model.addAttribute("laundryId",laundryId)
        return "customer/apply-pickup"
    }

    @RequestMapping("/order/completed")
    fun orderCompleted(@RequestParam("pg_token") token: String?, model: Model): String {
        model.addAttribute("pg_token", token)
        return "customer/order-wait"
    }

    @RequestMapping("/order/success")
    fun orderSuccess():  String { return "customer/order-complete" }

    @RequestMapping("/orderHistory")
    fun orderHistory(): String { return "customer/order-history" }

    @RequestMapping("/orderHistory/{pickupId}")
    fun orderHistoryDetail(@PathVariable("pickupId") pickupId: Int, model: Model): String{
        model.addAttribute("pickupId", pickupId)
        return "customer/order-history-detail"
    }

    @RequestMapping("/laundryshop-by-map")
    fun  laundryshopByMap():String {
        return "customer/laundryshop-by-map";
    }

    @RequestMapping("/laundryshop-by-category/{category}")
    fun laundryshopByCategory(@PathVariable("category")category: String, model: Model): String {
//        LaundryCategory laundryCategory = LaundryCategory.valueOf(category.toUpperCase())
//        categoryName:String  = laundryCategory.getDescription()
//
//        model.addAttribute("category", category);
//        model.addAttribute("categoryName", categoryName);
        return "customer/laundryshop-by-category"
    }

    @RequestMapping("/laundryshop-detail/{laundryId}")
    fun laundryshopDetail(@PathVariable("laundryId") laundryId: Int, model: Model):String {
//        val to : LaundryDetailResDTO = laundryShopService.getLaundryShopById(laundryId)
//
//        if(to == null) { return "error"; }
//
//        model.addAttribute("laundry", to)
//        model.addAttribute("laundryId", laundryId)
        return "customer/laundryshop-detail"
    }
}