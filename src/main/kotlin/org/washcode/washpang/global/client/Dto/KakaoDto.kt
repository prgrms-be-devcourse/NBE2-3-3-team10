package org.washcode.washpang.global.client.Dto

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter

class KakaoDto private constructor() {

//    data class Data(
//        val id: Long,
//        val connected_at: String,
//        val properties: Properties,
//        val kakao_account: KakaoAccount
//    ) {
//        data class Properties(
//            val nickname: String
//        )
//
//        data class KakaoAccount(
//            val profile_nickname_needs_agreement: Boolean,
//            val profile: Profile,
//            val has_email: Boolean,
//            val email_needs_agreement: Boolean,
//            val is_email_valid: Boolean,
//            val is_email_verified: Boolean,
//            val email: String
//        )
//
//        data class Profile(
//            val nickname: String,
//            val is_default_nickname: Boolean
//        )
//    }

    data class Data(
        val id: Long,
        val email: String,
        val nickname: String,
        val password: String
    )

    @Getter
    data class AccessToken (
        val grant_type: String = "authorization_code",
        val client_id: String,
        val redirect_uri: String,
        val code: String
    ) {
        override fun toString(): String {
            return "code=" + code + '&' +
                    "client_id=" + client_id + '&' +
                    "redirect_uri=" + redirect_uri + '&' +
                    "grant_type=" + grant_type
        }
    }

    data class KakaoAccessToken (
        val token_type: String,
        val access_token: String,
        val id_token: String,
        val expires_in: Int,
        val refresh_token: String,
        val refresh_token_expires_in: Int,
        val scope: String
    )
}