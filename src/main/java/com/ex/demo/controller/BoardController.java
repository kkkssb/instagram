package com.ex.demo.controller;

import com.ex.demo.domain.dto.*;
import com.ex.demo.service.BoardService;
import com.ex.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("getBoards")
    public ResponseEntity<List<BoardDTO>> getBoardsByAjax(){
        List<BoardDTO> board=boardService.getboard();
        System.out.println("abc"+board);
        return ResponseEntity.ok(board);
    }
    @GetMapping("getBoardImgs")
    public ResponseEntity<List<BoardFileDTO>> getBoardImgsByAjax(){
        List<BoardFileDTO> boardFile=boardService.getBoardImg();
        return ResponseEntity.ok(boardFile);
    }
    //게시글 넘버로 잉미지 가져오기

    @GetMapping("home")
    public String home(HttpServletRequest req, Model model){
        String nickName1 = (String) req.getSession().getAttribute("loginUser");
        UserDTO user=userService.userInfo(nickName1);
        List<BoardDTO> list = boardService.getboard();
        UserFileDTO userfile = userService.userfileInfo(user.getIdx());
        model.addAttribute("list", list);
        model.addAttribute("files", boardService.getBoardImg());
        model.addAttribute("user", user);
        model.addAttribute("userfile", userfile);
        System.out.println(boardService.getBoardImg());
        return "/board/home";
    }
    @GetMapping("getBImgs")
    public ResponseEntity<Resource> getBoardImgs(String systemname) throws Exception {
        return boardService.getBordImgs(systemname);
    }

    @PostMapping(value = "/clickLike/{loginUser}/{boardnum}")
    public ResponseEntity<String> clickLike(
            @PathVariable("loginUser") String loginUser,
            @PathVariable("boardnum") Long boardnum
    ) {
        boolean check = boardService.clickLike(loginUser, boardnum);
        if (check) {
            System.out.println("좋아요 성공");
            return ResponseEntity.ok("success");
        } else {
            System.out.println("좋아요 실패");
            return ResponseEntity.badRequest().body("fail");
        }
    }
    @PostMapping(value = "/cancelLike/{loginUser}/{boardnum}")
    public ResponseEntity<String> cancelLike(
            @PathVariable("loginUser") String loginUser,
            @PathVariable("boardnum") Long boardnum
    ) {
        boolean check = boardService.cancelLike(loginUser, boardnum);
        if (check) {
            System.out.println("좋아요 취소성공");
            return ResponseEntity.ok("success");
        } else {
            System.out.println("좋아요 취소실패");
            return ResponseEntity.badRequest().body("fail");
        }
    }
    @GetMapping("likeList")
    public ResponseEntity<List<LikeDTO>> getLikelist(){
        List<LikeDTO> list = boardService.getLikeList();
//        System.out.println("list: "+list);
        return ResponseEntity.ok(list);
    }
    @GetMapping(value = "/followList/{user}")
    public ResponseEntity<List<FollowDTO>> getFollowlist(@PathVariable("user") String user){
        List<FollowDTO> list = boardService.getFollowlist(user);
        System.out.println("list: "+list);
        return ResponseEntity.ok(list);
    }
    @PostMapping(value = "/follow/{user}/{writer}")
    public ResponseEntity<String> follow(
            @PathVariable("user") String loginUser,
            @PathVariable("writer") String writer
    ) {
        boolean check = boardService.follow(loginUser, writer);
        if (check) {
            System.out.println("팔로우 성공");
            return ResponseEntity.ok("success");
        } else {
            System.out.println("팔로우 실패");
            return ResponseEntity.badRequest().body("fail");
        }
    }
    @PostMapping(value = "/cancelFollow/{user}/{writer}")
    public ResponseEntity<String> cancelFollow(
            @PathVariable("user") String loginUser,
            @PathVariable("writer") String writer
    ) {
        boolean check = boardService.cancelFollow(loginUser, writer);
        if (check) {
            System.out.println("팔로우 취소성공");
            return ResponseEntity.ok("success");
        } else {
            System.out.println("팔로우 취소실패");
            return ResponseEntity.badRequest().body("fail");
        }
    }

}
