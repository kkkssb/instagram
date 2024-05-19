package com.ex.demo.mapper;

import com.ex.demo.domain.dto.FollowDTO;
import com.ex.demo.domain.dto.LikeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardFollowMapper {
    int follow(String user,String writer);
    int cancelFollow(String user,String writer);
    List<FollowDTO> followList(String user);
}
