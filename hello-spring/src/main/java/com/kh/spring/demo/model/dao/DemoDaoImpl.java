package com.kh.spring.demo.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.demo.model.vo.Dev;

@Repository
public class DemoDaoImpl implements DemoDao {
	
	//pom.xml에 추가 #7
	@Autowired
	private SqlSessionTemplate session;

	@Override
	public int insertDev(Dev dev) {
		
		return session.insert("demo.insertDev", dev);
	}

	@Override
	public List<Dev> selectDevList() {
		
		return session.selectList("demo.selectDevList");
	}

}
