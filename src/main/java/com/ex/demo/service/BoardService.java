package com.ex.demo.service;

import com.ex.demo.domain.dto.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    boolean regist(BoardDTO boardDTO);
    boolean saveFile(BoardDTO boardDTO, MultipartFile[] files) throws IOException;
    List<BoardDTO> getboard();
    List<BoardDTO> getboardByName(String nickName);
    ResponseEntity<Resource> getBoardThum(Long boardnum)throws Exception;
    List<BoardFileDTO> getBoardImg();
    ResponseEntity<Resource> getBordImgs(String systemname) throws Exception;
    boolean clickLike(String nickName,Long boardnum);
    boolean cancelLike(String nickName,Long boardnum);
    boolean follow(String user,String writer);
    boolean cancelFollow(String user,String writer);
    boolean registRelpy(ReplyDTO replyDTO,Long boardnum);
    List<ReplyDTO> getReplyList(Long boardnum);
    boolean removeBoard(String user,Long boardnum);
    boolean removeReply(String user,int replynum,Long boardnum);
    List<LikeDTO> getLikeList();
    List<FollowDTO> getFollowlist(String user);
    List<FollowDTO> getFollowerlist(String writer);
}
