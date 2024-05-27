package com.ex.demo.mapper;

import com.ex.demo.domain.dto.BoardDTO;
import com.ex.demo.domain.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardReplyMapper {
    int registReply(ReplyDTO replyDTO);
    List<ReplyDTO> getReplyList(Long boardnum);
    int removeReply(String nickName,int replynum);
}
