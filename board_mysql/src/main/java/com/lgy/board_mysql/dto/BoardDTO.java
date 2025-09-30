package com.lgy.board_mysql.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardDTO {
	private int boardNo;
	private String	boardName;
	private String	boardTitle;
	private String	boardContent;
	private Timestamp	boardDate;
	private int boardHit;
}



