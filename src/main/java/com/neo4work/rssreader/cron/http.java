package com.neo4work.rssreader.cron;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.io.entity.EntityUtils;


import javax.net.ssl.SSLContext;

public class http
{
    private static final Log log = LogFactory.getLog(http.class);

    public static String get(String url)
    {
        String result = "";
        HttpGet httpGet = new HttpGet(url);

        try {
            // 使用项目中已有的SSLUtil类创建不安全的SSLContext
            SSLContext sslContext = SSLUtil.createUnsafeSSLContext();

            // 创建SSLConnectionSocketFactory
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);
            PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(sslSocketFactory).build();
            // 构建HttpClient，使用setSSLSocketFactory方法的正确名称
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .build();

            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // 输出响应状态码
            //System.out.println("Response Code: " + response.getCode());
            if(response.getCode() == 200)
            {
                //System.out.println(EntityUtils.toString(response.getEntity()));
                result= EntityUtils.toString(response.getEntity());
            }

            // 关闭资源
            response.close();
            httpClient.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
