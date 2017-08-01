package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by bu_dong on 2017/7/27.
 */

@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名adb.jpg
        String fileExtensionName = fileName.substring(fileName.indexOf(".") + 1);
        String uploadFileName = "";
        uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件 文件名:{},上传路径是{},新文件名:{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        //上传到path路径文件
        File targetFile = new File(path, uploadFileName);
        try {
            file.transferTo(targetFile);

            // TODO: 2017/7/27 将targetfile上传到ftp服务器下
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // TODO: 2017/7/27 上传后 删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件失败", e);
            return null;
        }

        return targetFile.getName();
    }
}
