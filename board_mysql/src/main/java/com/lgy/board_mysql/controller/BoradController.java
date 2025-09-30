package com.lgy.board_mysql.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lgy.board_mysql.service.BoardContentService;
import com.lgy.board_mysql.service.BoardDelecteService;
import com.lgy.board_mysql.service.BoardListService;
import com.lgy.board_mysql.service.BoardModifyService;
import com.lgy.board_mysql.service.BoardService;
import com.lgy.board_mysql.service.BoardWriteService;

import lombok.extern.slf4j.Slf4j;

@Controller //컨트롤정의
@Slf4j //log 확인
public class BoradController {
//	BoardService.java 인터페이스
	BoardService service;
	
	
	//게시판 목록 조회
	@RequestMapping("/list")
	public String list(Model model) {
		log.info("@# list()");
		
		//service(command)단 호출
//		BoardListService service = new BoardListService();
		service = new BoardListService();
		service.execute(model);
		return "list";
	}
	@RequestMapping("/write")
	//request : 뷰에서 값을 받아옴
	public String write(HttpServletRequest request, Model model) {
		log.info("@# write()");
		
		model.addAttribute("request",request); //""에 있는건 writeservice에 있는 request ,아닌 것 위에 꺼 
		//service(command)단 호출
//		BoardWriteService service = new BoardWriteService();
		service = new BoardWriteService();
//		model안에 request 담겨있음
		service.execute(model);
		return "redirect:list";
	}
	@RequestMapping("/write_view")
	public String write() {
		log.info("@# write_view()");
		return "write_view";
		
	}
	@RequestMapping("/content_view")
	public String content_view(HttpServletRequest request, Model model) {
		log.info("@# content_view()");
		
		model.addAttribute("request",request);
		service = new BoardContentService();
		service.execute(model);
		return "content_view";
	}
	@RequestMapping("/modify")
	public String modify(HttpServletRequest request, Model model) {
		log.info("@# modify()");
		
		model.addAttribute("request",request);
		service = new BoardModifyService();
		service.execute(model);
		return "redirect:list";
	}
	@RequestMapping("/delect")
	public String delect(HttpServletRequest request, Model model) {
		log.info("@# delect()");
		
		model.addAttribute("request",request);
		service = new BoardDelecteService();
		service.execute(model);
		return "redirect:list";
	}
}
