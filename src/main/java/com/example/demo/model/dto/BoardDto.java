package com.example.demo.model.dto;

import com.example.demo.model.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BoardDto {

    private int no;
    private String title;
    private String content;
    private String imagePath;
    private String user_Id;

    private MultipartFile image;  // 이미지 파일 자체 - DB로 보내지 않으므로 toEntity는 필요없음

//    public Board toEntity() {
//        Board board = new Board();
//        board.setNo(this.getNo());
//        board.setTitle(this.getTitle());
//        board.setContent(this.getContent());
//        board.setImagePath(this.getImagePath());
//        return board;
//    }
    
    // 빌더 패턴 적용
    public Board toEntity() {
        return Board.builder()
                .no(this.getNo())
                .title(this.getTitle())
                .content(this.getContent())
                .imagePath(this.getImagePath())
                .build();
    }
}
