package com.ex.demo.service;

import com.ex.demo.domain.dto.BoardDTO;
import com.ex.demo.domain.dto.BoardFileDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BoardService {
    boolean regist(BoardDTO boardDTO);
    boolean saveFile(BoardDTO boardDTO, MultipartFile[] files) throws IOException;
}
