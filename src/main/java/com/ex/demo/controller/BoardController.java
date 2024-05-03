package com.ex.demo.controller;

import com.ex.demo.domain.dto.BoardDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board/*")
public class BoardController {
    @GetMapping("write")
    public String write(){
        return "/board/write";
    }

    @PostMapping("write")
    public String write(BoardDTO boardDTO){
        return "/board/write";
    }
}
