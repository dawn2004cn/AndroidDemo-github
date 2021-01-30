package com.noahedu.demo.mybatis.core;

import com.noahedu.audiorecorder.base.MyApp;
import com.noahedu.demo.mybatis.dao.UserMapper;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
/**
 * © 2020 www.youxuepai.com
 *
 * @version 1.0
 * @file name：MybatisUtils$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/11/30$ 17:09$
 */
public class MybatisUtils {

    static public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = MyApp.getInstance().getApplicationContext().getAssets().open(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SqlSessionFactoryBuilder().build(inputStream);
    }
    static public void test() throws IOException{
        // 1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        // 2、获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();
        try {
            // 3、获取接口的实现类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            UserMapper mapper = openSession.getMapper(UserMapper.class);
            //User employee = mapper.select(1);
            System.out.println(mapper.getClass());

        } finally {
            openSession.close();
        }

    }

}
