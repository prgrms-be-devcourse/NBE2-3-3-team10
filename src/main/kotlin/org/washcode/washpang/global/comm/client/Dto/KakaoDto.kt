package org.washcode.washpang.global.comm.client.Dto

import com.fasterxml.jackson.databind.JsonNode
import lombok.Getter
import java.util.UUID

class KakaoDto private constructor() {

    data class Data(
        val id: Long,
        val email: String,
        val nickname: String,
        val password: String
    ) {
        companion object {
            @JvmStatic
            fun from(json: JsonNode): Data {
                return Data(
                    id = json["id"].asLong(),
                    email = json["kakao_account"]["email"].asText(""),
                    nickname = json["properties"]["nickname"].asText(""),
                    password = UUID.randomUUID().toString().replace("-", "").substring(0, 12)
                )
            }
        }
    }

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