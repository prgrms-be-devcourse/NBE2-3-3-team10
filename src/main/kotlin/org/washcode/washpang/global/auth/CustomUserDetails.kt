package org.washcode.washpang.global.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.washcode.washpang.global.comm.enums.UserRole

class CustomUserDetails(private val id: Int,private val  role: UserRole) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_$role"));
    }

    override fun getUsername(): String {return id.toString()}
    override fun getPassword(): String? {return null}

    override fun isEnabled(): Boolean {return true}
    override fun isCredentialsNonExpired(): Boolean {return true}
    override fun isAccountNonExpired(): Boolean {return true}
    override fun isAccountNonLocked(): Boolean {return true}

}
