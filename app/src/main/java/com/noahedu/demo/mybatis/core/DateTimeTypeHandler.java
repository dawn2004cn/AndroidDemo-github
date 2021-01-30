package com.noahedu.demo.mybatis.core;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeTypeHandler extends BaseTypeHandler<Date> {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void setParameter(PreparedStatement ps, int i,
                             Date parameter, JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		if(parameter != null) {
			ps.setString(i, sdf.format(parameter));
		}else{
			ps.setString(i, null);
		}

	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
                                    Date parameter, JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		ps.setString(i, sdf.format(parameter));
	}

	  @Override
	  public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Date d = null;
		if(rs.getString(columnName) != null) {
			try {
				d = sdf.parse(rs.getString(columnName));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    return d;
	  }

	  @Override
	  public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		  Date d = null;
		  if(rs.getString(columnIndex) != null) {
			try {
				d = sdf.parse(rs.getString(columnIndex));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		    return d;
	  }

	  @Override
	  public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		  Date d = null;
		  if(cs.getString(columnIndex) != null) {
			try {
				d = sdf.parse(cs.getString(columnIndex));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		    return d;
	  }

	

}
