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
    public String join(UserDTO userDTO) throws IOException {
        Long idx = 0L;
        if(userService.join(userDTO)){
            UserDTO user=userService.userInfo(userDTO.getNickName());
            userService.insertFile(user.getIdx());
            System.out.println("회원가입 성공");
            return "redirect:/user/login";
        }
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
        return "redirect:/user/join";
    }

    @PostMapping("profile")
    public String profile(HttpServletRequest req,MultipartFile[] files)throws Exception{
        String nickName = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(nickName);
        if(userService.insertFile(user.getIdx())){
            System.out.println("프로필 등록 완료!");
            return "redirect:/user/mypage";
        }
        return "redirect:/user/mypage";
    }
    //프로필
    @GetMapping("/getImg")
    public ResponseEntity<Resource> getProfileImage(HttpServletRequest req,String loginUser) throws IOException {
        String nickName = (String) req.getSession().getAttribute("loginUser");
        return userService.forProfile(nickName);
    }
    @GetMapping("/getproImg")
    public ResponseEntity<Resource> getProfileImage2(String user) throws IOException {
        return userService.forProfile(user);
    }
    @GetMapping("mypage")
    public String mypage(HttpServletRequest req,Model model){
        String nickName = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(nickName);
        UserFileDTO userfile = userService.userfileInfo(user.getIdx());
        model.addAttribute("user", user);
        model.addAttribute("userfile", userfile);
        return "/user/mypage";
    }

    @GetMapping("modify")
    public String modify2(HttpServletRequest req,Model model){
        String phoneNumber = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(phoneNumber);
        model.addAttribute("user", user);
        return "/user/modify";
    }
    @PostMapping("modify/{idx}")
    public String modify(@PathVariable("idx") Long idx,UserDTO userDTO,MultipartFile[] files,HttpServletRequest req)throws IOException{
        if(userService.modify(userDTO)){
            if(userService.updateFile(files,idx)){
                System.out.println("프로필사진 update 성공");
                return "redirect:/user/mypage";
            }
        }
        return "redirect:/user/mypage";
    }
}

