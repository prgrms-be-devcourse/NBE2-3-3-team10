package org.washcode.washpang.controller

import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.washcode.washpang.global.client.KakaoClient


@Controller
@RequiredArgsConstructor
class CommPageController(
    private val kakaoClient: KakaoClient
) {
//   private final KakaoClient kakaoClient;

    @Value("\${kakao.redirect-uri}")
    private var redirectUri: String = ""

    @Value("\${kakao.key.client-id}")
    private var kakaoApiKey: String = ""

    @RequestMapping("/")
    fun login(model: Model): String {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        model.addAttribute("redirectUri", redirectUri);

        return "glober/login";
    }

    @RequestMapping("/kakaoLogin")
    fun kakaoLogin(@RequestParam("code") code: String, model: Model, response: HttpServletResponse): String {
        return kakaoClient.login(code, model, response);
    }

    @RequestMapping("/register")
    fun register(model: Model): String {
        model.addAttribute("kakaoUserData", null);
        return "glober/register";
    }
}