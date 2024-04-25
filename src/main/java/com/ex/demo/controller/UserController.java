package com.ex.demo.controller;

import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.domain.dto.UserFileDTO;
import com.ex.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        System.out.println(loginUser);
        if(loginUser!=null){
            req.getSession().setAttribute("loginUser",loginUser.getName());
            return "redirect:/user/mypage";
        }
        System.out.println("a");

        return "redirect:/user/join";
    }
    @GetMapping("modify")
    public String modify(HttpServletRequest req, Model model){
        // 세션에서 loginUser 속성을 가져와서 닉네임을 읽어옵니다.
        String Name = (String) req.getSession().getAttribute("loginUser");

        // 닉네임을 모델에 추가하여 마이페이지로 전달합니다.
        model.addAttribute("nickName", Name);

        return "/user/modify";
    }
    @PostMapping("profile")
    public String profile(HttpServletRequest req,MultipartFile[] files)throws Exception{
        String name = (String) req.getSession().getAttribute("loginUser");
        if(userService.insertFile(files,name)){
            System.out.println("afaf");
            return "redirect:/user/mypage";
        }
        return "redirect:/user/mypage";
    }
    //프로필
    @GetMapping("/getImg")
    public ResponseEntity<Resource> getProfileImage(HttpServletRequest req,String loginUser) throws IOException {
        String name = (String) req.getSession().getAttribute("loginUser");
        System.out.println("user controller - getProfileImage/"+name);
        return userService.forProfile(name);
    }
    @GetMapping("mypage")
    public String mypage(HttpServletRequest req,Model model){
        String nickName = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(nickName);
        model.addAttribute("user", user);
        return "/user/mypage";
    }

    @GetMapping("modify2")
    public String modify2(HttpServletRequest req,Model model){
        String nickName = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(nickName);
        model.addAttribute("user", user);
        return "/user/modify2";
    }
    @PostMapping("modify2/{idx}")
    public String modify22(@PathVariable("idx") Long idx,UserDTO userDTO,MultipartFile[] files,HttpServletRequest req)throws IOException{
        System.out.println(userDTO);
        if(userService.modify(userDTO)){
            String name = (String) req.getSession().getAttribute("loginUser");
            if(userService.updateFile(files,name)){
                System.out.println("update성공");
                return "redirect:/user/mypage";
            }
            System.out.println("here");
            return "redirect:/user/mypage";
        }
        return "redirect:/user/mypage";
    }
}

