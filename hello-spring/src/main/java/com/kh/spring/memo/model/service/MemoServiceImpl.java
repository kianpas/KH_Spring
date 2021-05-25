package com.kh.spring.memo.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.memo.model.dao.MemoDao;
import com.kh.spring.memo.model.vo.Memo;

import lombok.extern.slf4j.Slf4j;

/**
 * 의존주입 받은 객체는 우리가 작성한 
 * MemoController, MemoServiceImpl, MemoDaoImple타입 객체가 아닌
 * proxy 객체이다.
 *
 * 1. jdk동적 proxy - interface구현체 - 현재는 인터페이스를 통과하므로 이것.
 * 2. cglib - interface구현체 아닌 경우. cglib 동적프록시 작동
 */
@Slf4j
@Service
public class MemoServiceImpl implements MemoService {
	
	@Autowired
	private MemoDao memoDao;

	@Override
	public int insertMemo(Memo memo) {
		
		return memoDao.insertMemo(memo);
	}

	@Override
	public List<Memo> selectMemoList() {
		//log.debug("dao = {}", memoDao.getClass());
		return memoDao.selectMemoList();
	}

	@Override
	public int deleteMemo(int no) {
		// TODO Auto-generated method stub
		return memoDao.deleteMemo(no);
	}
	
	
}
