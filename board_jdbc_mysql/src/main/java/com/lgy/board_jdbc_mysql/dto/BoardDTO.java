package com.lgy.board_jdbc_mysql.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
	private int boardNo;
	private String	boardName;
	private String	boardTitle;
	private String	boardContent;
	private Timestamp	boardDate;
	private int boardHit;
}



