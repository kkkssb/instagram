package com.ex.demo.controller;

import com.ex.demo.domain.dto.BoardDTO;
import com.ex.demo.domain.dto.BoardFileDTO;
import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.domain.dto.UserFileDTO;
import com.ex.demo.service.BoardService;
import com.ex.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/board/*")
public class BoardController {
    @Autowired
    private UserService userService;
    @Autowired
    private BoardService boardService;
    @GetMapping("write")
    public String write(HttpServletRequest req, Model model){
        String nickName = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(nickName);
        UserFileDTO userfile = userService.userfileInfo(user.getIdx());
        model.addAttribute("user", user);
        model.addAttribute("userfile", userfile);
        return "/board/write";
    }

    @PostMapping("write")
    public String write(BoardDTO boardDTO, MultipartFile[] files)throws Exception{
        if(boardService.saveFile(boardDTO,files)){
            System.out.println("스토리 등록 완료");
            return "redirect:/user/mypage";
        }
        return "redirect:/user/mypage";
    }

    @GetMapping("home")
    public String home(HttpServletRequest req, Model model){
        String nickName = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(nickName);
        List<BoardDTO> list = boardService.getboard();
        UserFileDTO userfile = userService.userfileInfo(user.getIdx());
        model.addAttribute("list", list);
        model.addAttribute("files", boardService.getBoardImg());
        model.addAttribute("user", user);
        model.addAttribute("userfile", userfile);
        System.out.println(boardService.getBoardImg());
        return "/board/home";
    }
    @GetMapping("getBoardImgs")
    public ResponseEntity<Resource> getBoardImgs(String systemname) throws Exception {
        System.out.println("MBoard controller - thumbnail");
        return boardService.getBordImgs(systemname);
    }

}
