package com.ex.demo.controller;

import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("join")
    public String join(){
        return "/user/join";
    }
    @PostMapping("join")
    public String join(UserDTO userDTO) {
        Long idx = 0L;
        if(userService.join(userDTO)){
            System.out.println("afaf");
            return "redirect:/user/login";
        }
        System.out.println("a");

        return "redirect:/user/join";

    }
    @GetMapping("login")
    public String login(){
        return "/user/login";
    }

    @PostMapping("login")
    public String login(int phoneNumber, String password, HttpServletRequest req) {
        UserDTO loginUser= userService.login(phoneNumber,password);
        if(loginUser!=null){
            req.getSession().setAttribute("loginUser",loginUser.getNickName());
            return "redirect:/user/mypage";
        }
        System.out.println("a");

        return "redirect:/user/join";
    }
    @GetMapping("mypage")
    public String mypage(HttpServletRequest req, Model model){
        // 세션에서 loginUser 속성을 가져와서 닉네임을 읽어옵니다.
        String nickName = (String) req.getSession().getAttribute("loginUser");

        // 닉네임을 모델에 추가하여 마이페이지로 전달합니다.
        model.addAttribute("nickName", nickName);

        return "/user/mypage";
    }


}
