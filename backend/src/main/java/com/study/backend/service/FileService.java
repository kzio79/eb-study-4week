package com.study.backend.service;

import com.study.backend.model.FileDTO;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;


public interface FileService {

    /**
     * file upload
     *
     * @throws Exception
     */
    public void updateFileList(FileDTO fileDTO, Part filePart) throws IOException;

    /**
     * file getLIst
     *
     * @throws Exception
     */
    public List<FileDTO> getFileList(int boardNum);

    /**
     * file all delete
     *
     * @param boardNum
     */
    public void deleteAllFile(int boardNum);

    /**
     * file all delete
     *
     * @param boardNum
     */
    public void deleteSelectFile(int boardNum, int fileNum);

    /**
     * file getNum
     *
     * @param boardNum
     * @return
     */
    public FileDTO getFileNum(int boardNum, int fileNum);

    /**
     * file download
     *
     * @param boardNum
     * @param fileNum
     */
    public List<FileDTO> downloadFile(int boardNum, int fileNum, HttpServletResponse response) throws IOException;
}
