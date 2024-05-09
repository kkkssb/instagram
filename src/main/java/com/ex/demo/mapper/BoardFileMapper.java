package com.ex.demo.mapper;

import com.ex.demo.domain.dto.BoardFileDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardFileMapper {
    int saveFile(BoardFileDTO boardFileDTO);
}
