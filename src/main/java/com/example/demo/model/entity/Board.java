package com.example.demo.model.entity;

import com.example.demo.model.dto.BoardDto;
import jakarta.persistence.*;
import lombok.*;

// 하나의 글은 하나의 사용자를 가집니다.
// 한명의 사용자는 여러개의 글을 쓸 수 있습니다.

@NoArgsConstructor
@AllArgsConstructor
@Data  // Required 생성자만 지원
@Entity
@Builder
@ToString(exclude = "user")
public class Board {

    // 글번호, 글제목, 글내용, 이미지파일경로
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AutoIncrement +1씩
    private int no;
    private String title;
    private String content;
    private String imagePath;

    @ManyToOne // N:1 - 1명의 사용자가 여러개의 게시글 작성
    @JoinColumn(name="user_id")
    User user; // select * from user where id=??

    // dto로 데이터를 전송할 메서드 toDto 작성
    public BoardDto toDto() {
        BoardDto dto = new BoardDto();
        dto.setNo(this.getNo());
        dto.setTitle(this.getTitle());
        dto.setContent(this.getContent());
        dto.setImagePath(this.getImagePath());  // DB에 저장한 이미지 경로 보내기
        dto.setUser_Id(this.getUser().getId());
        return dto;
    }


}
