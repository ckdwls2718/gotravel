//package com.scnu.gotravel.controller;
//
//import com.scnu.gotravel.service.ProviderService;
//import lombok.RequiredArgsConstructor;
//import lombok.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/social/login")
//public class SocialController {
//
//    private final Environment env;
//    private final ProviderService providerService;
//
//    @Value("${spring.social.google.client_id}")
//    private String googleClientId;
//    @Value("${spring.social.google.redirect}")
//    private String googleRedirect;
//
//    @Value("${spring.social.kakao.client_id}")
//    private String kakaoClientId;
//
//    @Value("${spring.social.kakao.redirect}")
//    private String kakaoRedirect;
//
//    @Value("${spring.social.naver.client_id}")
//    private String naverClientId;
//
//    @Value("${spring.social.naver.redirect}")
//    private String naverRedirect;
//
//    @GetMapping()
//    public ModelAndView socialKakaoLogin(ModelAndView mav) {
//        StringBuilder loginUrl1 = new StringBuilder()
//                .append(env.getProperty("spring.social.kakao.url.login"))
//                .append("?client_id=").append(kakaoClientId)
//                .append("&response_type=code")
//                .append("&redirect_uri=").append(kakaoRedirect);
//
//        StringBuilder loginUrl2 = new StringBuilder()
//                .append(env.getProperty("spring.social.google.url.login"))
//                .append("?client_id=").append(googleClientId)
//                .append("&response_type=code")
//                .append("&scope=email%20profile")
//                .append("&redirect_uri=").append(googleRedirect);
//
//        StringBuilder loginUrl3 = new StringBuilder()
//                .append(env.getProperty("spring.social.naver.url.login"))
//                .append("?client_id=").append(naverClientId)
//                .append("&response_type=code")
//                .append("&state=project")
//                .append("&redirect_uri=").append(naverRedirect);
//
//        mav.addObject("loginUrl1", loginUrl1);
//        mav.addObject("loginUrl2", loginUrl2);
//        mav.addObject("loginUrl3", loginUrl3);
//        mav.setViewName("login");
//        return mav;
//    }
//
//    // 인증 완료 후 리다이렉트 페이지
//    @GetMapping(value = "/{provider}")
//    public ModelAndView redirectKakao(ModelAndView mav, @RequestParam String code, @PathVariable String provider) {
//        mav.addObject("code", code);
//        mav.setViewName("redirect");
//        return mav;
//    }
//}
