package com.ab.dh.apporder.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Administrator
 */
public class HttpUtil {
	private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private final static int time = 5000;

	/**
	 * 使用Get方式获取数据
	 *
	 * @param url
	 *            URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String sendGet(String url, String charset) throws Exception {
		String result = "";
		BufferedReader in = null;

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection connection = realUrl.openConnection();
		// 设置通用的请求属性
		connection.setConnectTimeout(time);// 30秒超时时间
		connection.setReadTimeout(time);// 30秒读取时间
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 建立实际的连接
		connection.connect();
		// 定义 BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}

		// 使用finally块来关闭输入流
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		return result;
	}

	/**
	 * POST请求，字符串形式数据
	 *
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostUrl(String url, String param, String charset)
			throws UnsupportedEncodingException, IOException {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setConnectTimeout(time);// 30秒超时时间
		conn.setReadTimeout(time);// 30秒读取时间
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		conn.setRequestProperty("Referer", "http://mendian.dbjb.com");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(param);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;

		}
		// 使用finally块来关闭输出流、输入流

		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * POST请求，字符串形式数据
	 *
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static String sendPostUrlObject(String url, Object param, String charset)
			throws UnsupportedEncodingException, IOException {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		// 设置通用的请求属性
		conn.setConnectTimeout(time);// 30秒超时时间
		conn.setReadTimeout(time);// 30秒读取时间
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		conn.setRequestProperty("Referer", "http://mendian.dbjb.com");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(param);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;

		}
		// 使用finally块来关闭输出流、输入流

		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * POST请求，Map形式数据
	 *
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 * @throws IOException
	 */
	public static String sendPost(String url, Map<String, String> param, String charset) throws IOException {

		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue())).append("&");

			}
		}
		buffer.deleteCharAt(buffer.length() - 1);

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		conn.setConnectTimeout(time);// 5秒超时时间
		conn.setReadTimeout(time);// 5秒读取时间
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(buffer);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}

		// 使用finally块来关闭输出流、输入流

		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * post请求重定向封装
	 * 
	 * @param resp
	 * @param url
	 * @param params
	 */
	/*public static void postRedirect(HttpServletResponse resp, String url, Map<String, String> params) throws Exception {
		postRedirect(url, params, (entity) -> {
			try {
				resp.setContentType(entity.getContentType().getValue());
				StreamUtils.copy(entity.getContent(), resp.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		});
	}*/

	/**
	 * post请求重定向封装
	 * 
	 * @param
	 * @param url
	 * @param params
	 * @throws Exception
	 */
	/*public static String postRedirect(String url, Map<String, String> params) throws Exception {
		return (String) postRedirect(url, params, (entity) -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			StringBuilder str = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				str.append(line);
			}
			return str.toString();
		});
	}*/

	/**
	 * post请求重定向封装
	 * 
	 * @param
	 * @param url
	 * @param params
	 */
	/*public static Object postRedirect(String url, Map<String, String> params, ResponseHandler handler)
			throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(1000)
				.setSocketTimeout(30000).build();
		httpPost.setConfig(requestConfig);
		// 参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			nvps.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		// 请求
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			// System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return null;
			}
			if (entity.isStreaming()) {
				return handler.makeInfo(entity);
			} else {
				return entity.getContent();
			}
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

	private static final ContentType APPLICATION_XML = ContentType.create("application/xml", "GBK");

	/*public static String sendPostB2BRequest(String url, String xmlString) throws Exception {
		String start = DateUtil.getNow();
		logger.info(">>>>>>>HttpEdiHelper.sendPostB2BRequest，时间标记【" + start + "】请求地址：" + url + "<<<<<<<<开始<<<<<<");
		logger.info("发送报文：\n" + formatXML(xmlString));
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(1000)
				.setSocketTimeout(30000).build();
		httpPost.setConfig(requestConfig);
		httpPost.setEntity(new StringEntity(xmlString, APPLICATION_XML));
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				throw new Exception("返回信息为空");
			}
			if (entity.isStreaming()) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "GBK"));
				StringBuilder str = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
				String responseXml = str.toString();
				logger.info(">>>>>>>时间标记:【" + start + "】，响应报文如下：\n" + formatXML(responseXml));
				return responseXml;
			} else {
				throw new Exception("未识别返回信息");
			}
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

    /*public static String sendPostB3BRequest(String url, String xmlString) throws Exception {
        String start = DateUtil.getNow();
        logger.info(">>>>>>>HttpEdiHelper.sendPostB2BRequest，时间标记【" + start + "】请求地址：" + url + "<<<<<<<<开始<<<<<<");
        logger.info("发送报文：\n" + formatXML(xmlString));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(30000).build();
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Content-Type","application/soap+xml; charset=GBK");
        httpPost.setHeader("SOAPAction","application/soap+xml; charset=GBK");//随便填，必须要有值

        httpPost.setEntity(new StringEntity(xmlString, APPLICATION_XML));
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new Exception("返回信息为空");
            }
            if (entity.isStreaming()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "GBK"));
                StringBuilder str = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
                String responseXml = str.toString();
                logger.info(">>>>>>>时间标记:【" + start + "】，响应报文如下：\n" + formatXML(responseXml));
                return responseXml;
            } else {
                throw new Exception("未识别返回信息");
            }
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

	/***
	 * 格式化XML 输出
	 * 
	 * @param xmlStr
	 * @return
	 * @throws Exception
	 */
	public static String formatXML(String xmlStr) throws Exception {
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
	}

	/**
	 * requestBody
	 */
	public static JSONObject sendURLRequestBody(String urlSite, Map<String, String> map) {
		URL url = null;
		HttpURLConnection conn = null;
		try {
			ObjectMapper om = new ObjectMapper();
			String paramJson = om.writeValueAsString(map);
			JSONObject obj = JSONObject.parseObject(paramJson);
			byte[] writeByte = obj.toString().getBytes("UTF-8");
			url = new URL(urlSite);
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setConnectTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setReadTimeout(600000);
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Content-Length", String.valueOf(writeByte.length));
			// 添加主体信息 加入header
			// String memberIdentity =
			// obj.get("memberIdentity")==null?null:obj.get("memberIdentity").toString();
			// if(StringUtils.isNotBlank(memberIdentity)){
			// conn.setRequestProperty("memberIdentity", memberIdentity);
			// }
			// String memberType =
			// obj.get("memberType")==null?null:obj.get("memberType").toString();
			// if(StringUtils.isNotBlank(memberType)){
			// conn.setRequestProperty("memberType", memberType);
			// }
			// conn.connect();
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(obj.toString().getBytes("UTF-8"));
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return JSONObject.parseObject(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * POST请求，Map形式数据
	 *
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 * @throws IOException
	 */
	public static String sendPostSms(String url, Map<String, String> param, String charset) throws IOException {

		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				buffer.append(entry.getKey()).append("=")
						// .append(URLEncoder.encode(entry.getValue()))
						.append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");

			}
		}
		buffer.deleteCharAt(buffer.length() - 1);

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		conn.setConnectTimeout(time);// 5秒超时时间
		conn.setReadTimeout(time);// 5秒读取时间
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(buffer);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}

		// 使用finally块来关闭输出流、输入流

		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * POST请求，Map形式数据
	 *
	 * @param url
	 *            请求地址
	 * @param param
	 *            请求数据
	 * @param charset
	 *            编码方式
	 * @throws IOException
	 */
	public static String sendPostRequset(String url, Map<String, String> param, String charset) {
		StringBuffer buffer = new StringBuffer();
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			if (param != null && !param.isEmpty()) {
				for (Map.Entry<String, String> entry : param.entrySet()) {
					buffer.append(entry.getKey()).append("=")
							// .append(URLEncoder.encode(entry.getValue()))
							.append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");

				}
			}
			if (buffer.length() > 0)
				buffer.deleteCharAt(buffer.length() - 1);
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			conn.setConnectTimeout(time);// 5秒超时时间
			conn.setReadTimeout(time);// 5秒读取时间
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(buffer);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		}
		// 使用finally块来关闭输出流、输入流
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}
	/*
	 * 返回为JSONObject的post请求，map中传的参数为header
	 */
	public static JSONObject postRequest(String url, Map<String, String> headers, String jsonStr)
			throws UnsupportedEncodingException {
		StringBuilder response = new StringBuilder();
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		if (jsonStr != null) {
			RequestEntity requestEntity = new StringRequestEntity(jsonStr, "application/json", "UTF-8");
			method.setRequestEntity(requestEntity);
		}
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				method.addRequestHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			client.executeMethod(method);

			response.append(method.getResponseBodyAsString());
		} catch (IOException e) {
			// LOG.error("执行HTTP Post请求 {} 时，发生异常！", url);
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return JSONObject.parseObject(response.toString());
	}
	/*
	 * 返回为JSONObject的post请求
	 */
	public static JSONObject sendPostRequest(String url, Map<String, String> param, String charset) throws IOException {

		StringBuffer buffer = new StringBuffer();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue())).append("&");

			}
		}
		buffer.deleteCharAt(buffer.length() - 1);

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		URL realUrl = new URL(url);
		// 打开和URL之间的连接
		URLConnection conn = realUrl.openConnection();
		conn.setConnectTimeout(time);// 5秒超时时间
		conn.setReadTimeout(time);// 5秒读取时间
		// 设置通用的请求属性
		conn.setRequestProperty("accept", "*/*");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		out = new PrintWriter(conn.getOutputStream());
		// 发送请求参数
		out.print(buffer);
		// flush输出流的缓冲
		out.flush();
		// 定义BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		// 使用finally块来关闭输出流、输入流
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return JSONObject.parseObject(result);
	}

	/**
	 * requestBody
	 */
	public static JSONObject sendPostRequestBody(String url, String paramJson)  throws IOException  {
		URL realUrl = null;
		HttpURLConnection conn = null;
		try {
			JSONObject obj = JSONObject.parseObject(paramJson);
			byte[] writeByte = obj.toString().getBytes("UTF-8");
			realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setUseCaches(false);
			conn.setConnectTimeout(3000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setReadTimeout(600000);
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Content-Length", String.valueOf(writeByte.length));
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.write(writeByte);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return JSONObject.parseObject(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}



	private static final int TIMEOUT = 50000;

	private static final String ENCODING = "UTF-8";

	//上传
	public static String postMultiData(String url, Map<String, String> params, MultipartFile file, HttpServletRequest request) {
		String fileStr = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().
				setCookieSpec(CookieSpecs.IGNORE_COOKIES).
				setConnectTimeout(TIMEOUT).
				setConnectionRequestTimeout(TIMEOUT).
				setSocketTimeout(TIMEOUT).
				setRedirectsEnabled(false).build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setConfig(requestConfig);
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		//Properties props = new Properties();
		try {
			//props= PropertiesLoaderUtils.loadAllProperties("config.properties");
            //String tempDir = (String)props.get("tempDir");
            String tempDir = request.getSession().getServletContext().getRealPath("/") +"data/tempDir/";  // 临时文件保存目录
			logger.info("tempDir="+tempDir);
			File filePath = new File(tempDir);
			if (!(filePath.exists())) {
				filePath.mkdirs();
			}
			//System.out.println("---保存文件-saveNOULL-"+tempDir);
			String	fileName = "";
			String extName = "";
			if (null != file && !file.isEmpty()) {
				fileName = UUID.randomUUID().toString();
				if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
					extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				}
                logger.info("extName="+extName);
				if (".pdf".equals(extName)) {
				    fileName = file.getOriginalFilename().replace(".pdf", "");
                }
                logger.info("fileName="+fileName);
				File transferFile = new File(tempDir + fileName + extName);
				file.transferTo(transferFile);
				multipartEntityBuilder.addBinaryBody("file", transferFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != params) {
			Set<String> set = params.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				String v = params.get(key);
				multipartEntityBuilder.addPart(key, new StringBody(v, ContentType.APPLICATION_JSON));
			}
		}
		try {
			httpPost.setEntity(multipartEntityBuilder.build());
			HttpResponse response = httpClient.execute(httpPost);
			String res = EntityUtils.toString(response.getEntity(), ENCODING);
			logger.info("返回信息为：******"+res);
			if(res==null || res.isEmpty()){
				return fileStr;
			}
			JSONObject jsonObj = JSONObject.parseObject(res);
			if(StringUtils.equals("1",jsonObj.getString("code"))){
				JSONObject refObj = JSONObject.parseObject(jsonObj.getString("data"));
				if(refObj!=null){
					logger.info("上传成功："+refObj.getString("key"));
					fileStr = refObj.getString("key");
				}
			}else{
				logger.info("上传失败，错误信息为："+jsonObj.getString("msg"));
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (null != httpClient) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileStr;
	}

	//下载
	public static String download(String url, String key, String desPath) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().
				setCookieSpec(CookieSpecs.IGNORE_COOKIES).
				setConnectTimeout(TIMEOUT).
				setConnectionRequestTimeout(TIMEOUT).
				setSocketTimeout(TIMEOUT).
				setRedirectsEnabled(false).build();
		HttpGet httpGet = new HttpGet(url + "?key=" + key);
		httpGet.setConfig(requestConfig);

		File des = new File(desPath);
		if (!des.exists()) {
			des.mkdirs();
		}
		if (!desPath.endsWith(File.separator)) {
			desPath = desPath + File.separator;
		}
		InputStream is = null;
		OutputStream os = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			String original = response.getFirstHeader("Content-Disposition").getValue().split("[=]")[1];
			original = new String(original.getBytes("ISO-8859-1"), "UTF-8");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			os = new FileOutputStream(desPath + original);
			byte[] bytes = new byte[2048];
			int length;
			while ((length = is.read(bytes)) > 0) {
				os.write(bytes, 0, length);
			}
			os.flush();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os) {
					os.close();
				}
				if (null != is) {
					is.close();
				}
				if (null != httpClient) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
	
}
