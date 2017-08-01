package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by bu_dong on 2017/7/27.
 */
public class FTPUtil {
    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String FTP_IP = PropertiesUtil.getProperty("ftp.server.ip");
    private static String FTP_USER = PropertiesUtil.getProperty("ftp.user");
    private static String FTP_PASSWORD = PropertiesUtil.getProperty("ftp.pass");

    private  int port;
    private  String ftpIp;
    private  String user;
    private  String password;

    private static FTPClient ftpClient;

    public FTPUtil(String ftpIP, int port, String user, String password) {
        this.ftpIp = ftpIP;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public static boolean uploadFile(List<File> fileList) {
        FTPUtil ftpUtil = new FTPUtil(FTP_IP, 21, FTP_USER, FTP_PASSWORD);
        logger.info("开始连接ftp服务器");
        boolean result = false;
        try {
            result =ftpUtil.uploadFile("img", fileList);
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        logger.info("开始连接ftp服务器结束上传， 上传结果:{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        FileInputStream fis = null;
        boolean upload = false;
        if (connectServer(this.ftpIp, this.port, this.user, this.password)) {
            try {
                this.ftpClient.changeWorkingDirectory(remotePath);
                this.ftpClient.setBufferSize(1024);
                this.ftpClient.setControlEncoding("UTF-8");
                this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                this.ftpClient.enterLocalPassiveMode();
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    this.ftpClient.appendFile(remotePath, fis);
                }
                upload = true;
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("上传文件异常", e);
                upload = false;
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return upload;
    }

    private static boolean connectServer(String ip, int port, String user, String password) {
        boolean isConnect = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            isConnect = ftpClient.login(user, password);

        } catch (IOException e) {
            logger.error("FTP服务器连接失败", e);
        }
        return isConnect;
    }
}
