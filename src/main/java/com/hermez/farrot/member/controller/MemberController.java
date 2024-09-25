package com.hermez.farrot.member.controller;

import com.hermez.farrot.image.entity.Image;
import com.hermez.farrot.member.dto.request.MemberCheckRequest;
import com.hermez.farrot.member.dto.request.MemberLoginRequest;
import com.hermez.farrot.member.dto.request.MemberRegisterRequest;
import com.hermez.farrot.member.dto.request.MemberUpdateRequest;
import com.hermez.farrot.member.entity.Member;
import com.hermez.farrot.member.exception.NotFoundMemberException;
import com.hermez.farrot.member.repository.MemberRepository;
import com.hermez.farrot.member.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String postMemberRegister(MemberRegisterRequest memberRegisterRequest, Model model) {
        try {
            userService.save(memberRegisterRequest);
            return "redirect:/member/login";
        }catch (NotFoundMemberException ex){
            model.addAttribute("errorMessage", ex.getMessage());
            return "member/register";
        }
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
    public String logIn(MemberLoginRequest memberLoginRequest, HttpServletResponse response,
                        HttpServletRequest httpServletRequest, Model model) {
        try {
            userService.logIn(memberLoginRequest, response, httpServletRequest);
            return "redirect:/";
        }catch (NotFoundMemberException ex){
            model.addAttribute("errorMessage", ex.getMessage());
            return "member/login";
        }
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

        model.addAttribute("member", member);
        model.addAttribute("image", image.get(lastIndex));

        return "member/detail";
    }

    @PostMapping("/detail")
    String updateMemberDetail(@ModelAttribute MemberUpdateRequest memberUpdateRequest,
                              @RequestPart(name = "profileImage") MultipartFile file,
                              HttpServletRequest request,Model model) {
        try {
            userService.updateUserDetail(memberUpdateRequest, file);
            return "redirect:/member/detail";
        }catch (NotFoundMemberException ex){
            Member member= userService.getMember(request);
            List<Image> image = userService.userImage(member).getImages();
            int lastIndex = image.size() - 1;

            model.addAttribute("member", member);
            model.addAttribute("image", image.get(lastIndex));
            model.addAttribute("errorMessage", ex.getMessage());
            return "member/detail";
        }
    }
    // 중복체크
    @PostMapping("/check/email")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmail(@RequestBody MemberCheckRequest memberCheckRequest) {
        boolean isAvailable = userService.isEmailAvailable(memberCheckRequest.getEmail());
        return ResponseEntity.ok(isAvailable);
    }

    @PostMapping("/check/nickname")
    @ResponseBody
    public ResponseEntity<Boolean> checkNickname(@RequestBody MemberCheckRequest memberCheckRequest) {
        boolean isAvailable = userService.isNicknameAvailable(memberCheckRequest.getNickname());
        return ResponseEntity.ok(isAvailable);
    }
    @PostMapping("/check/expassword")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkExPassword(@RequestBody Map<String, String> payload,
                                                                HttpServletRequest request) {
        String exPassword = payload.get("exPassword");
        System.out.println("controller request"+request);
        System.out.println("exps: "+ exPassword);

        boolean isValid = userService.validateCurrentPassword(exPassword,request);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);

        return ResponseEntity.ok(response);
    }
}
