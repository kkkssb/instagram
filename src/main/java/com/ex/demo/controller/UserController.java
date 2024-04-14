package com.ex.demo.controller;

import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
            return "redirect:/user/join";
        }
        System.out.println("a");

        return "redirect:/user/join";

    }

}
