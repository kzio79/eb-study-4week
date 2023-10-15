package com.study.backend.controller;

import com.study.backend.model.BoardDTO;
import com.study.backend.model.FileDTO;
import com.study.backend.model.ReplyDTO;
import com.study.backend.service.FileService;
import com.study.backend.service.BoardService;
import com.study.backend.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final FileService fileService;

    /**
     * home접속시 index로 이동
     */
    @RequestMapping("/")
    public RedirectView home() {
        return new RedirectView("index");
    }

    /**
     * 리스트보기, 카운팅, 페이징, 검색
     * <p>
     * /api/vi : 웹서버와 was의 구분을 지을수 있게 수정
     */

    /*
    todo: /api/vi : 웹서버와 was의 구분을 지을수 있게 수정, 변수명
     */
    @GetMapping("/index")
//    @GetMapping("/api/v1/index")
    public ResponseEntity<Map<String, Object>> boardContentList(@ModelAttribute BoardDTO boardDTO) {

        int categoryNum = 0;
        Timestamp startDate = null;
        Timestamp endDate = null;

        if (boardDTO.getCategory() != null && !boardDTO.getCategory().isEmpty()) {
            categoryNum = Integer.parseInt(boardDTO.getCategory());
        }

        if (boardDTO.getStartDateStr() != null && !boardDTO.getStartDateStr().isEmpty()) {
            startDate = Timestamp.valueOf(boardDTO.getStartDateStr() + " 00:00:00");
        }

        if (boardDTO.getEndDateStr() != null && !boardDTO.getEndDateStr().isEmpty()) {
            endDate = Timestamp.valueOf(boardDTO.getEndDateStr() + " 23:59:59");
        }

        //boardList
        List<BoardDTO> boardList = boardService.getBoardList(boardDTO.getSearchId(), categoryNum, startDate, endDate, boardDTO.getPageNum(), boardDTO.getPageSize());

        //counting
        int totalCount = boardService.getBoardCount(boardDTO.getSearchId(), categoryNum, startDate, endDate);

        Map<String, Object> response = new HashMap<>();
        response.put("boardList", boardList);
        response.put("totalCount", totalCount);

        return ResponseEntity.ok().body(response);
    }

    /**
     * content보기, 파일보기, 댓긋보기
     *
     * @param boardNum
     * @return
     */

    /*
    todo : @GetMapping("/content/{seq}") 식으로 수정을 요함, 모든 api의 결과는 동일한 결과로 보여준다
    api result
     result code - enum을 사용
     result message
     */
    @GetMapping("/content")
    public ResponseEntity<?> viewBoardContent(@RequestParam("boardNum") int boardNum) {

        //contentView
        BoardDTO viewBoardContent = boardService.viewBoardContent(boardNum);
        boardService.updateHit(boardNum);

        //replyView
        List<ReplyDTO> viewReplyContent = replyService.getReplyList(boardNum);

        //fileView
        List<FileDTO> viewListFile = fileService.getFileList(boardNum);

        Map<String, Object> responseContent = new HashMap<>();
        responseContent.put("viewBoardContent", viewBoardContent);
        responseContent.put("viewReplyContent", viewReplyContent);
        responseContent.put("viewListFile", viewListFile);

        return ResponseEntity.ok().body(responseContent);
    }

    /**
     * 댓글 작성
     *
     * @param boardNum
     * @param replyContent
     * @return
     */
    @PostMapping("/content")
    public ResponseEntity<?> writeReplyContent(@RequestParam("boardNum") int boardNum,
                                               @RequestParam("replyContent") String replyContent) {

        Map<String, Object> responseContent = new HashMap<>();

        if (replyContent != null && !replyContent.isEmpty()) {
            replyService.writeReply(boardNum, replyContent);
        }

        responseContent.put("redirectUrl", "/content?boardNum=" + boardNum);
        return ResponseEntity.ok().body(responseContent);
    }

    /**
     * 글작성, 파일등록
     *
     * @param
     * @param
     * @return
     */

