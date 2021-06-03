package com.kh.spring.menu.model.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.kh.spring.menu.model.vo.MenuType;

@MappedTypes(MenuType.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class MenuTypeHandler extends BaseTypeHandler<MenuType> {
	
	//java -> db
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, MenuType parameter, JdbcType jdbcType)
			throws SQLException {
		// TODO Auto-generated method stub
		ps.setString(i, parameter.getValue()); // kr
		
	}
	//db -> java
	@Override
	public MenuType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		return MenuType.menuTypeValueOf(rs.getString(columnName));
	}
	//db -> java
	@Override
	public MenuType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return MenuType.menuTypeValueOf(rs.getString(columnIndex));
	}
	//db -> java
	@Override
	public MenuType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return MenuType.menuTypeValueOf(cs.getString(columnIndex));
	}

}
