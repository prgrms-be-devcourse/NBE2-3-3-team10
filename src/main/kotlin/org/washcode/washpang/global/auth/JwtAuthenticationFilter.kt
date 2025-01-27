package org.washcode.washpang.global.auth

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import org.washcode.washpang.global.comm.enums.UserRole

class JwtAuthenticationFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    @Throws(ServletException::class, java.io.IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: jakarta.servlet.FilterChain
    ) {
        val accessToken = jwtProvider!!.resolveAccessToken(request)
        // 1-1. 유효한 토큰인지 확인
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            // 2. 유저정보 저장
            this.setAuthentication(accessToken)
        }
        filterChain.doFilter(request, response)
    }

    // SecurityContext 에 유저정보 받아서 저장
    fun setAuthentication(token: String) {
        val id = jwtProvider.getId(token)
        val role: UserRole = jwtProvider.getRole(token)
        val userDetails: UserDetails = CustomUserDetails(id, role)
        val authentication: UsernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(id, null, userDetails.getAuthorities())
        SecurityContextHolder.getContext().setAuthentication(authentication)
    }


    /*
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);
        // 1-1. 유효한 토큰인지 확인
        if(accessToken!=null && jwtProvider.validateToken(accessToken)){
            // 2. 유저정보 저장
            this.setAuthentication(accessToken);
            // 1-2. access 토큰 만료로 refresh 토큰 유효성 검사
        }else if (!jwtProvider.validateToken(accessToken) && refreshToken != null){
            if(jwtProvider.validateToken(refreshToken)){
                // 새로운 access 토큰 생성
                String newAccessToken = jwtProvider.generateAccessToken(
                        jwtProvider.getId(refreshToken),jwtProvider.getRole(refreshToken) );
                // header에 토큰을 넣어서 전달 추후 변경
                jwtProvider.setHeaderAccessToken(response,newAccessToken);
                this.setAuthentication(newAccessToken);
            }
        }
        filterChain.doFilter(request,response);
    }*/
}
