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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
        if (user == null) {
            return "redirect:/user/login";
        }
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
        if (user == null) {
            return "redirect:/user/login";
        }
        List<BoardDTO> list = boardService.getboard();
        UserFileDTO userfile = userService.userfileInfo(user.getIdx());
        model.addAttribute("list", list);
        model.addAttribute("files", boardService.getBoardImg());
        model.addAttribute("user", user);
        model.addAttribute("userfile", userfile);
        System.out.println(boardService.getBoardImg());
        return "/board/home";
    }
    @GetMapping("getBoardThum")
    public ResponseEntity<Resource> getBoardThum(Long boardnum) throws Exception {
        return boardService.getBoardThum(boardnum);
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
        return ResponseEntity.ok(list);
    }
    @GetMapping(value = "/followList/{user}")
    public ResponseEntity<List<FollowDTO>> getFollowlist(@PathVariable("user") String user){
        List<FollowDTO> list = boardService.getFollowlist(user);
        return ResponseEntity.ok(list);
    }
    @PostMapping(value = "/registReply/{loginUser}/{boardnum}")
    public ResponseEntity<String> registReply(
            @PathVariable("loginUser") String loginUser,
            @PathVariable("boardnum") Long boardnum,
            @RequestBody Map<String, String> requestBody
    ) {
        String text = requestBody.get("text");
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setNickName(loginUser);
        replyDTO.setBoardnum(boardnum);
        replyDTO.setContents(text);
        if(boardService.registRelpy(replyDTO,boardnum)){
            System.out.println("댓글 등록 완료");
        }
        return ResponseEntity.ok("댓글이 성공적으로 등록되었습니다.");
    }
    @GetMapping(value = "/getReply/{boardnum}")
    public ResponseEntity<List<ReplyDTO>> getReply(@PathVariable("boardnum") Long boardnum){
        List<ReplyDTO> list = boardService.getReplyList(boardnum);
        return ResponseEntity.ok(list);
    }
    @PostMapping(value = "/removeBoard/{user}/{boardnum}")
    public ResponseEntity<String> removeBoard(
            @PathVariable("user") String loginUser,
            @PathVariable("boardnum") Long boardnum
    ) {
        if (boardService.removeBoard(loginUser,boardnum)) {
            System.out.println("글 삭제 완료");
            return ResponseEntity.ok("success");
        } else {
            System.out.println("글 삭제 실패");
            return ResponseEntity.badRequest().body("fail");
        }
    }
    @PostMapping(value = "/removeReply/{user}/{replynum}/{boardnum}")
    public ResponseEntity<String> removeReply(
            @PathVariable("user") String loginUser,
            @PathVariable("replynum") int replynum,
            @PathVariable("boardnum") Long boardnum
    ) {
        if (boardService.removeReply(loginUser,replynum,boardnum)) {
            System.out.println("댓글 삭제 완료");
            return ResponseEntity.ok("success");
        } else {
            System.out.println("댓글 삭제 실패");
            return ResponseEntity.badRequest().body("fail");
        }
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
