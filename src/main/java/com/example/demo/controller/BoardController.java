package com.example.demo.controller;

import com.example.demo.model.dto.BoardDto;
import com.example.demo.model.entity.Board;
import com.example.demo.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Tag(name="swagger API for Board", description = "Board APIs")
@Controller // ModelAndView를 거치는 컨트롤러
//@RestController
@RequestMapping("/board")
public class BoardController {

    private BoardService service;

    // 생성자
    public BoardController(BoardService service) {
        this.service = service;
    }
    
    @Operation(summary = "게시글 등록 페이지로 이동", description = "글쓰기 화면으로 이동만 합니다")
    @GetMapping("/regist")
    public String registForm(){
//        log.info("controller--" + dto.getId() + "가 목록 조회"); --- userId가 없음
        return "board/registboard";
    }

    // 2-1. 글을 Form으로 받아서 전달
//    @PostMapping("/regist")
//    public String registBoard(@ModelAttribute BoardDto board) {  // 입력받은 값을 바로 화면에서 ${ }로 사용할 수 있는 requestScope로 전달
//        service.registBoard(board);
//        return "redirect:/board/list";
//    }


    // 2-2. 글과 이미지를 Form으로 받아서 전달
//    @Autowired  // Spring Bean에 servletContext라는 변수를 등록
//    private ServletContext servletContext; // 현재 파일이 실행중인 경로 등을 상대경로로 사용하기 위해
//
//    @Value("${upload.path}") // application.properties에 적어놓은 upload.path의 값을 가져옵니다
//     private String uploadPath;
//
//    @PostMapping("/regist")
//    public String registBoard(@ModelAttribute BoardDto board) {  // 입력받은 값을 바로 화면에서 ${ }로 사용할 수 있는 requestScope로 전달
//        // 1) Dto에 멀티파트파일 추가
//        // 2) uploadPath @Value 전달
//        MultipartFile imageFile = board.getImage();
////        System.out.println(imageFile);
//        String originalFileName = imageFile.getOriginalFilename(); // 파일명이 String으로 리턴
////        System.out.println(uploadPath);
////         이미지 파일은 경로에 저장  - 절대 경로  @Value로 위에 선언한 application.properties 내부의 절대경로 가져와서 사용
//        String uploadDir = servletContext.getRealPath("/upload/");
//        String filePath = uploadDir + originalFileName; //  uploadPath+ cat.jpg
//
////         1)  이미지 파일은 경로에 저장  - 상대 경로
//
////		System.out.println(uploadDir);
////        System.out.println(filePath);
//
//         try {
//         	imageFile.transferTo(new File(filePath));
//         } catch (IllegalStateException e) {
//         	e.printStackTrace();
//         } catch (IOException e) {
//         	e.printStackTrace();
//         }
//            return "redirect:/board/list";
//}

    @Autowired
    private S3Client s3Client;

