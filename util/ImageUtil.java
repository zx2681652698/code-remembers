package com.ab.dh.apporder.util;

import com.ab.dh.api.constant.OrderReturnFlag;
import com.ab.dh.api.model.order.OrderResponse;
import org.apache.log4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import tk.mybatis.mapper.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ImageUtil {

    protected static Logger logger = Logger.getLogger(ImageUtil.class);

    /**
     * pdf转图片(转成一张图片)并上传
     * @param fileName：文件名称，带后缀
     * */
    public static String pdfToImgAndUpload(String fileName, String url, HttpServletRequest request, Map<String, String> imgParams) {
        String pdfImagePath = "";
        try {
            String localPath = request.getSession().getServletContext().getRealPath("/")+ "data/tempDir/";
            File outFile = new File(localPath);
            if (!outFile.exists()) {
                outFile.mkdirs();
            }
            String outPath = localPath + fileName.replace(".pdf", ".jpg");
            String pdfPath = localPath + fileName;
            //pdf转成一张图片
            PdfUtil.pdf2multiImage(pdfPath, outPath, "jpg");
            //上传图片
            MultipartFile pdfImageFile = PdfUtil.getMulFileByPath(outPath, "textField");
            //Map<String, String> imgParams = new HashMap<>();
            if (imgParams == null) {
                imgParams = new HashMap<>();
                imgParams.put("appId", "order-center/sign/signPdfImg");
                imgParams.put("outer", "true");
                //imgParams.put("appId","order-center/signTemplet");
            }
            pdfImagePath = HttpUtil.postMultiData(url, imgParams, pdfImageFile, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfImagePath;
    }

    /**
     * pdf转成多张图片(tif图片)并上传
     * @param fileName：文件名称，不带后缀
     * */
    public static Map<String, String> pdfToMultiImgAndUpload(String fileName, String url, HttpServletRequest request, String format) {
        Map<String, String> imgMap = new HashMap<>();
        try {
            String localPath = request.getSession().getServletContext().getRealPath("/")+ "data/tempDir/";
            File outFile = new File(localPath);
            if (!outFile.exists()) {
                outFile.mkdirs();
            }
            String pdfPath = localPath + fileName +".pdf";
            //pdf转成多张图片
            Map<String, String> map = PdfUtil.PdfToTifImg(pdfPath, localPath, fileName, format);
            if (null != map) {
                Set<String> set = map.keySet();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String pageId = iter.next(); //pdf页码
                    String tifOutPath = map.get(pageId); //图片存放全路径
                    //上传图片
                    MultipartFile tifImageFile = PdfUtil.getMulFileByPath(tifOutPath, "textField");
                    Map<String, String> tifImgParams = new HashMap<>();
                    tifImgParams.put("appId", "order-center/sign/signPdfImg");
                    tifImgParams.put("outer", "true");
                    String tifImagePath = HttpUtil.postMultiData(url, tifImgParams, tifImageFile, request);
                    logger.info("imagePath=" + tifImagePath);
                    imgMap.put(pageId, tifImagePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgMap;
    }

    /** 上传图片 */
    public static OrderResponse imgUpload(String url, String imgBase64, HttpServletRequest request) {
        MultipartFile multipartFile = null;
        String imgName = "bankPositive.jpg";
        try {
            BASE64Decoder d = new BASE64Decoder();
            byte[] data = d.decodeBuffer(imgBase64);
            InputStream inputStream = new ByteArrayInputStream(data);
            multipartFile = new MockMultipartFile(imgName, imgName, "jpg", inputStream);

        } catch (IOException e) {
            e.printStackTrace();
            return afterUpload(OrderReturnFlag.return_failure_code, "图片处理异常", null);
        }
        Map<String,String> params = new HashMap<>();
        params.put("appId","ocr");
        params.put("outer","true");
        String imgPath = HttpUtil.postMultiData(url, params, multipartFile, request);
        logger.info("======银行卡正面照上传地址===imgPath==="+imgPath);
        if (StringUtil.isEmpty(imgPath)) {
            return afterUpload(OrderReturnFlag.return_failure_code, "银行卡正面照上传失败", null);
        }else {
            return afterUpload(OrderReturnFlag.return_success_code, "银行卡正面照上传成功", imgPath);
        }
    }

    /**
     * 补录资料上传
     * */
    public static OrderResponse furtherImgUpload(String url, MultipartFile file, HttpServletRequest request) {
        Map<String,String> params = new HashMap<>();
        params.put("appId","order-center/furtherInfo");
        params.put("outer","true");
        String imgPath = HttpUtil.postMultiData(url, params, file, request);
        logger.info("======图片上传地址===imgPath==="+imgPath);
        if (StringUtil.isEmpty(imgPath)) {
            return afterUpload(OrderReturnFlag.return_failure_code, "图片上传失败", null);
        }else {
            return afterUpload(OrderReturnFlag.return_success_code, "图片上传成功", imgPath);
        }
    }

    public static OrderResponse afterUpload(String code, String msg, String content) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCode(code);
        orderResponse.setMsg(msg);
        orderResponse.setStrContent(content);
        return orderResponse;
    }

}
