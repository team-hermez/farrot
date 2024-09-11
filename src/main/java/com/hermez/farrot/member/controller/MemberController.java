package com.hermez.farrot.member.controller;

import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.member.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member")
@Controller
public class MemberController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;

    public MemberController(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder, UserService userService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/register")
    String memberRegisterPage(Model model) {
        return "member/register";
    }

    @PostMapping("/register")
    String postMemberRegister(MemberRegisterRequest memberRegisterRequest) {
        userService.save(memberRegisterRequest);

        return "redirect:/login";
    }
    @GetMapping("/login")
    String memberLoginPage(Model model) {
        return "member/login";
    }

    @PostMapping("/login")
    String postMember(Model model) {
        return "member/login";
    }


    @GetMapping("/detail")
    String memberDetailPage(Model model) {
        return "member/detail";
    }
}
