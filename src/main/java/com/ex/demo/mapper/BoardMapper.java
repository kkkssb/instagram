package com.ex.demo.mapper;

import com.ex.demo.domain.dto.BoardDTO;
import com.ex.demo.domain.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int regist(BoardDTO boardDTO);
    Long lastBoardnum(String nickName);
    List<BoardDTO> getBoard();
    List<BoardDTO> getboardByName(String nickName);
    int likeCntUp(Long boardnum);
    int likeCntDown(Long boardnum);
    int replyCntUp(Long boardnum);
    int replyCntDown(Long boardnum);
    boolean removeBoard(String nickName,Long boardnum);

}
