package com.ex.demo.mapper;

import com.ex.demo.domain.dto.BoardDTO;
import com.ex.demo.domain.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    int regist(BoardDTO boardDTO);
    Long lastBoardnum(String nickName);
}
