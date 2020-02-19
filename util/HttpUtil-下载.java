package com.ab.dh.api.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import tk.mybatis.mapper.util.StringUtil;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import java.util.Map.Entry;


/**
 * @author Administrator
 */
public class HttpUtil {
	protected static Logger logger=Logger.getLogger(HttpUtil.class);
    private final static int time = 5000;
    private static String encoding = "UTF-8";

	public static String sendInfo(Map<String, String> requestPropertyMap,
			String postUrl) throws Exception {
		HttpURLConnection httpURLConnection = null;
		BufferedWriter out = null;
		BufferedReader reader = null;

		try {
			if (StringUtil.isNotEmpty(postUrl)) {
				URL httPost = new URL(postUrl);
				httpURLConnection = (HttpURLConnection) httPost
						.openConnection();
				httpURLConnection.setDoOutput(true);// 打开写入属性
				httpURLConnection.setDoInput(true);// 打开读取属性
				httpURLConnection.setRequestMethod("POST");// 设置提交方法
				httpURLConnection.setConnectTimeout(time);// 连接超时时间
				httpURLConnection.setReadTimeout(time);

				httpURLConnection.connect();

				out = new BufferedWriter(new OutputStreamWriter(
						httpURLConnection.getOutputStream(), encoding));
				if (requestPropertyMap != null && requestPropertyMap.size() > 0) {
					int index = 0;

					for (Map.Entry<String, String> entry : requestPropertyMap
							.entrySet()) {
						if (index == 0) {
							out.write(entry.getKey() + "=" + entry.getValue());
						} else {
							out.write("&" + entry.getKey() + "="
									+ entry.getValue());
						}
						System.out.println(entry.getKey() + "="
								+ entry.getValue());

						index++;
					}
				}
				out.flush();

				// 读取post之后的返回值
				reader = new BufferedReader(new InputStreamReader(
                        httpURLConnection.getInputStream(),
						encoding));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString();
			} else {
				return null;
			}

		} catch (Throwable e) {
			throw new Exception(e);
		} finally {
			logger.info("通过HTTP协议的POST方法提交数据到服务器操作已经完成");
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					logger.info(e.getMessage());
				}
			}
			if (httpURLConnection != null) {
				try {
					httpURLConnection.disconnect();
				} catch (Exception e) {
					logger.info(e.getMessage());
				}

			}

		}
	}

    /**
     * 使用Get方式获取数据
     *
     * @param url     URL包括参数，http://HOST/XX?XX=XX&XXX=XXX
     * @return
     * @throws Exception
     */
    public static String sendGet(String url) throws Exception {
        String result = "";
        BufferedReader in = null;

        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        // 设置通用的请求属性
        connection.setConnectTimeout(time);//5秒超时时间
        connection.setReadTimeout(time);//5秒读取时间
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        // 定义 BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), encoding));
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


    public static String sendModifyGet(String url) throws Exception {
        String result = "";
        BufferedReader in = null;

        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection connection = realUrl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        // 设置通用的请求属性
        connection.setConnectTimeout(time);//5秒超时时间
        connection.setReadTimeout(time);//5秒读取时间
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        connection.connect();
        connection.getOutputStream().write("".getBytes());
        // 定义 BufferedReader输入流来读取URL的响应
        in = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), encoding));
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
     * @param url     请求地址
     * @param param   请求数据
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static String sendPostUrl(String url, String param) throws IOException {

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        // 设置通用的请求属性
        conn.setConnectTimeout(time);//5秒超时时间
        conn.setReadTimeout(time);//5秒读取时间
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
        in = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), encoding));
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
     * @param url     请求地址
     * @param param   请求数据
     * @param charset 编码方式
     * @throws IOException
     */
    public static String sendPost(String url, Map<String, Object> param,
                                  String charset) throws IOException {

        StringBuffer buffer = new StringBuffer();
        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                buffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue().toString()))
                        .append("&");

            }
        }
        buffer.deleteCharAt(buffer.length() - 1);

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        conn.setConnectTimeout(time);//5秒超时时间
        conn.setReadTimeout(time);//5秒读取时间
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
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
        in = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), charset));
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

    public static String httpForm(String url, Map<String, String> params) {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        System.out.println("==================send_url:" + url);
        System.out.println("==================send_data:" + sb.toString());
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            //一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }


    public static File downloadFile(String urlPath, String downloadDir,String fileFullName) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpURLConnection.setConnectTimeout(1000 * 5);
            //设置请求方式，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 控制台打印文件大小
            System.out.println("您要下载的文件大小为:" + fileLength / (1024 * 1024) + "MB");

            // 建立链接从请求中获取数据
            URLConnection con = url.openConnection();
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            // 指定文件名称(有需求可以自定义)
            // 指定存放位置(有需求可以自定义)
            String path = downloadDir + File.separatorChar + fileFullName;
            file = new File(path);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 控制台打印文件下载的百分比情况
//                System.out.println("下载了-------> " + len * 100 / fileLength + "%\n");
            }
            // 关闭资源
            bin.close();
            out.close();
            System.out.println("文件下载成功！");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("文件下载失败！");
        } finally {
            return file;
        }

    }

    public static void main(String[] args) {
//        Map<String, String> map = new HashMap<>();
//        map.put("name","陈毓杰");
//        map.put("idNo","44011119831112037X");
//        String s = httpForm("http://dat.dahuobaoxian.com/bfb/bfb/identify/idcard", map);
//        Map parse = (Map)JSONObject.parse(s);
//        Map data = (Map)parse.get("data");
//        Object respCode = data.get("respCode");
//        System.out.println(parse.toString());

        downloadFile("https://life-agent-vir.djbx.com/file/order-center/signTemplet/2019-11-07/Templet1101191028001-1016.pdf","C:\\Users\\liujiji\\Desktop","pdf");
    }

}