package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardExt;

import lombok.extern.slf4j.Slf4j;

/*@Transactional(rollbackFor =Exception.class)*/
@Service
@Slf4j
public class BoardServiceImpl implements BoardService{
	@Autowired
	private BoardDao boardDao;

	@Override
	public List<Board> selectBoardList() {
		
		return boardDao.selectBoardList();
	}

	@Override
	public List<Board> selectBoardList(Map<String, Object> param) {
	
		return boardDao.selectBoardList(param);
	}

	@Override
	public int selectBoardCount() {
		
		return boardDao.selectBoardCount();
	}
	
	/**
	 * rollbakcFor - 트랜잭션 rollback처리하기위한 예외등록. Exception -> 모든 예외.
	 * 		기본적으로 RuntimeException만 rollback한다.
	 */
	//@Transactional(rollbackFor =Exception.class) 
	@Override
	public int insertBoard(BoardExt board) {
		int result = 0;
		//1. board 등록
		result = boardDao.insertBoard(board);
		
		//다오 인서트보드를 넘기면서 값이 지정됨
		//직접적으로 값을 넘기지는 않지만 같은 주소를 넘김
		log.debug("board = {}", board);
		
		//2. attachment 등록
		if(board.getAttachList().size()>0) {
			for(Attachment attach : board.getAttachList()) {
				//보드게시판에 등록된 시퀀스 번호를 가져와서 사용해야함
				//board no fk 세팅
				attach.setBoardNo(board.getNo());
				//아래의 메소드로 입력
				result = insertAttachment(attach);
			}
		}
		
		return result;
	}
	
	//@Transactional(rollbackFor =Exception.class)
	@Override
	public int insertAttachment(Attachment attach) {
		
		return boardDao.insertAttachment(attach);
	}

	@Override
	public BoardExt selectOneBoard(int no) {
		
		List<Attachment>list = selectAttachmentList(no);
		log.debug("list = {}", list);
		BoardExt board = boardDao.selectOneBoard(no);
		log.debug("board = {}", board);
		board.setAttachList(list);
		log.debug("board = {}", board);
		return board;
	}

	@Override
	public List<Attachment> selectAttachmentList(int no) {
		
		return boardDao.selectAttachmentList(no);
	}

	@Override
	public BoardExt selectOneBoardCollection(int no) {
		
		return boardDao.selectOneBoardCollection(no);
	}

	@Override
	public Attachment selectOneAttachment(int no) {
		
		return boardDao.selectOneAttachment(no);
	}

	@Override
	public List<BoardExt> searchBoardList(String search) {
		// TODO Auto-generated method stub
		return boardDao.searchBoardList(search);
	}

	
	
	
	
	
	
}
