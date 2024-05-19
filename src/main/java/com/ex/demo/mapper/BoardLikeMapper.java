package com.ex.demo.mapper;

import com.ex.demo.domain.dto.BoardDTO;
import com.ex.demo.domain.dto.LikeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardLikeMapper {
    int clicklike(String nickName,Long boardnum);
    int cancelLike(String nickName,Long boardnum);
    List<LikeDTO> likeList();
}