    //    2-3. 글과 이미지를 Form으로 받아서 AWS S3로 전달
    @PostMapping("/regist")
    public String registBoard(@ModelAttribute BoardDto board) {
                MultipartFile imageFile = board.getImage();
                String originalFileName = imageFile.getOriginalFilename(); // 파일명이 String으로 리턴
            try {
                // 객체 스토리지로 저장하는 코드
                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket("upload") // 파일을 집어넣을 버킷(폴더)
                        .key(originalFileName) // 파일명
                        .build(); // 요청을 작성 (어느 버켓에, 어떤 이름으로 저장할지)
                // 객체 스토리지 클라이언트에게 요청과 파일(바이트)을 보내는 명령
                System.out.println(request.toString());
                s3Client.putObject(request, RequestBody.fromBytes(imageFile.getBytes()));

                // 이미지파일의 이름을 받아서 imagePath 라는 필드에 담아서 board Entity로 보냄
                // 저장은 전체 경로에 하되 db에는 /upload/폴더 이하의 경로만 저장
                board.setImagePath(originalFileName); // image path를 저장
                log.info("controller--" + board.getUser_Id() + "가 목록 조회"); // userId가 없음
            service.registBoard(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/board/list";
    }
    
//    @Operation(summary = "Book 정보 모두 조회", description = "Book의 전체 정보를 조회합니다.")
//    @GetMapping("/list")  // Model - spring ui 입력받은 값을 바로 화면에서 ${ }로 사용할 수 있는 requestScope로 전달
//    public String list(Model model) {
//        List<Board> boardList = service.list();
//        model.addAttribute("pageInfo", boardList);
//        return "board/list";
//    }

    // 목록을 출력하는 메서드   /board/list      ?no=1
    @Operation(summary = "Book 정보 모두 조회", description = "Book의 전체 정보를 페이지별로 조회합니다.")
    @GetMapping("/list")  // Model - spring ui 입력받은 값을 바로 화면에서 ${ }로 사용할 수 있는 requestScope로 전달
    public String listBoard(@RequestParam(required = false, defaultValue =  "1") Integer page, Model model) {
        page--;
        Page<Board> pageInfo = service.listBoard(page);
        model.addAttribute("pageInfo", pageInfo);

//        log.debug("page: {}",page);
//        log.debug("pageInfo: {}",pageInfo);
        return "board/list";

    }

//    // 글 하나를 출력하는 메서드
//    @GetMapping("/detail")
//    public String detail(@RequestParam int no, Model model) {
//        try {
//            Optional<Board> board = service.detail(no);
//            model.addAttribute("board", board);  // ${board.title} ${board.content}
//            return "/board/detail";
//        } catch (RuntimeException e) { // 없는 글번호를 요청했을 때
//            return "/board/list";  // /WEB-INF/view/list.jsp
//        }
//    }

    // 글 하나를 출력하는 메서드  -> 로직을 SERVICE로 옮기는 리팩터링
    @GetMapping("/detail")
    public String detail(@RequestParam int no, Model model) {
        try {
            Optional<Board> optionalBoard = service.detail(no);
            if (optionalBoard.isPresent()) {
                Board board = optionalBoard.get();
                model.addAttribute("board", board);  // ${board.title} ${board.content}
//                log.info("controller--" + board.getUser_Id() + "가 글번호 "+ no + " 조회"); // userId가 없음
                return "/board/detail";
            } else {
                // 없는 글번호를 요청했을 때
                return "redirect:/board/list";  // /WEB-INF/view/list.jsp
            }
        } catch (RuntimeException e) {
            // 예외 발생 시 리스트 페이지로 리다이렉트
            return "redirect:/board/list";  // /WEB-INF/view/list.jsp
        }
    }

    // 삭제하는 메서드  /delete?no=3
    @GetMapping("/delete")
    public String delete(@RequestParam int no) {
        service.delete(no);
//        log.info("controller--" + board.getUser_Id() + "가 글번호 "+ no + " 삭제"); // userId가 없음
        return "redirect:/board/list";  // /WEB-INF/view/list.jsp
    }


//    // 수정하는 메서드
//    @PostMapping("/update")
//    public String update(@ModelAttribute BoardDto dto,Model model) {
//        service.registBoard(dto);
//        return "redirect:/board/detail?no=" +dto.getNo();
//    }

    // 수정하는 메서드
    @PostMapping("/update")
    public String update(@ModelAttribute BoardDto dto,Model model) {
//        log.debug("board 수정: {}", dto);
        MultipartFile imageFile = dto.getImage();
        String originalFileName = imageFile.getOriginalFilename(); // 파일명이 String으로 리턴
        // System.out.println(uploadPath);
        // 이미지 파일은 경로에 저장  - 절대 경로  @Value로 위에 선언한 application.properties 내부의 절대경로 가져와서 사용
        // String filePath = uploadPath + originalFileName;

        // 이미지 파일은 경로에 저장  - 상대 경로
//        String uploadDir = servletContext.getRealPath("/upload/");
//        String filePath = uploadDir + originalFileName;

//        try {
//            imageFile.transferTo(new File(filePath));
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 이미지파일의 이름을 받아서 imagePath 라는 필드에 담아서 board Entity로 보냄
        // 저장은 전체 경로에 하되 db에는 /upload/폴더 이하의 경로만 저장
        dto.setImagePath(originalFileName);

        log.info("controller--" + dto.getUser_Id() + "가 글번호 "+ dto.getNo() + " 수정"); // userId가 없음
        service.registBoard(dto);
        return "redirect:/board/detail?no=" +dto.getNo();
    }

}
