package com.example.demo.controller;

import com.example.demo.model.dto.UserDto;
import com.example.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    private UserService userService;

    // 생성자
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 화면으로 이동
    @GetMapping("/join")
    public String join(){
        return ("user/join");
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserDto dto, Model model) {
        // 성공한 경우
        try {
            userService.join(dto);
            log.info("controller--" + dto.getId() + "가 가입");
            return "redirect:/";
        } catch (RuntimeException e) {
            // 실패한 경우
            model.addAttribute("msg", dto.getId()+"는 사용할 수 없습니다" );
            log.info("controller--" + "사용자 ID: " +dto.getId() + "사용자 name: " + dto.getName() + "로 가입 실패");
            return "user/join";
        }
    }

    // 사용자의 정보를 전달하기 위해
    @PostMapping("/login")
    public String login(@ModelAttribute UserDto dto, Model model, HttpSession session, HttpServletResponse response) {
        try {
            UserDto result = userService.login(dto);
            session.setAttribute("loginUser", result);

            Cookie cookie = new Cookie("name2", "쿠키");
			cookie.setMaxAge(60); // 1분
			response.addCookie(cookie);

            log.info("controller--" + dto.getId() + "가 로그인");
            return  "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("loginmsg", e.getMessage());
            log.info("controller--" + dto.getId() + "가 로그인 실패");
            return "index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        log.info("controller--" + loginUser.getId() + "가 로그아웃");
        log.info("controller--" + session.getId() + "가 로그아웃 2"); // 세션의 실제 메모리 위치
        session.invalidate();
        return "redirect:/";
    }
}
