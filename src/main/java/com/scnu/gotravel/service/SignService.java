//package com.scnu.gotravel.service;
//
//
//
//import com.scnu.gotravel.auth.AccessToken;
//import com.scnu.gotravel.auth.profile.ProfileDto;
//import com.scnu.gotravel.dto.member.MemberLoginResponseDto;
//import com.scnu.gotravel.jwt.JwtTokenProvider;
//import com.scnu.gotravel.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Slf4j
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class SignService {
//
//    private final ProviderService providerService;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final PasswordEncoder passwordEncoder;
//    private final MemberRepository memberRepository;
//
//    @Transactional
//    public MemberLoginResponseDto loginMemberByProvider(String code, String provider){
//        AccessToken accessToken =
//        ProfileDto profileDto = providerService.getProfile(accessToken.getAccess_token(), provider);
//
//    }
//
//
//}
