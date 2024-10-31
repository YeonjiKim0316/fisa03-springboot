package com.example.demo.service;

import com.example.demo.model.dto.BoardDto;
import com.example.demo.model.entity.Board;
import com.example.demo.model.entity.User;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BoardService {

    private BoardRepository brepo;
    private UserRepository urepo;
 
    // 생성자
    public BoardService(BoardRepository brepo, UserRepository urepo) {
        this.brepo = brepo;
        this.urepo = urepo;
    }
    
    // 쓰기
    @Transactional   // 아래의 작업이 순차적으로 모두 완료됐을 때만 테이블에 글이 들어간다
    public void registBoard(BoardDto dto){
        String userId = dto.getUser_Id();
        User user = urepo.getReferenceById(userId); // User의 정보를 UserRepository에서 id를 가지고 끌어옵니다.
        Board board = dto.toEntity();
        board.setUser(user);
//        brepo.save(board);
        brepo.saveAndFlush(board);
    }
    
    // 읽기
    
    // 1. 전체 목록 읽기 
//    public List<Board> list() {
//        return brepo.findAll();
//    }

    // -1. 전체 글 읽기 페이지로 끊어서 값을 가지고 오는 방법이 있습니다. - Page 객체 활용
    public Page<Board> listBoard(int page){
        // 최신순 정렬 - 내림차순
        Pageable pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "no");
        Page<Board> pageInfo = brepo.findAll(pageable);
        // 어떻게 객체가 오는지 확인
        log.info("service--pageInfo {}", pageInfo.get().toList());
        return pageInfo;
    }

    // 2. 특정 값 읽기  {no} 
    public Optional<Board> detail(int no){  // no가 없으면 null, 아니면 해당 글의 Board 객체를 찾아옵니다.
        return brepo.findById(no);
    }

    // 삭제 {no}
    public void delete(int no){
        brepo.deleteById(no);
    }
    
    // 수정
    
}
