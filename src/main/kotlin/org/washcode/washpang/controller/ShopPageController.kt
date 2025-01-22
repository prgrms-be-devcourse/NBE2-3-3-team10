package org.washcode.washpang.controller

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopPageController {

    @RequestMapping("/shop-main")
    fun shopMain(model: Model): String { return "shop/shop-main"; }

    @RequestMapping("/pickup-check")
    fun pickupCheck(model: Model): String { return "shop/pickup-check"; }

    @RequestMapping("/pickup-list")
    fun pickupList(model: Model): String { return "shop/pickup-list"; }

    @RequestMapping("/pickup-detail")
    fun pickupDetail(): String { return "shop/pickup-detail"; }

    @RequestMapping("/pickup-delivery")
    fun pickupDelivery(model: Model): String { return "shop/pickup-delivery"; }

    @RequestMapping("/sales-summary")
    fun salesSummary(model: Model): String { return "shop/sales-summary"; }

    @RequestMapping("/shop-review")
    fun shopReview(model: Model): String { return "shop/shop-review"; }

    @RequestMapping("/modify-shop-info")
    fun modifyShopInfo(model: Model): String { return "shop/modify-shop-info"; }

    @RequestMapping("/mypage")
    fun shopMyPage(): String { return "shop/shop-my-page"; }

    @RequestMapping("/myInfoModify")
    fun shopInfoModify(): String { return "shop/my-info-modify-page"; }

    @RequestMapping("/myInfo")
    fun shopInfo(): String { return "shop/my-info"; }
}
