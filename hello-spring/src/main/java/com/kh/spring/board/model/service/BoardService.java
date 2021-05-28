package com.kh.spring.board.model.service;

import java.util.List;
import java.util.Map;

import com.kh.spring.board.model.vo.Attachment;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardExt;


public interface BoardService {

	List<Board> selectBoardList();

	List<Board> selectBoardList(Map<String, Object> param);

	int selectBoardCount();

	int insertBoard(BoardExt board);
	
	int insertAttachment(Attachment attach);

	BoardExt selectOneBoard(int no);
	
	List<Attachment> selectAttachmentList(int no);

	BoardExt selectOneBoardCollection(int no);

	Attachment selectOneAttachment(int no);

	List<BoardExt> searchBoardList(String search);
}