//    @GetMapping("/write")
//    public ResponseEntity<?> writeBoardContent() {
//        return ResponseEntity.ok().build();
//    }

    /*
    todo : null체크 dto에서 할수 있는 부분들을 할수 있음. file 업로드 수정
     */
    @PostMapping("/write")
    public ResponseEntity<?> writeBoardContent(@ModelAttribute BoardDTO boardDTO,
                                               HttpServletRequest request,
                                               MultipartFile file) {

        Map<String, Object> responseWrite = new HashMap<>();

        if (boardService.isCategoryNum(boardDTO.getCategoryNum())) {

            responseWrite.put("error", "categoryNum is null");
            return ResponseEntity.badRequest().body(responseWrite);
        }
        int writeContent = boardService.writeBoardContent(boardDTO);

        boardDTO.setCategoryNum(boardDTO.getCategoryNum());
        boardDTO.setWriter(boardDTO.getWriter());
        boardDTO.setPw(boardDTO.getPw());
        boardDTO.setTitle(boardDTO.getTitle());
        boardDTO.setContent(boardDTO.getContent());

        //file save
        if (file != null && !file.isEmpty()) {

            String filePath = "spring.servlet.multipart.location";

//            fileService.updateFileList(boardDTO.getBoardNum(), request.getPart("file"));
//            fileService.updateFileList(boardDTO.getBoardNum(), filePath, request.getPart("file1"));
//            fileService.updateFileList(boardDTO.getBoardNum(), filePath, request.getPart("file2"));

        }

        responseWrite.put("writeContent", writeContent);

        return ResponseEntity.ok().body(responseWrite);
    }

    /**
     * 글수정, 파일등록
     *
     * @param boardNum
     * @param
     * @return
     */
    @GetMapping("/modify")
    public ResponseEntity<?> modifyBoardContent(@RequestParam("boardNum") int boardNum) {
        Map<String, Object> responseModify = new HashMap<>();
        //boardList
        BoardDTO modifyBoardContent = boardService.viewBoardContent(boardNum);

        //fileView
        List<FileDTO> viewListFile = fileService.getFileList(boardNum);

        responseModify.put("modifyBoardContent", modifyBoardContent);
        responseModify.put("viewListFile", viewListFile);

        return ResponseEntity.ok().body(responseModify);
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyBoardContent(@ModelAttribute BoardDTO boardDTO,
                                                HttpServletRequest request,
                                                MultipartFile file) {

        Map<String, Object> responseModify = new HashMap<>();

        if (boardDTO == null) {
            responseModify.put("error", "No data");
            return ResponseEntity.badRequest().body(responseModify);
        }

        int modifyContent = boardService.modifyBoardContent(boardDTO);

        //file save
        if (file != null && !file.isEmpty()) {

//                    fileService.updateFileList(boardDTO.getBoardNum(), filePath, request.getPart("file"));
//                    fileService.updateFileList(boardDTO.getBoardNum(), filePath, request.getPart("file1"));
//                    fileService.updateFileList(boardDTO.getBoardNum(), filePath, request.getPart("file2"));

        }

        responseModify.put("modifyContent", modifyContent);

        return ResponseEntity.ok().body(responseModify);
    }

    /**
     * 글삭제
     *
     * @param boardNum
     * @return
     */
    @GetMapping("/delete")
    public ResponseEntity<?> deleteBoardContent(@RequestParam("boardNum") int boardNum) {
        Map<String, Object> responseDelete = new HashMap<>();

        BoardDTO deleteContent = boardService.viewBoardContent(boardNum);

        responseDelete.put("deleteContent", deleteContent);
        return ResponseEntity.ok().body(responseDelete);
    }

    /*
    todo : pw를 확인시 db에서 가져온 값과 확인 sql구문중 pw를 가져올 수 있는 부분 생성 후 비교
     */
    @PostMapping("/delete")
    public ResponseEntity<?> deleteBoardContent(@RequestParam("boardNum") int boardNum,
                                                @RequestParam("pw") String pw,
                                                BoardDTO boardDTO) {
        Map<String, Object> responseDelete = new HashMap<>();


        if (pw != null && !pw.isEmpty()) {
            if (boardDTO.getPw().equals(pw)) {
                replyService.deleteReply(boardNum);
                fileService.deleteAllFile(boardNum);
                boardService.deleteBoardContent(boardNum);
            } else {

                responseDelete.put("redirectUrl", "/delete?boardNum=" + boardNum);
                return ResponseEntity.badRequest().body(responseDelete);
            }
        } else {

            responseDelete.put("redirectUrl", "/delete?boardNum=" + boardNum);
            return ResponseEntity.badRequest().body(responseDelete);
        }

        responseDelete.put("redirectUrl", "/index");
        return ResponseEntity.ok().body(responseDelete);
    }

    /**
     * file download
     *
     * @param boardNum
     * @param response
     * @throws FileNotFoundException
     */
    @GetMapping("/download")
    public void fileDownload(@RequestParam("boardNum") int boardNum,
                             @RequestParam("fileNum") int fileNum,
                             HttpServletResponse response) throws IOException {

        fileService.downloadFile(boardNum, fileNum, (HttpServletResponse) response.getOutputStream());

//        try {
//            FileDTO fileDTO = fileService.getFileNum(boardNum, fileNum);
//            String filePath = fileDTO.getFilePath();
//
//            if (!filePath.startsWith("D:\\tmp")) {
//                throw new SecurityException("저장된 위치가 아닙니다.");
//            }
//
//            File fileDownload = new File(filePath);
//
//            if (!fileDownload.exists() || !fileDownload.isFile()) {
//                throw new FileNotFoundException("파일이 존재하지 않습니다." + filePath);
//            }
//
//            String fileName = fileDownload.getName();
//
//            response.setContentType("application/octet-stream");
//            response.setHeader("Content-Disposition",
//                    "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
//
//            Files.copy(fileDownload.toPath(), response.getOutputStream());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * file select delete
     *
     * @param boardNum
     * @param fileNum
     * @return
     */

    @GetMapping("/deleteFile")
    public String deleteSelectFile(@RequestParam("boardNum") int boardNum,
                                   @RequestParam("fileNum") int fileNum) {

        fileService.deleteSelectFile(boardNum, fileNum);

        return "redirect:/modify?boardNum=" + boardNum;
    }
}
