package com.ab.dh.apporder.util;

import com.sun.media.imageio.plugins.tiff.TIFFTag;
import com.sun.media.jai.codec.*;
import com.sun.media.jai.codecimpl.TIFFImageEncoder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfUtil {
    protected static Logger logger = Logger.getLogger(PdfUtil.class);

    /**
     * @Description pdf转成一张图片
     * @created 2019年4月19日 下午1:54:13
     * @param pdfFile  pdf地址
     * @param outpath  生成图片存放的地址（包括图片名称：xxx.jpg）
     *
     */
    public static void pdf2multiImage(String pdfFile, String outpath, String formatName) {
        try {
            InputStream is = new FileInputStream(pdfFile);
            PDDocument pdf = PDDocument.load(is);
            PDFRenderer renderer = new PDFRenderer(pdf);
            int actSize  = pdf.getNumberOfPages();
            List<BufferedImage> piclist = new ArrayList<BufferedImage>();
            for (int i = 0; i < actSize; i++) {
                //dpi越高图片体积越大,一般电脑显示分辨率为96
                //BufferedImage  image = new PDFRenderer(pdf).renderImageWithDPI(i,105, ImageType.RGB);
                BufferedImage image = renderer.renderImageWithDPI(i, 131, ImageType.RGB);
                piclist.add(image);
            }
            yPic(piclist, outpath, formatName);
            is.close();
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将指定pdf文件的首页转换为指定路径的缩略图
     *@param pdfFile 原文件路径，
     *@param outpath 图片生成路径，
     *@param zoom  缩略图显示倍数，1表示不缩放，0.3则缩小到30% 2 表示放大两倍
     */
    public static void pdfToImage(String pdfFile, String outpath, String formatName, float zoom) {
        try {
            Document document = null;
            List<BufferedImage> imageList = new ArrayList<>();

            float rotation = 0f;

            document = new Document();
            document.setFile(pdfFile);
            // maxPages = document.getPageTree().getNumberOfPages();
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage img = (BufferedImage) document.getPageImage(i,
                        GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);
                imageList.add(img);
            }
            yPic(imageList, outpath, formatName);
        } catch (PDFException e) {
            e.printStackTrace();
        } catch (PDFSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     * @param piclist  文件流数组
     * @param outPath  输出路径
     */
    public static void yPic(List<BufferedImage> piclist, String outPath, String formatName) {// 纵向处理图片
        if (piclist == null || piclist.size() <= 0) {
            System.out.println("图片数组为空!");
            return;
        }
        try {
            int height = 0, // 总高度
                    width = 0, // 总宽度
                    _height = 0, // 临时的高度 , 或保存偏移高度
                    __height = 0, // 临时的高度，主要保存每个高度
                    picNum = piclist.size();// 图片的数量
            File fileImg = null; // 保存读取出的图片
            int[] heightArray = new int[picNum]; // 保存每个文件的高度
            BufferedImage buffer = null; // 保存图片流
            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
            int[] _imgRGB; // 保存一张图片中的RGB数据
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);
                System.out.println("width="+ buffer.getWidth());
                if (i == 0) {
                    width = buffer.getWidth();// 图片宽度
                }
                if (i > 0 && buffer.getWidth() > width) {
                    //定义一个BufferedImage对象，用于保存缩小后的位图
                    double wr = buffer.getWidth()*1.0/width;
                    int h = (int)Math.round(buffer.getHeight()/wr);
                    BufferedImage bufferedImage=new BufferedImage(width,h,BufferedImage.TYPE_INT_BGR);
                    Graphics graphics = bufferedImage.getGraphics();
                    //将原始位图缩小后绘制到bufferedImage对象中
                    graphics.drawImage(buffer, 0, 0, width, h, null);
                    buffer = bufferedImage;
                }
                heightArray[i] = _height = buffer.getHeight();// 图片高度

                height += _height; // 获取总高度
                _imgRGB = new int[width * _height];// 从图片中读取RGB
                _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            _height = 0; // 设置偏移高度为0
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                if (i != 0)
                    _height += heightArray[i-1]; // 计算偏移高度
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
            }
            File outFile = new File(outPath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(imageResult, formatName, out);// 写图片
            byte[] b = out.toByteArray();
            FileOutputStream output = new FileOutputStream(outFile);
            output.write(b);
            out.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据地址转成 MultipartFile 文件
     * textFieldName的值为 textField
     * */
    public static MultipartFile getMulFileByPath(String picPath, String textFieldName) {
        FileItem fileItem = createFileItem(picPath, textFieldName);
        MultipartFile mfile = new CommonsMultipartFile(fileItem);
        return mfile;
    }

    public static FileItem createFileItem(String filePath, String textFieldName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        //String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 每页pdf转tif图片
     * @param pdfPath：pdf路径，如"E:\\ceshi\\macth.pdf"
     * @param outPath：图片的输入路径，不带图片名称
     * @param fileName：文件名称，不带后缀
     * */
    public static Map<String,String> PdfToTifImg(String pdfPath, String outPath, String fileName, String format) {
        // 定义Document,用于转换图片

        // 用来保存当前页码的页码
        Map<String,String> map = new HashMap<>();
        float rotation = 0f;
        try {
            // 找到路径
            InputStream is = new FileInputStream(pdfPath);
            PDDocument pdf = PDDocument.load(is);
            PDFRenderer renderer = new PDFRenderer(pdf);
            // 获取这个pdf的页码一共多少页
            int maxPages = pdf.getNumberOfPages();
            // 循环pdf每一页并转换
            for (int i = 0; i < maxPages; i++) {
                //BufferedImage  image = new PDFRenderer(pdf).renderImageWithDPI(i,130, ImageType.RGB);
                BufferedImage image = renderer.renderImageWithDPI(i, 180, ImageType.RGB);
                //BufferedImage.TYPE_INT_RGB ： 表示一个图像，该图像具有整数像素的 8 位 RGB 颜色
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                                                                BufferedImage.TYPE_INT_RGB);
                bufferedImage.getGraphics().drawImage(image, 0, 0, null);
                //图片的输出全路径
                String outAllPath = outPath + fileName +"-"+ (i+1) +"."+format;
                OutputStream os = new FileOutputStream(outAllPath);
                if ("tif".equals(format)) {
                    TIFFEncodeParam param = new TIFFEncodeParam();
                    // 设置压缩方式
                    param.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE);
                    //转换成指定的格式。
                    ImageEncoder enc = ImageCodec.createImageEncoder("TIFF", os, param);
                    enc.encode(bufferedImage);

                }else if ("jpg".equals(format)) {
                    JPEGEncodeParam param = new JPEGEncodeParam();
                    //转换成指定的格式。
                    ImageEncoder enc = ImageCodec.createImageEncoder("JPEG", os, param);
                    enc.encode(bufferedImage);
                }
                os.close();
                image.flush();
                map.put(String.valueOf((i+1)), outAllPath);
            }
            is.close();
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /** pdf上传到服务器 */
    public static String pdfUpload(String pdfName, String url, InputStream inputStream, HttpServletRequest request) {
        String pdfFtpPath = "";
        try {
            MultipartFile file = new MockMultipartFile(pdfName, pdfName, "pdf", inputStream);
            Map<String,String> params = new HashMap<>();
            params.put("appId","order-center/sign/signPdf");
            params.put("outer","true");
            pdfFtpPath = HttpUtil.postMultiData(url, params, file, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFtpPath;
    }


    public static String image2Tif(String fileAbsolutePath){
        OutputStream outputStream = null;
        String filterFilePath = null;
        String tifFilePath = null;
        ImageOutputStream ios = null;
        try {
            // 解决位深度太小 start ====注意：8位深度的图片会出现文件损坏问题
            File picture = new File(fileAbsolutePath);
            // 统一进行一次过滤 转换成24位深度
            filterFilePath = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("."))+".JPG";
            tifFilePath = filterFilePath.substring(0, filterFilePath.lastIndexOf("."))+".tif";
            ios = ImageIO.createImageOutputStream(new File(filterFilePath));
            ImageIO.write(ImageIO.read(picture),"JPG", ios);
            // 解决位深度太小 end
            FileSeekableStream stream = new FileSeekableStream(filterFilePath);
            PlanarImage in = JAI.create("stream", stream);
            OutputStream os = null;
            os = new FileOutputStream(tifFilePath);
            // 设置dpi为300
            TIFFEncodeParam param = new TIFFEncodeParam();
            param.setCompression(TIFFEncodeParam.COMPRESSION_NONE);
            TIFFField[] extras = new TIFFField[2];
            extras[0] = new TIFFField(282, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) 300, 1}, {0, 0}});
//            extras[0] = new TIFFField(282, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) dpi, 1}, {0, 0}});
            extras[1] = new TIFFField(283, TIFFTag.TIFF_RATIONAL, 1, (Object) new long[][]{{(long) 300, 1}, {0, 0}});
            param.setExtraFields(extras);
            TIFFImageEncoder enc = new TIFFImageEncoder(os, param);
            try {
                enc.encode(in);
                os.flush();
                os.close();
                stream.close();
            } catch (Exception e) {
                logger.error("{}",e );
                throw new RuntimeException(e);
            }
            return tifFilePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
