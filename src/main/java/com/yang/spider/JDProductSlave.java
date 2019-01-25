package com.yang.spider;

import com.google.gson.Gson;
import com.yang.dao.ProductDao;
import com.yang.domain.Product;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 从redies中获取id，获取详情页
 */
public class JDProductSlave {


    static ExecutorService threadPool= Executors.newFixedThreadPool(10);
    static ProductDao dao = new ProductDao();


    public static void main(String[] args) {

        //创建10个线程爬取
        for (int i=0;i<10;i++){
            threadPool.execute(new Runnable() {
                public void run() {
                    Jedis jedis = new Jedis("192.168.81.101",6379);
                    while (true){
                        try {
                            //从redies中获取商品id查询详情页
                            String pid = jedis.rpop("itcast:spider:jd:pids");
                            //获取商品详情页
                            if(pid!=null){
                                httpGetDetail(pid);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    /**
     * 获取商品详情页
     * @param pid
     */
    private static void httpGetDetail(String pid) {
        String ditailUrl="https://item.jd.com/"+pid+".html";
        HttpGet httpGet = new HttpGet(ditailUrl);

        CloseableHttpClient client = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            String detailHtml = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
            Document document = Jsoup.parse(detailHtml);
            String proTitle = document.select(".sku-name").get(0).text();

            Product product=new Product();
            product.setPid(pid);

            product.setUrl(ditailUrl);

            product.setTitle(proTitle);

            String pingpai = document.select("#parameter-brand a").get(0).text();

            product.setBrand(pingpai);

            String productName = document.select("[class=parameter2 p-parameter-list] li").get(0).attr("title");
            product.setProName(productName);

            String price = httpGetPrice(pid);
            product.setProPrice(price);

            System.out.println(product);
            dao.saveProduct(product);
        } catch (Exception e) {
            //出现异常将pid放回redis
            Jedis jedis = new Jedis("192.168.81.101",6379);
            jedis.lpush("itcast:spider:jd:pids",pid);
            System.out.println("获取商品详情错误："+ditailUrl+"  "+e);
        }
    }

    /**
     * 获取价格
     * @param pid
     */
    private static String httpGetPrice(String pid){
        String ditailUrl="https://p.3.cn/prices/mgets?type=1&&skuIds=J_"+pid;
        HttpGet httpGet = new HttpGet(ditailUrl);
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            String priceJson = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
            Gson gson=new Gson();
            ArrayList arrayList = gson.fromJson(priceJson, ArrayList.class);
            Map<String,String> map= (Map<String, String>) arrayList.get(0);
            String price = map.get("p");
            return price;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
