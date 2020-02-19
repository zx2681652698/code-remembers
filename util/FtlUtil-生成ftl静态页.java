package com.ab.dh.api.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

/**
 * @author Liujiji
 * @date 2019/12/25
 */
public class FtlUtil {

    public static void genFile(Model model,String ftlFileName,File file,File htmlFile) throws Exception {
        //logger.info("templateRealURL="+ templateRealURL);
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        // 第二步：设置模板文件所在的路径，一般都在web项目WEB-INF下的ftl文件夹中
        configuration.setDirectoryForTemplateLoading(file);
        // 第三步：初始化一些配置,设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate(ftlFileName);
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名
        Writer out = new FileWriter(htmlFile);
        // 第七步：调用模板对象的process方法输出文件。
        template.process(model, out);
        // 第八步：关闭流。
        out.close();
    }
    /**
     * 生成产品详情页的静态页面
     * */
    public static void genFile(Model model, String productNo,String resourcePath) throws Exception {
        String templateRealURL = resourcePath.replaceAll("target/classes/", "src/main/resources/templates");
        //logger.info("templateRealURL="+ templateRealURL);
        String htmlURL = resourcePath .replaceAll("target/classes/", "src/main/resources/static/staticHtml/productDetail")+ productNo +".html";
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        // 第二步：设置模板文件所在的路径，一般都在web项目WEB-INF下的ftl文件夹中
        //"D:\\anbang\\abdh_new\\cms-service\\src\\main\\resources\\templates"
        configuration.setDirectoryForTemplateLoading(new File(templateRealURL));
        // 第三步：初始化一些配置,设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate("productDetail.ftl");
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名
        Writer out = new FileWriter(new File(htmlURL));
        // 第七步：调用模板对象的process方法输出文件。
        template.process(model, out);
        // 第八步：关闭流。
        out.close();
    }
}
