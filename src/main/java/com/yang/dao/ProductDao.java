package com.yang.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yang.domain.Product;
import org.springframework.jdbc.core.JdbcTemplate;


public class ProductDao extends JdbcTemplate{
    public ProductDao(){
        ComboPooledDataSource dataSource=new ComboPooledDataSource();
//        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://192.168.81.101:3306/jdspider");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        setDataSource(dataSource);
    }

    public void saveProduct(Product product){
        String sql="INSERT INTO jdspider.product(pid,url,title,brand,proName,proPrice) VALUES(?,?,?,?,?,?)";
        update(sql,product.getPid(),product.getUrl(),product.getTitle(),product.getBrand(),product.getProName(),product.getProPrice());
    }





}
