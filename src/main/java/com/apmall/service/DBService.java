package com.apmall.service;

import org.springframework.util.ResourceUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBService {
    private Connection con;
    private Statement stat;
    private boolean isOpened = false;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean open() {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:"+ResourceUtils.getFile("classpath:database/task.db"));
            stat = con.createStatement();
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        isOpened = true;
        return true;
    }

    public boolean close() {
        if(this.isOpened == false) {
            return true;
        }
        try {
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void dbConnect() {

        if(!isOpened){
            open();
        }
        /*Connection con = null;
        try { // SQLite JDBC 체크
           Class.forName("org.sqlite.JDBC"); // SQLite 데이터베이스 파일에 연결

           con = DriverManager.getConnection("jdbc:sqlite:"+ResourceUtils.getFile("classpath:database/task.db"));
           Statement stat = con.createStatement();
           ResultSet rs = stat.executeQuery("SELECT * FROM category;");
           while(rs.next()) {
               String id = rs.getString("category_no");
               System.out.println(id + " ");
           }
        }catch(Exception e) {
           e.printStackTrace();
        }finally {
           if(con != null) {
               try {
                   con.close();
               }catch(Exception e) {
               }
           }
        }*/

    }

    public List<Map<String, Object>> selectAllProduct(){
        List<Map<String, Object>> productList= new ArrayList<>();
        try{

            String queryStr ="select ";
            queryStr += "product_no, brand_name, product_name, product_price, prd.category_no, category_name ";
            queryStr += "from product prd                                             ";
            queryStr += "left join (                                                  ";
            queryStr += "	select                                                    ";
            queryStr += "		a.category_no,                                        ";
            queryStr += "		CASE                                                  ";
            queryStr += "			WHEN a.depth = 2                                  ";
            queryStr += "				THEN b.category_name || '_' || a.category_name";
            queryStr += "			ELSE a.category_name                              ";
            queryStr += "		END category_name                                     ";
            queryStr += "	from category a                                           ";
            queryStr += "	left join category b                                      ";
            queryStr += "	on a.parent_no = b.category_no                            ";
            queryStr += ")ct                                                          ";
            queryStr += "on prd.category_no = ct.category_no                          ";

            ResultSet rs = stat.executeQuery("queryStr");

            while(rs.next()) {
               String id = rs.getString("category_no");
               System.out.println(id + " ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return productList;
    }
}
