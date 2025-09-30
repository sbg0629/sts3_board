package com.lgy.board_mysql.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.lgy.board_mysql.dao.BoardDAO;

public class BoardWriteService implements BoardService{
	
	@Override
	public void execute(Model model) {
//		model 객체에서 꺼내서 전송
//		Dao에 있는 boardName , boardTitle , boardContent 값들이 필요
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		String boardName = request.getParameter("boardName");
		String boardTitle = request.getParameter("boardTitle");
		String boardContent = request.getParameter("boardContent");
		
		BoardDAO dao = new BoardDAO();
		dao.write(boardName, boardTitle, boardContent);
	}
}
