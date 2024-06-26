package com.ex.demo.service;

import com.ex.demo.domain.dto.*;
import com.ex.demo.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class BoardServiceImpl implements BoardService{
    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private BoardFileMapper boardFileMapper;
    @Autowired
    private BoardReplyMapper boardReplyMapper;
    @Autowired
    private BoardLikeMapper boardLikeMapper;
    @Autowired
    private BoardFollowMapper boardFollowMapper;
    @Value("${file.dir}")
    private String saveFolder;

    @Override
    public boolean regist(BoardDTO boardDTO) {
        return boardMapper.regist(boardDTO)==1;
    }

    @Override
    public boolean saveFile(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        int row = boardMapper.regist(boardDTO);
        if(row != 1) {
            return false;
        }
        if(files == null || files.length == 0) {
            return true;
        }
        else {
            Long boardnum=boardMapper.lastBoardnum(boardDTO.getNickName());
            //방금 등록한 게시글 번호
            boolean flag = false;
            for(int i=0;i<files.length-1;i++) {
                MultipartFile file = files[i];
                //apple.png
                String orgname = file.getOriginalFilename();
                System.out.println(orgname);
                //5
                int lastIdx = orgname.lastIndexOf(".");
                System.out.println(lastIdx);
                //.png
                String extension = orgname.substring(lastIdx);

                LocalDateTime now = LocalDateTime.now();
                String time = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

                //20231005103911237랜덤문자열.png
                String systemname = time+ UUID.randomUUID().toString()+extension;
                System.out.println(systemname);

                //실제 저장될 파일의 경로
                String path = saveFolder +systemname;

                BoardFileDTO fdto = new BoardFileDTO();
                fdto.setBoardnum(boardnum);
                fdto.setSystemname(systemname);
                fdto.setOrgname(orgname);

                //실제 파일 업로드
                file.transferTo(new File(path));

                flag = boardFileMapper.saveFile(fdto) == 1;

                if(!flag) {
                    //업로드 했던 파일 삭제, 게시글 데이터 삭제
                    return flag;
                }
            }
        }
        return true;
    }

    @Override
    public List<BoardDTO> getboard() {
        return boardMapper.getBoard();
    }

    @Override
    public List<BoardDTO> getboardByName(String nickName) {
        return boardMapper.getboardByName(nickName);
    }

    @Override
    public ResponseEntity<Resource> getBoardThum(Long boardnum) throws Exception {
        String Systemname =  boardFileMapper.getBoardThum(boardnum).getSystemname();
        Path path = Paths.get(saveFolder+Systemname);
        String contentType = Files.probeContentType(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return new ResponseEntity<>(resource,headers,HttpStatus.OK);
    }

    @Override
    public List<BoardFileDTO> getBoardImg() {
        return boardFileMapper.findByBoardnum();
    }

    @Override
    public ResponseEntity<Resource> getBordImgs(String systemname) throws Exception {
        //경로에 관련된 객체(자원으로 가지고 와야 하는 파일에 대한 경로)
        Path path = Paths.get(saveFolder+systemname);
        //경로에 있는 파일의 MIME타입을 조사해서 그대로 담기
        String contentType = Files.probeContentType(path);
        //응답 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        //해당 경로(path)에 있는 파일에서부터 뻗어나오는 InputStream(Files.newInputStream)을 통해 자원화(InputStreamResource)
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        System.out.println(resource);

        return new ResponseEntity<>(resource,headers,HttpStatus.OK);
    }

    @Override
    public boolean clickLike(String nickName, Long boardnum) {
        boolean click=boardLikeMapper.clicklike(nickName,boardnum)==1;
        boolean likeCntUP=boardMapper.likeCntUp(boardnum)==1;
        return click&&likeCntUP;
    }

    @Override
    public boolean cancelLike(String nickName, Long boardnum) {
        boolean cancelLike=boardLikeMapper.cancelLike(nickName,boardnum)==1;
        boolean likeCntDown=boardMapper.likeCntDown(boardnum)==1;
        return cancelLike&&likeCntDown;
    }

    @Override
    public boolean follow(String user, String writer) {
        return boardFollowMapper.follow(user, writer)==1;
    }

    @Override
    public boolean cancelFollow(String user, String writer) {
        return boardFollowMapper.cancelFollow(user, writer)==1;
    }

    @Override
    public boolean registRelpy(ReplyDTO replyDTO,Long boardnum) {
        boolean registReply = boardReplyMapper.registReply(replyDTO)==1;
        boolean replyCntUp = boardMapper.replyCntUp(boardnum)==1;
        return registReply&&replyCntUp;
    }

    @Override
    public boolean removeReply(String nickName, int replynum,Long boardnum) {
        boolean removeReply = boardReplyMapper.removeReply(nickName,replynum)==1;
        boolean replyCntDown = boardMapper.replyCntDown(boardnum)==1;
        return removeReply&&replyCntDown;
    }

    @Override
    public List<ReplyDTO> getReplyList(Long boardnum) {
        return boardReplyMapper.getReplyList(boardnum);
    }

    @Override
    public boolean removeBoard(String nickName, Long boardnum) {
        return boardMapper.removeBoard(nickName,boardnum);
    }

    @Override
    public List<LikeDTO> getLikeList() {
        return boardLikeMapper.likeList();
    }

    @Override
    public List<FollowDTO> getFollowlist(String user) {
        return boardFollowMapper.followList(user);
    }

    @Override
    public List<FollowDTO> getFollowerlist(String writer) {
        return boardFollowMapper.followerList(writer);
    }
}
