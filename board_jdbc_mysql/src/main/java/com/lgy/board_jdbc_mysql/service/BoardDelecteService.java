package com.lgy.board_jdbc_mysql.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.lgy.board_jdbc_mysql.dao.BoardDAO;


public class BoardDelecteService implements BoardService {

	@Override
	public void execute(Model model) {

		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		
		String boardNo = request.getParameter("boardNo");
		
		BoardDAO dao = new BoardDAO();
		dao.delect(boardNo);
//		model.addAttribute("content_view",dto); //바로 삭제 해서 들고 갈 필요가 없음
	

}
}
