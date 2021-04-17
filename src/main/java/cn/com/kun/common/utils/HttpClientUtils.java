package cn.com.kun.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * 基于HttpClient的http工具类
 * @author Kunghsu
 * @datetime 2017年10月14日 下午8:13:27
 * @desc
 */
public class HttpClientUtils {

    private static final String ENCODEING = "UTF-8";

    /**
     * post方式提交
     */
    public static String doPost(String targetUrl, Map<String, String> paramsMap) {

        String result = "";
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(targetUrl);
        // 创建参数队列
        List<NameValuePair> formparams = getAllParamsFromMap(paramsMap);

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, ENCODEING);
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    //获取返回内容
//					System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    //方法1
//					result = EntityUtils.toString(entity, ENCODEING);
                    //方法2
                    InputStream inputStream = entity.getContent();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        builder.append(line);
                    }
                    result = builder.toString();
                }
                return result;//返回结果
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送 get请求
     */
    public static String doGet(String targetUrl, Map<String, String> paramsMap) {

        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            //本来是链式的写法
//			URI uri = new URIBuilder("http://www.baidu.com/s").setParameter("wd","java").build();
            URIBuilder uriBuilder = new URIBuilder(targetUrl);
            setParamsFromMap(uriBuilder, paramsMap);//假如有get方法的请求参数，就放到uri对象中
            URI uri = uriBuilder.build();
            // 创建httpget对象
            HttpGet httpget = new HttpGet(uri);//参数可以拼在url后
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                }
                result = EntityUtils.toString(entity);
//					System.out.println("Response content: " + result);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void setParamsFromMap(URIBuilder uriBuilder, Map<String, String> paramsMap){

        if (paramsMap == null){
            return;
        }

        Iterator<Map.Entry<String, String>> entries = paramsMap.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            uriBuilder.setParameter(entry.getKey(), entry.getValue());
        }

    }

    /**
     * 获取所有请求参数放入List中
     * @param paramsMap
     * @return
     */
    public static List<NameValuePair> getAllParamsFromMap(Map<String, String> paramsMap){

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> entries = paramsMap.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        return formparams;
    }

    /**
     * 上传文件
     */
    public static void upload(String filePath) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceFile.action");

            FileBody bin = new FileBody(new File(filePath));//"F:\\image\\test.jpg"
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment)
                    .build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
