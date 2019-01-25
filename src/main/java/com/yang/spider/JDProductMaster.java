package com.yang.spider;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.nio.charset.Charset;

/**
 * 分页获取商品id，保存到redies
 */
public class JDProductMaster {

    public  static Jedis jedis=new Jedis("192.168.81.101",6379);
    public static void main(String[] args) throws Exception {
        //分页获取数据
        String url="https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid2=653&cid3=655&page=";
        for (int i=1;i<100;i++){
            System.out.println("page:-----------------"+i);
            getPageHtml(url+i);
        }
    }

    /**
     * 获取每一页的列表
     * @param url
     * @throws Exception
     */
    private static void getPageHtml(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse res = client.execute(httpGet);

        String html = EntityUtils.toString(res.getEntity(), Charset.forName("utf-8"));

        Document document = Jsoup.parse(html);
        Elements select = document.select("#J_goodsList li[class=gl-item]");
        for (Element element : select) {
            String pid=element.attr("data-sku");
            //将列表中商品id加入到redis
            System.out.println(pid);
            jedis.lpush("itcast:spider:jd:pids",pid);
        }
    }
}
