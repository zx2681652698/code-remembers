package com.ab.dh.apporder.util;

import it.sauronsoftware.jave.DefaultFFMPEGLocator;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基于FFmpeg内核来编解码音视频信息；
 * 使用jave-1.0.2.jar自带的FFmpeg.exe程序来处理音视频，这样无需在本地安装部署FFmpeg应用程序，且自适应linux和windows等系统；
 * 可直接在本程序中执行FFmpeg命令，实现对音视频的各种处理；
 *
 * Author: dreamer-1
 *
 */
public class FFmpegUtil {

    protected static Logger logger = Logger.getLogger(FFmpegUtil.class);

    private static String FFMPEG_PATH;

    private static String systemName = System.getProperty("os.name").toLowerCase();

    /**
     * 初始化时利用反射获取jave-1.0.1.jar中FFmpeg.exe的路径
     * 利用jave-1.0.1.jar来避免本地安装FFmpeg.exe
     */
    static {
        DefaultFFMPEGLocator locator = new DefaultFFMPEGLocator();
        try {
            Method method = locator.getClass().getDeclaredMethod("getFFMPEGExecutablePath");
            method.setAccessible(true);
            FFMPEG_PATH = (String) method.invoke(locator);
            method.setAccessible(false);
            logger.info("--- 获取FFmpeg可执行路径成功 --- 路径信息为：" + FFMPEG_PATH);
        } catch (Exception e) {
            logger.error("--- 获取FFmpeg可执行路径失败！ --- 错误信息： " + e.getMessage());
        }

    }

    /**
     * 获取FFmpeg程序的路径（windows和linux环境下路径不一样）
     *
     * @return
     */
    public static String getFFmpegPath() {
        return FFMPEG_PATH;
    }

	/**
	 * 
	 * @param faceVideo 上传的视频
	 * @return
	 */
    public static MultipartFile FFmpeg(MultipartFile faceVideo, HttpServletRequest request){
        //byte[] byteVideo = null;
        logger.info("FFMPEG_PATH="+FFMPEG_PATH);
        //ffmpeg压缩工具路径 "D:\\testFile\\ffmpeg.exe"
        String ffmpegPath = systemName.contains("windows") ? new File("order-app-front/tools/windows/ffmpeg.exe").getAbsolutePath() : new File("/data/server/ffmpeg").getAbsolutePath();
        //String ffmpegPath = FFMPEG_PATH;
        logger.info("ffmpegPath="+ffmpegPath);
        MultipartFile file = null;
        File oldFile = null, newFile = null;
        String localPathPrefix = request.getSession().getServletContext().getRealPath("/") +"data/tempDir/";
        File localFile = new File(localPathPrefix);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        try {
            String suffix = ".mp4";
            String fileName = faceVideo.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            if(index > 0) {
                suffix = fileName.substring(index);
            }
            oldFile = File.createTempFile(localPathPrefix+"oldVideo", suffix);
            faceVideo.transferTo(oldFile);

            if (!oldFile.isFile()) {
                return file;
            }

            String inputPath = oldFile.getPath();
            newFile = File.createTempFile(localPathPrefix +"newVideo", suffix);
            String outputPath = newFile.getPath();
            int type = checkContentType(inputPath);
            List command = getFFmpegCommand(type, ffmpegPath, inputPath, outputPath);
            logger.info("command="+ Arrays.toString(command.toArray()).replace(",", ""));
            if (command.size() > 0) {
                boolean result = process(command);
                if(result) {
                    //byteVideo = getBytes(outputPath);
                    file = PdfUtil.getMulFileByPath(outputPath, "textField");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(oldFile != null) {
                oldFile.deleteOnExit();
            }
            if(newFile != null) {
                newFile.deleteOnExit();
            }
        }
        return file;
    }

    private static int checkContentType(String inputPath) {
        String type = inputPath.substring(inputPath.lastIndexOf(".") + 1, inputPath.length()).toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
        if (type.equals("avi")) {
            return 1;
        } else if (type.equals("mpg")) {
            return 1;
        } else if (type.equals("wmv")) {
            return 1;
        } else if (type.equals("3gp")) {
            return 1;
        } else if (type.equals("mov")) {
            return 1;
        } else if (type.equals("mp4")) {
            return 1;
        } else if (type.equals("mkv")) {
            return 1;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("flv")) {
            return 0;
        } else if (type.equals("rm")) {
            return 0;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    private static boolean process(List<String> command) throws Exception {
        try {
            Process videoProcess = new ProcessBuilder().command(command).start();
            /*if (systemName.contains("windows")) {
                videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
            }else {
                Runtime rt = Runtime.getRuntime();
                videoProcess = rt.exec(command.toString());
            }*/
            new PrintStream(videoProcess.getErrorStream().toString()).println();
            new PrintStream(videoProcess.getInputStream().toString()).println();
            int exitCode = videoProcess.waitFor();
            if (exitCode == 1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    private static List getFFmpegCommand(int type, String ffmpegPath, String oldFilePath, String outputPath) throws Exception {
        List command = new ArrayList();
        if (type == 1) {
            command.add(ffmpegPath);
            command.add("-loglevel");
            command.add("quiet");
            command.add("-y");
            command.add("-i");
            command.add(oldFilePath);
            command.add("-c:v");
            command.add("libx264");
            command.add("-x264opts");
            command.add("force-cfr=1");
            command.add("-c:a");
            command.add("mp2");
            command.add("-b:a");
            command.add("256k");
            command.add("-vsync");
            command.add("cfr");
            command.add("-f");
            command.add("mpegts");
            command.add("-r");
            command.add("15");
            command.add("-b:v");
            command.add("500k");
            command.add(outputPath);
        } else if (type == 0) {
            command.add(ffmpegPath);
            command.add("-y");
            command.add("-i");
            command.add(oldFilePath);
            command.add("-c:v");
            command.add("libx264");
            command.add("-x264opts");
            command.add("force-cfr=1");
            command.add("-vsync");
            command.add("cfr");
            command.add("-vf");
            command.add("idet,yadif=deint=interlaced");
            command.add("-filter_complex");
            command.add("aresample=async=1000");
            command.add("-c:a");
            command.add("libmp3lame");
            command.add("-b:a");
            command.add("192k");
            command.add("-pix_fmt");
            command.add("yuv420p");
            command.add("-f");
            command.add("mpegts");
            command.add("-r");
            command.add("25");
            command.add("-b:v");
            command.add("1000k");
            command.add(outputPath);
        } else {
            throw new Exception("不支持当前上传的文件格式");
        }
        return command;
    }
    
    /**
     * 获得指定文件的byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(filePath);
            if(!file.exists()) {
                throw new FileNotFoundException("文件没有找到");
            }
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream((int)file.length());
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis != null) {
                    fis.close();
                }
                if(bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buffer;
    }
}
