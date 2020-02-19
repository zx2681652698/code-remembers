package com.ab.dh.apporder.util;

import com.ab.dh.apporder.config.SSLClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class HttpclientUtils {
    private static final Logger logger = LoggerFactory.getLogger(Thread
            .currentThread().getStackTrace()[1].getClassName());
    public HttpclientUtils(){
        bulider = RequestConfig.custom();
        this.setHttpSo_Timeout(40000);
        this.setHttpConnection_Timeout(40000);
        initConfig();
        httpclient = HttpClients.createDefault();
    }
    private final String UTF8 = "UTF-8";
    private final String GBK = "GBK";
    private String Encoding = UTF8;
    private CloseableHttpClient httpclient;
    private RequestConfig requestConfig;
    private RequestConfig.Builder bulider;

    public void setEncodingUTF8(){
        Encoding = UTF8;
    }

    public void setEncodingGBK(){
        Encoding = GBK;
    }

    /**
     * 设置http服务器连接超时
     * */
    public void setHttpConnection_Timeout(int timeout) {
        bulider.setConnectTimeout(timeout);
        initConfig();
    }
    /**
     * 设置http服务器响应超时
     * */
    public void setHttpSo_Timeout(int timeout) {
        bulider.setSocketTimeout(timeout);
        initConfig();
    }

    private void initConfig() {
        requestConfig = bulider.build();
    }
    /**
     * post3
     * url 接口地址
     */
    public static String HttpPost3(String url, HttpEntity entity) throws Exception{
        String body = "";
        //创建HttpClient对象
        CloseableHttpClient client = new SSLClient();
        //构建POST请求   请求地址请更换为自己的。
        //1)
        HttpPost post = new HttpPost(url);

        post.setEntity(entity);

        try {
            //post.setEntity(new StringEntity(strJson, Charset.forName("UTF-8")));
            HttpResponse res = client.execute(post);
            if (res != null) {
                body = EntityUtils.toString(res.getEntity());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        }catch (SocketTimeoutException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return body;
    }
}
