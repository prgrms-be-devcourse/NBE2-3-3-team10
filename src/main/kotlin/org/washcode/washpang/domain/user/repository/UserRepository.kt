package org.washcode.washpang.domain.user.repository

import feign.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.washcode.washpang.domain.user.dto.UserDto
import org.washcode.washpang.domain.user.entity.User

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findById(id: Int): User?;

    @Query("SELECT EXISTS (SELECT true FROM User u where u.email = :email)")
    fun findByEmailExists(@Param("email") email: String): Boolean

    @Query("SELECT u.password FROM User u where u.email = :email")
    fun findByPassword(@Param("email") email: String): String

    @Query("SELECT U FROM User U WHERE U.kakao_id = :kakao_id")
    fun findIdByKakaoId(@Param("kakao_id") kakao_id: Long): User?

    @Query("SELECT new org.washcode.washpang.domain.user.dto.UserDto.MyPageRes(U.role, U.name) FROM User U WHERE U.id = :id")
    fun findRoleAndNameById(id: Int): UserDto.MyPageRes?

    fun findByEmail(email: String): User?

    @Query("SELECT U.name FROM User U WHERE U.id = :id")
    fun findNameById(id: Int): String?
}