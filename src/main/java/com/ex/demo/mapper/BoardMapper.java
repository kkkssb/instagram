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
    int likeCntUp(Long boardnum);
    int likeCntDown(Long boardnum);

}
