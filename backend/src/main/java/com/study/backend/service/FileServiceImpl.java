package com.study.backend.service;

import com.study.backend.model.FileDTO;
import com.study.backend.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/*
todo : file getList & getFileNum  sql 결합하기
 */
@RequiredArgsConstructor
@Service("FileService")
public class FileServiceImpl implements FileService {

    @Value("spring.servlet.multipart.location")
    private String filePathLocal;

    private final FileMapper fileMapper;

    /**
     * 파일 추가
     *
     * @param fileDTO
     */
    @Override
    public void updateFileList(FileDTO fileDTO, Part filePart) throws IOException {
//        public void fileUpdateList(int boardNum, Part filePart){
        if (filePart != null && filePart.getSize() > 0) {

            String originalFileName = filePart.getSubmittedFileName();
            String saveFileName = filePart.getSubmittedFileName();

            File fileSaveDir = new File(filePathLocal);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }

            filePart.write(filePathLocal + File.separator + originalFileName);
            String filePathDB = filePathLocal + File.separator + originalFileName;

            fileDTO.setBoardNum(fileDTO.getBoardNum());
            fileDTO.setSaveFileName(saveFileName);
            fileDTO.setOriginalFileName(originalFileName);
            fileDTO.setFilePath(filePathDB);

            fileMapper.updateFileList(fileDTO);

        }
    }

    /**
     * 파일 전체 블러오기
     *
     * @param boardNum
     * @return
     */
    @Override
    public List<FileDTO> getFileList(int boardNum) {
        return fileMapper.getFileList(boardNum);
    }

    /**
     * 게시판 글 삭제시 파일 전체 삭제
     *
     * @param boardNum
     */
    @Override
    public void deleteAllFile(int boardNum) {
        fileMapper.deleteAllFile(boardNum);
    }

    /**
     * 게시판 글 수정시 파일 삭제
     *
     * @param boardNum
     * @param fileNum
     */
    @Override
    public void deleteSelectFile(int boardNum, int fileNum) {
        fileMapper.deleteSelectFile(boardNum, fileNum);
    }

    /**
     * 파일 다운로드시 파일 불러오기
     *
     * @param boardNum
     * @param fileNum
     * @return
     */
    @Override
    public FileDTO getFileNum(int boardNum, int fileNum) {
        return fileMapper.getFileNum(boardNum, fileNum);
    }

    @Override
    public List<FileDTO> downloadFile(int boardNum, int fileNum, HttpServletResponse response) throws IOException {

        FileDTO fileDTO = getFileNum(boardNum, fileNum);
        String filePath = fileDTO.getFilePath();

        if (!filePath.startsWith(filePathLocal)) {
            throw new SecurityException("저장된 위치가 아닙니다.");
        }

        File fileDownload = new File(filePath);

        if (!fileDownload.exists() || !fileDownload.isFile()) {
            throw new FileNotFoundException("파일이 존재하지 않습니다." + filePath);
        }

        String fileName = fileDownload.getName();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        Files.copy(fileDownload.toPath(), response.getOutputStream());
        return null;
    }
}
