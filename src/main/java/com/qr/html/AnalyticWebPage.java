package com.qr.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.FileUtils;
import util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class AnalyticWebPage {
    private static final File root = new File("C:\\Users\\QianRui\\Desktop\\图片");

    public static String getWebPage(String url) {
        String localName = ZipUtil.zip(url, 9);
        System.out.println(localName);
        File file = new File(root, localName);
        if (file.exists()) {
            byte[] val = FileUtils.getFile(file);
            return new String(ZipUtil.unzip(val));
        } else {
            try {
                String pageXml = getWebPage0(url);
                byte[] localValue = ZipUtil.zip(pageXml.getBytes(), 9);
                FileUtils.toFile(file, localValue);
                return pageXml;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String getWebPage0(String url) throws IOException {
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

        webClient.getOptions()
                .setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions()
                .setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions()
                .setActiveXNative(false);
        webClient.getOptions()
                .setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions()
                .setJavaScriptEnabled(true); //很重要，启用JS
        webClient.setJavaScriptTimeout(1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        try {
            HtmlPage page = webClient.getPage(url);//尝试加载上面图片例子给出的网页
            return page.asXml();
        } finally {
            webClient.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String pageXml = getWebPage("http://bbs.duowan.com/forum.php?mod=viewthread&tid=36731270&page=1&authorid=53393409");

        Document document = Jsoup.parse(pageXml);//获取html文档
        Elements tables = document.getElementsByClass("t_table");
        List<List<Object[]>> gifList = new ArrayList<>();
        for (Element table : tables) {
            Elements trs = table.getElementsByTag("tr");
            for (int i = 0; i < trs.size(); i += 2) {
                Elements names = trs.get(i + 1)
                        .getElementsByTag("td");
                Elements gifs = trs.get(i)
                        .getElementsByTag("td");
                List<Object[]> gifRow = tryHold(gifs, names);
                gifList.add(gifRow);
            }
        }
        Object[] objects = gifList.stream()
                .flatMap((Function<List<Object[]>, Stream<?>>) Collection::stream)
                .toArray();
        StringBuilder sb = new StringBuilder();
        for (Object object : objects) {
            Object[] o = (Object[]) object;
            sb.append("insert into gif(name,url,valid) values('")
                    .append(o[1])
                    .append("','")
                    .append(o[2])
                    .append("',")
                    .append(1)
                    .append(");\n");
        }
        FileUtils.toFile("C:\\Users\\QianRui\\Desktop\\图片\\gifList.sql", sb.toString());
    }

    private static List<Object[]> tryHold(Elements gifs, Elements names) {
        List<Object[]> gifRow = new ArrayList<>();
        int min = Math.min(names.size(), gifs.size());
        for (int i = 0; i < min; i++) {
            Elements gif = gifs.get(i)
                    .getElementsByTag("img");
            if (gif.size() == 0) continue;
            String html = names.get(i)
                    .html();
            String[] split = html.split(" ");
            gifRow.add(new Object[]{Integer.parseInt(split[0]), split[1], gif.get(0).attr("file")});
        }
        return gifRow;
    }
}
