package util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public class HttpGeter {
    private static final CloseableHttpClient httpClient;

    static {
        Registry<ConnectionSocketFactory> build;
        try {
            build = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", new SSLConnectionSocketFactory(SSLContext.getDefault()))
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(build);
        cm.setDefaultMaxPerRoute(20);
        cm.setMaxTotal(200);
        cm.setDefaultConnectionConfig(ConnectionConfig.DEFAULT);
        cm.setValidateAfterInactivity(1000 * 60 * 60);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(3000)
                .setConnectTimeout(1000)
                .setSocketTimeout(5000)
                .build();
        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static void invoke(String url, String fileName) {
        HttpGet method = new HttpGet(url);
        method.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        method.addHeader("Accept-Encoding", "gzip, deflate");
        method.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        try (CloseableHttpResponse response = httpClient.execute(method); InputStream instream = response.getEntity()
                .getContent(); FileOutputStream outputStream = new FileOutputStream(fileName)) {
            byte[] bytes = new byte[2 << 16];
            int read = 0;
            while ((read = instream.read(bytes)) > -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        method.releaseConnection();
    }
}
