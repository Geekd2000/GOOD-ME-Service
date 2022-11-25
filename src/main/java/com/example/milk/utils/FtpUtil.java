package com.example.milk.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * FTP客户端工具类
 * @author hanbinbin
 */
public class FtpUtil {
    // ftp服务器地址
    private String hostname ;
    // ftp服务器端口号
    private Integer port;
    // ftp登录账号
    private String username;
    // ftp登录密码
    private String password;

    private FTPClient ftpClient;


    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    public FtpUtil(String hostname, Integer port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.ftpClient = null;
    }


    /**
     * 初始化ftp服务器
     */
    public void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            logger.info("connecting...ftp服务器:" + this.hostname + ":" + this.port);
            ftpClient.connect(hostname, port); // 连接ftp服务器
            ftpClient.login(username, password); // 登录ftp服务器
            //设置文件编码格式
            ftpClient.setControlEncoding("UTF-8");

            ftpClient.enterLocalPassiveMode();// 被动模式
            System.out.println("PASV 被动模式");
//            ftpClient.enterLocalActiveMode();// 主动模式
//            System.out.println("PORT 主动模式");

            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);//设置传输方式为流方式

            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("connect failed...ftp服务器:" + this.hostname + ":" + this.port);
            }

            logger.info("connect successfu...ftp服务器:" + this.hostname + ":" + this.port);
        } catch (MalformedURLException e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void checkFtp(){
        try {
            logger.info("检测是否成功登录服务器ftp服务器:" + this.hostname + ":" + this.port);
            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                logger.info("connect failed...ftp服务器:" + this.hostname + ":" + this.port);
                logger.info("重新连接...ftp服务器:" + this.hostname + ":" + this.port);
                initFtpClient();
            }
            logger.info("connect successfu...ftp服务器:" + this.hostname + ":" + this.port);
        }catch (Exception e){
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 关闭ftp服务器
     */
    public void closeFtpClient(){
        try {
            ftpClient.logout();

        } catch (IOException e) {
//            e.printStackTrace();
        }finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
//                e.printStackTrace();
//                throw new RuntimeException(e.getMessage());
            }
        }

    }

    /**
     * 上传文件
     *
     * @param pathname
     *            ftp服务保存地址
     * @param fileName
     *            上传到ftp的文件名
     * @param originfilename
     *            待上传文件的名称（绝对地址） *
     * @return
     */
    public boolean uploadFile(String pathname, String fileName, String originfilename) {
        InputStream inputStream = null;
        try {
            logger.info("开始上传文件");
            inputStream = new FileInputStream(new File(originfilename));
            //initFtpClient();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            CreateDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            // 每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据
            ftpClient.enterLocalPassiveMode();
            // 观察是否真的上传成功
            boolean storeFlag = ftpClient.storeFile(fileName, inputStream);
            logger.info("storeFlag==" + storeFlag);
            inputStream.close();
            //ftpClient.logout();
            logger.info("上传文件成功");
            return true;
        } catch (Exception e) {
            logger.info("上传文件失败");
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
            return false;
        } finally {
//            if (ftpClient.isConnected()) {
//                try {
//                    ftpClient.disconnect();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e.getMessage());
                }
            }
        }

    }

    /**
     * 上传文件
     *
     * @param pathname
     *            ftp服务保存地址
     * @param fileName
     *            上传到ftp的文件名
     * @param inputStream
     *            输入文件流
     * @return
     */
    public boolean uploadFile(String pathname, String fileName, InputStream inputStream) {
        try {
            logger.info("开始上传文件");
            System.out.println("文件size:"+String.valueOf(inputStream.available()/1000)+"k");
            //initFtpClient();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            // 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
            CreateDirecroty(pathname);
            ftpClient.makeDirectory(pathname);
            ftpClient.changeWorkingDirectory(pathname);
            // 每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据
            //ftpClient.enterLocalPassiveMode();



            // 观察是否真的上传成功
            boolean storeFlag = ftpClient.storeFile(fileName, inputStream);
            logger.info("storeFlag==" + storeFlag);
            inputStream.close();
            //ftpClient.logout();
            logger.info("上传文件成功");
            return true;
        } catch (Exception e) {
            logger.info("上传文件失败");
            e.printStackTrace();
            return false;
        } finally {
//            if (ftpClient.isConnected()) {
//                try {
//                    ftpClient.disconnect();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }
    }

    // 改变目录路径
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
            if (flag) {
                logger.info("进入文件夹" + directory + " 成功！");

            } else {
                logger.info("进入文件夹" + directory + " 失败！开始创建文件夹");
            }
        } catch (IOException ioe) {
//            ioe.printStackTrace();
            throw new RuntimeException(ioe.getMessage());
        }
        return flag;
    }

    // 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    public boolean CreateDirecroty(String remote) throws IOException {
        boolean success = true;
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        logger.info("创建目录[" + subDirectory + "]失败");
                        changeWorkingDirectory(subDirectory);
