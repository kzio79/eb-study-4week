package com.study.backend.service;

import com.study.backend.model.BoardDTO;
import com.study.backend.mapper.BoardMapper;
import com.study.backend.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service("boardService")
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;
    private final FileService fileService;

    /**
     * 목록 카운팅
     *
     * @return
     * @throws Exception
     */

    @Override
    public Integer getBoardCount
    (String searchId, int categoryNum, Timestamp startDate, Timestamp endDate) {
        return boardMapper.getBoardCount(searchId, categoryNum, startDate, endDate);
    }

    /**
     * 전체 리스트, 검색
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<BoardDTO> getBoardList(String searchId, int categoryNum, Timestamp startDate, Timestamp endDate, int pageNum, int pageSize) {

        int startPage = (pageNum - 1) * pageSize;

        List<BoardDTO> boardList = boardMapper.getBoardList(searchId, categoryNum, startDate, endDate, startPage, pageSize);

        for (BoardDTO boardDTO : boardList) {
            int fileCount = fileMapper.fileCount(boardDTO.getBoardNum());
            boardDTO.setFileCount(fileCount);
        }
        return boardList;
    }

    /**
     * 글보기
     *
     * @param boardNum
     */
    @Override
    public BoardDTO viewBoardContent(int boardNum) {
        return boardMapper.viewBoardContent(boardNum);
    }

    /**
     * 글작성
     *
     * @param boardDTO
     * @return
     * @throws Exception
     */
    @Override
    public int writeBoardContent(BoardDTO boardDTO) {
        if (boardDTO == null || boardDTO.getWriter().isBlank() || boardDTO.getPw().isBlank() || boardDTO.getTitle().isBlank() || boardDTO.getContent().isBlank()) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return boardMapper.writeBoardContent(boardDTO);
    }

    /**
     * 글수정
     *
     * @param
     * @return
     * @throws Exception
     */
    @Override
    public int modifyBoardContent(BoardDTO boardDTO) {

        if (boardDTO == null || boardDTO.getWriter().isBlank() || boardDTO.getPw().isBlank() || boardDTO.getTitle().isBlank() || boardDTO.getContent().isBlank()) {
            throw new IllegalArgumentException("Invalid arguments");
        }
        return boardMapper.modifyBoardContent(boardDTO);
    }

    /**
     * 글삭제
     *
     * @param boardNum
     * @throws Exception
     */
    @Override
    public void deleteBoardContent(int boardNum) {
        boardMapper.deleteBoardContent(boardNum);
    }

    /**
     * 조회수
     *
     * @param boardNum
     * @throws Exception
     */
    @Override
    public void updateHit(int boardNum) {
        boardMapper.updateHit(boardNum);
    }

    /**
     * categoryNum 블러오기
     *
     * @param categoryNum
     * @return
     */
    @Override
    public boolean isCategoryNum(int categoryNum) {
        return boardMapper.getCategoryNum(categoryNum) != null;
    }
}
