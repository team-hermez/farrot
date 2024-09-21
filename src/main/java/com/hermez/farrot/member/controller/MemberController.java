package com.hermez.farrot.member.controller;

import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.image.service.ImageService;
import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.dto.request.MemberUpdateRequest;
import com.hermez.farrot.member.entity.Member;
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

import java.util.List;

@Slf4j
@RequestMapping("/member")
@Controller
public class MemberController {
    private final UserService userService;
    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository, UserService userService) {
        this.userService = userService;
        this.memberRepository = memberRepository;
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
        Member member= userService.getMember(request);
        List<Image> image = userService.userImage(member).getImages();
        int lastIndex = image.size() - 1;
        System.out.println("lastIndex: " + lastIndex);
        System.out.println("imagePath: "+image.get(lastIndex).getPath());

        model.addAttribute("member", member);
        model.addAttribute("image", image.get(lastIndex));

        return "member/detail";
    }

    @PostMapping("/detail")
    String updateMemberDetail(@ModelAttribute MemberUpdateRequest memberUpdateRequest,
                              @RequestPart(name = "profileImage") MultipartFile file) {
        boolean complete = userService.updateUserDetail(memberUpdateRequest, file);

        if(complete==true) System.out.println("완료");
        else System.out.println("실패");

        return "redirect:/member/detail";
    }
}
