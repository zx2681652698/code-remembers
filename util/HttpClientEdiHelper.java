package com.ab.dh.apporder.util;

import com.ab.dh.apporder.config.SSLClient;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.util.Map;

public class HttpClientEdiHelper {

    protected static Logger logger = Logger.getLogger(HttpClientEdiHelper.class);
    private static final String ENCODING = "UTF-8";
    private static final int TIMEOUT = 50000;

    public static String sendPostB2BRequest(String postUrl, Map<String,Object> map) throws Exception {
        //logger.info(">>>>>>>HttpEdiHelper.sendPostB2BRequest 接口名:【" + name + "】，请求地址：" + postUrl + "<<<<<<<<开始<<<<<<");
        DefaultHttpClient httpClient = new SSLClient();
        ObjectMapper om = new ObjectMapper();
        String paramJson = om.writeValueAsString(map);
        JSONObject obj = JSONObject.parseObject(paramJson);
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(TIMEOUT).
                setConnectionRequestTimeout(TIMEOUT).
                setSocketTimeout(TIMEOUT).
                setRedirectsEnabled(false).build();
        httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setConfig(requestConfig);
        StringEntity stringEntity = new StringEntity(obj.toString(), ENCODING);
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        try {
            if (StringUtils.isNotEmpty(postUrl)) {
                logger.info(">>>>>>>接口名:【" + postUrl + "】，请求报文如下：\n");
                logger.info(paramJson);
                HttpResponse response = httpClient.execute(httpPost);
                logger.info(">>>>>>>httpClient响应状态："+response.getStatusLine().getStatusCode()+"\n");
                String responseXml = "";
                if(response.getStatusLine().getStatusCode()==200){
                    logger.info(">>>>>>>接口名:【" + postUrl + "】，响应报文如下：\n");
                    responseXml = EntityUtils.toString(response.getEntity());
                    //logger.info(formatXML(responseXml));
                }
                return responseXml;
            } else {
                return null;
            }

        } catch (Throwable e) {
            e.printStackTrace();
            throw new Exception(e);
        } finally {
            logger.info(">>>>>>>HttpEdiHelper.sendPostB2BRequest 接口名:【" + postUrl + "】，请求地址：" + postUrl + "<<<<<<<<结束<<<<<<");
            if(null != httpClient){
                httpClient.close();
            }
        }
    }

    /***
     * 格式化XML 输出
     *
     * @param xmlStr
     * @return
     * @throws Exception
     */
    /*public static String formatXML(String xmlStr) throws Exception {
        StringWriter out = null;
        try {
            SAXReader reader = new SAXReader();
            StringReader in = new StringReader(xmlStr);
            Document doc = reader.read(in);
            OutputFormat formate = OutputFormat.createPrettyPrint();
            out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, formate);
            writer.write(doc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
        return out.toString();
    }*/

  
}
