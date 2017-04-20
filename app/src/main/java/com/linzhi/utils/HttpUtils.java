package com.linzhi.utils;

import com.linzhi.R;
import com.linzhi.application.MyApplication;
import com.linzhi.common.MyException;
import com.linzhi.common.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * 访问网络方法
 * (2)检查网络设置，
 * 13 14 获取json的JSONObject的方法，(HttpURLConnection post方式,HashMap<String,String>)
 * 15 获取json的JSONObject的方法(HttpURLConnection post方式, HashMap<String,String>和File)
 * 16 对JSON结果统一处理 handleResult
 *
 * @author JackSong
 */
public class HttpUtils {

    public static final int TIMEOUT1 = 10;// 超时设置
    public static final int TIMEOUT = 30000;// 超时设置

    /**
     * 单例模式设置
     */
    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        return HttpUtilsContainer.instance;
    }

    public static class HttpUtilsContainer {
        private static HttpUtils instance = new HttpUtils();
    }

    /**
     * 检查网络
     *
     * @throws MyException
     */
    void checkNetwork() throws MyException {
        if (!NetworkManager.isNetworkAvailable(MyApplication.getInstance())) {
            throw new MyException(R.string.network_invalid);
        }
    }

    /**
     * 01 url
     * <p>
     * HttpURLConnection Get
     *
     * @param urlStr
     * @return
     * @throws MyException
     * @throws Exception
     */
    public JSONObject getByURL(String urlStr) throws MyException {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT);// 读取超时
            conn.setConnectTimeout(TIMEOUT);// 链接超时
            conn.setRequestMethod("GET");// get
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");

            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];

                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                baos.flush();
                JSONObject jsonObject = new JSONObject(baos.toString());
                return jsonObject;
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            throw new MyException(e.toString());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            // 关闭连接
            conn.disconnect();
        }
    }

    /**
     * (HttpURLConnection post)
     *
     */
    public JSONObject postStringURL(String urlPost, JSONObject jsonObject, boolean withLogin) throws MyException {
        if (!withLogin) {
            return null;
        }

        return postStringURL(urlPost, jsonObject);
    }

    /**
     *
     */
    public JSONObject postStringURL(String urlPost, JSONObject jsonObject) throws MyException {
        //变量
        JSONObject js = null;
        DataOutputStream outStream = null;
        InputStreamReader in = null;
        HttpURLConnection conn = null;
        //常量
        String BOUNDARY = java.util.UUID.randomUUID().toString();// 边界标识 随机生成
        String PREFIX = "--";
        String LINEND = "\r\n";
        String MULTIPART_FORM_DATA = "multipart/form-data";// 内容类型
        String CHARSET = "UTF-8";
        try {
            //(1)创建url对象
            URL url = new URL(urlPost);
            //(2)利用HttpURLConnectioncont从网络中获取数据
            conn = (HttpURLConnection) url.openConnection();
            //(3)设置连接要求
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
//            conn.setRequestProperty("Charset", "UTF-8");// 设置编码
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

            /*
             * 首先组拼文本类型参数
			 */

            // 获取输出流
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(jsonObject.toString().getBytes());
            outStream.flush();


            //(4)得到响应码
            int res = conn.getResponseCode();

            if (res == 200) {

                //(5)得到数据流
                in = new InputStreamReader(conn.getInputStream(), "utf-8");
                int ch;
                StringBuilder sb3 = new StringBuilder();

                while ((ch = in.read()) != -1) {
                    sb3.append((char) ch);
                }
                js = new JSONObject(sb3.toString());
                return js;
            } else {
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (IOException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            throw new MyException(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(e.toString());
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                throw new MyException("你竟然敢报IO异常");
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                throw new MyException("你竟然敢报IO异常");
            }
            // 关闭连接
            conn.disconnect();
        }

        return js;
    }

} 
