package com.lgy.board_mysql.service;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.lgy.board_mysql.dao.BoardDAO;
import com.lgy.board_mysql.dto.BoardDTO;

//게시판 목록 조회
public class BoardListService implements BoardService {

	@Override
	public void execute(Model model) {
		//dao 단 호출
		BoardDAO dao = new BoardDAO();
		//게시글들을 dtos 객체로 받음
		ArrayList<BoardDTO> dtos = dao.list();
		//dtos 객체를 list 이름으로 컨트롤러단으로 전송
		model.addAttribute("list",dtos);
	}
}
