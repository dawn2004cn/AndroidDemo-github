package com.noahedu.demo.mybatis.core;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by RefitD on 10/27/2015.
 */
public abstract  class SqlSessionDaoSupport {

    private SqlSession sqlSession;

    public SqlSessionDaoSupport(){
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try{
            inputStream = MainApp.getAppContext().getAssets().open(resource);
        } catch (
    IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        this.sqlSession = sqlSessionFactory.openSession();
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

}