//                        throw new RuntimeException("创建目录[" + subDirectory + "]失败");
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    // 判断ftp服务器文件是否存在
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    // 创建目录
    public boolean makeDirectory(String dir) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
            if (flag) {
                logger.info("创建文件夹" + dir + " 成功！");

            } else {
                logger.info("创建文件夹" + dir + " 失败！");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());

        }
        return flag;
    }

    /**
     * * 下载文件 *
     *
     * @param pathname
     *            FTP服务器文件目录 *
     * @param filename
     *            文件名称 *
     * @param localpath
     *            下载后的文件路径 *
     * @return
     */
    public boolean downloadFile(String pathname, String filename, String localpath) {
        boolean flag = false;
        OutputStream os = null;
        try {
            logger.info("开始下载文件");
            initFtpClient();
            // 切换FTP目录
            boolean changeFlag = ftpClient.changeWorkingDirectory(pathname);
            logger.info("changeFlag==" + changeFlag);

            ftpClient.enterLocalPassiveMode();
            ftpClient.setRemoteVerificationEnabled(false);
            // 查看有哪些文件夹 以确定切换的ftp路径正确
            String[] a = ftpClient.listNames();
            System.err.println(a[0]);

            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    File localFile = new File(localpath + "/" + file.getName());
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                }
            }
            ftpClient.logout();
            flag = true;
            logger.info("下载文件成功");
        } catch (Exception e) {
            logger.info("下载文件失败");
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e.getMessage());
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e.getMessage());

                }
            }
        }
        return flag;
    }

    /**
     * * 下载文件 *
     * 返回： InputStream
     * @param remote
     * 远端FTP服务器文件路径 *
     *
     * @return
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     * ftp目录下单文件处理（下载文件返回文件流，业务层直接进行流的处理）
     * @param pathName
     * @param fileName
     * @param remote
     * @return
     * @throws IOException
     */
    public List downloadFile2(String pathName, String fileName, String remote) throws IOException {
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        InputStream inputStream = null;
        byte[]  bytes = null;
        List result = new ArrayList();
        try {
            logger.info("开始下载文件");
            initFtpClient();

            // 切换FTP目录
            boolean changeFlag = ftpClient.changeWorkingDirectory(pathName+"/");
            logger.info("changeFlag==" + changeFlag);

            //判断当前有没有新的excel文件上传并创建了（yyyymmdd形式）目录
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -1);
            String yesterday = sdf.format(c.getTime());

            FTPFile[] ftpFiles1 = ftpClient.listDirectories();
            for (FTPFile file: ftpFiles1) {
                if (yesterday.equals(file.getName())){
                    boolean changeFlag1 = ftpClient.changeWorkingDirectory(yesterday+"/");
                    logger.info("changeFlag==" + changeFlag1);

                    ftpClient.enterLocalPassiveMode();
                    ftpClient.setRemoteVerificationEnabled(false);
                    // 查看有哪些文件夹 以确定切换的ftp路径正确

                    FTPFile[] ftpFiles = ftpClient.listFiles();
                    for (int i = 0; i < ftpFiles.length; i++) {
                        if (ftpFiles[i].getName().startsWith(fileName)) {
                            logger.error("begin to downLoad the file from FTP !");
                            inputStream = ftpClient.retrieveFileStream(ftpFiles[i].getName());
                        }
                        logger.error("add the file to list and the index is :"+ i );
                        logger.error("add the file to list and the fileName is :"+ ftpFiles[i].getName() );
                        result.add(i, inputStream);
                    }

                    ftpClient.logout();
                    if (null != inputStream){
                        logger.info("下载文件成功");
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            logger.info("下载文件失败");
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    logger.error("downLoad the file is fail :"+e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * ftp目录下多文件处理（下载文件返回文件流，业务层直接进行流的处理）
     * @param pathName
     * @param fileName
     * @param remote
     * @return
     * @throws IOException
     */
    public List downloadFile1(String pathName, String fileName, String remote) throws IOException {
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        InputStream inputStream = null;
        byte[]  bytes = null;
        List result = new ArrayList();
        try {
            logger.info("开始下载文件");
            initFtpClient();

            // 切换FTP目录
            boolean changeFlag = ftpClient.changeWorkingDirectory(pathName+"/");
            logger.info("changeFlag==" + changeFlag);

            //判断当前有没有新的excel文件上传并创建了（yyyymmdd形式）目录
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -1);
            String yesterday = sdf.format(c.getTime());

            FTPFile[] ftpFiles1 = ftpClient.listDirectories();
            for (FTPFile file: ftpFiles1) {
                if (yesterday.equals(file.getName())){
                    boolean changeFlag1 = ftpClient.changeWorkingDirectory(yesterday+"/");
                    logger.info("changeFlag==" + changeFlag1);

                    ftpClient.enterLocalPassiveMode();
                    ftpClient.setRemoteVerificationEnabled(false);
                    // 查看有哪些文件夹 以确定切换的ftp路径正确

                    FTPFile[] ftpFiles = ftpClient.listFiles();
                    for (int i = 0; i < ftpFiles.length; i++) {
                        if (ftpFiles[i].getName().startsWith(fileName)) {
                            inputStream = ftpClient.retrieveFileStream(ftpFiles[i].getName());
                            bytes = inputToBytes(inputStream);
                            logger.error("===1===the length of byte file is"+ bytes.length);
                        }
                        if (null != inputStream){
                            inputStream.close();
                            logger.info("下载文件成功");
                        }
                        ftpClient.completePendingCommand();
                        logger.error("add the file to list and the index is :"+ i );
                        logger.error("add the file to list and the fileName is :"+ ftpFiles[i].getName() );
                        result.add(i, bytes);
                    }

                    ftpClient.logout();
                }
            }
        } catch (Exception e) {
            logger.info("下载文件失败");
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    logger.error("downLoad the file is fail :"+e.getMessage());
                }
            }
        }
        return result;
    }

    public byte[] inputToBytes(InputStream is){
        byte[] buffer=new byte[1024];
        int len=0;
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        try {
            while((len=is.read(buffer))!=-1){
                bos.write(buffer,0,len);
            }
            bos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.error("文件流转byte数组失败！");
        }
        return bos.toByteArray();
    }

    /**
     * * 删除文件 *
     *
     * @param pathname
     *            FTP服务器保存目录 *
     * @param filename
     *            要删除的文件名称 *
     * @return
     */
    public boolean deleteFile(String pathname, String filename) {
        boolean flag = false;
        try {
            logger.info("开始删除文件");
            initFtpClient();
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            ftpClient.dele(filename);
            ftpClient.logout();
            flag = true;
            logger.info("删除文件成功");
        } catch (Exception e) {
            logger.info("删除文件失败");
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());

        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e.getMessage());

                }
            }
        }
        return flag;
    }

    public boolean downloadFile(String pathname, String filename, File localFile) {
        boolean flag = false;
        OutputStream os = null;
        try {
            logger.info("开始下载文件");
            initFtpClient();
            // 切换FTP目录
            boolean changeFlag = ftpClient.changeWorkingDirectory(pathname);
            logger.info("changeFlag==" + changeFlag);

            ftpClient.enterLocalPassiveMode();
            ftpClient.setRemoteVerificationEnabled(false);
            // 查看有哪些文件夹 以确定切换的ftp路径正确
            String[] a = ftpClient.listNames();
            System.err.println(a[0]);

            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    os.close();
                }
            }
            ftpClient.logout();
            flag = true;
            logger.info("下载文件成功");
        } catch (Exception e) {
            logger.info("下载文件失败");
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e.getMessage());
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e.getMessage());

                }
            }
        }
        return flag;
    }

    public InputStream downloadFileStream(String pathname, String filename) {
        boolean flag = false;
        InputStream inputStream = null;
        try {
            logger.info("开始下载文件");
            logger.info("文件路径为{}和文件名为{}",pathname,filename);
            initFtpClient();
            // 切换FTP目录
            boolean changeFlag = ftpClient.changeWorkingDirectory(pathname);
            logger.info("changeFlag==" + changeFlag);

            ftpClient.enterLocalPassiveMode();
            ftpClient.setRemoteVerificationEnabled(false);

            FTPFile[] ftpFiles = ftpClient.listFiles();
            logger.info("files is :"+ JSON.toJSONString(ftpFiles));
            logger.info(" the file size of path is"+ftpFiles.length);
            for (FTPFile file : ftpFiles) {
                logger.info("文件名为："+file.getName());
                if (filename.equalsIgnoreCase(file.getName())) {
                    logger.error("begin to downLoad the file from FTP !");
                    return ftpClient.retrieveFileStream(file.getName());
                }
            }
        } catch (Exception e) {
            logger.info("下载文件失败");
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public boolean createOrChangePath(String filePath,String fileName) throws Exception{
        if (filePath != null && filePath.length() > 0) {
            CreateDirecroty(filePath);
            ftpClient.makeDirectory(filePath);
            boolean b = ftpClient.changeWorkingDirectory(filePath);
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (int i = 0; i < ftpFiles.length; i++)
                if (ftpFiles[i].getName().equals(fileName + ".txt")) {
                    b = deleteFile1(filePath, fileName);
                }
            if (b){
                return b;
            }
        }
        return false;
    }

    public boolean uploadFileForSession(String fileName,String content,String fileType) throws Exception {
        OutputStreamWriter out = null;
        BufferedWriter pw = null;
        try {
                out = new OutputStreamWriter(ftpClient
                        .appendFileStream(fileName + fileType));
                pw = new BufferedWriter(out);
                pw.write(content);
                pw.flush();
                pw.close();
                ftpClient.getReply();
                return true;
        } catch (Exception e) {
            logger.error("文件上传异常："+e.getMessage());
        }finally {
            if (null != out) {
                out.close();
            }
            if (null != pw) {
                pw.close();
            }
        }
        return false;
    }

    public boolean deleteFile1(String filePath,String fileName) {
        try {
            boolean b = ftpClient.deleteFile(filePath + "/" + fileName + ".txt");
            if (b){
                return b;
            }
        } catch (Exception e) {
            logger.error("删除"+filePath+"下"+fileName+"文件失败！");
        }
        return false;
    }

    /**
     * 在文件末尾追加内容
     * @param content
     * @return
     */
    public boolean appendContent(OutputStream outputStream,byte[] content){
        try {
            if (outputStream != null){
                outputStream.write(content);
            }else {
                logger.error("outputStream为null!");
            }
            return true;
        } catch (IOException e) {
            logger.error("文件追加内容异常！");
            return false;
        }
    }

    /**
     * 获取文件输出流
     * @param filePath 文件路径拼接文件名
     * @return
     */
    public OutputStream getOutputStream(String filePath){
        try {
            return ftpClient.appendFileStream(filePath);
        } catch (IOException e) {
            logger.error("获取文件输出流异常！");
        }
        return null;
    }

    /**
     * 关闭输出流
     * @param outputStream
     */
    public void closeStream(OutputStream outputStream){
        if(outputStream!=null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                logger.error("输出流关闭异常！");
            }
        }
    }
}

