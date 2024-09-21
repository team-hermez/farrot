package com.hermez.farrot.member.controller;

import com.hermez.farrot.image.service.ImageService;
import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.dto.request.MemberUpdateRequest;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.member.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/member")
@Controller
public class MemberController {
    private final UserService userService;
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    public MemberController(MemberRepository memberRepository, UserService userService, ImageService imageService) {
        this.userService = userService;
        this.memberRepository = memberRepository;
        this.imageService = imageService;
    }

    @GetMapping("/register")
    public String memberRegisterPage(Model model) {
        return "member/register";
    }

    @PostMapping("/register")
    public String postMemberRegister(MemberRegisterRequest memberRegisterRequest) {
        userService.save(memberRegisterRequest);

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails userDetails) {
        // 로그인 상태면 메인 페이지로 돌려보냄
        if(userDetails != null){
            return "redirect:/";
        }
        return "member/login";
    }

    @PostMapping("/login")
    public String logIn(MemberLoginRequest memberLoginRequest, HttpServletResponse response, HttpServletRequest httpServletRequest) {
        userService.logIn(memberLoginRequest, response, httpServletRequest);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "Authorization", defaultValue="",required = false)
                             Cookie cookie, HttpServletResponse response){
        cookie.setValue(null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/member/login";
    }
  
    @GetMapping("/detail")
    String memberDetailPage(HttpServletRequest request, Model model) {
        model.addAttribute("member", userService.getMember(request));
        return "member/detail";
    }

    @PostMapping("/detail")
    String updateMemberDetail(@ModelAttribute MemberUpdateRequest memberUpdateRequest,
                              Model model) {
        System.out.println("테스트: "+memberUpdateRequest.getEmail());
        boolean complete = userService.updateUserDetail(memberUpdateRequest);

        if(complete) System.out.println("완료");

        return "redirect:/member/detail";
    }

  }
