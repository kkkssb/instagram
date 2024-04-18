package com.ex.demo.mapper;

import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.domain.dto.UserFileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper
public interface UserFileMapper {
    int insertFile(UserFileDTO fdto);
    List<UserFileDTO> getFiles(String name);


}
