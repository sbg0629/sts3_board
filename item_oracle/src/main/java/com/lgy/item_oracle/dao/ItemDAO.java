package com.lgy.item_oracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.lgy.item_oracle.dto.ItemDTO;

public class ItemDAO {
	DataSource dataSource;
	public ItemDAO() {
		try {
			Context ctx = new InitialContext();
//			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql");
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public void write(String name, String price, String description) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		String sql="insert into item(name, price"
				 + ", description) values(?,?,?)";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, name);
			pstmt.setString(2, price);
			pstmt.setString(3, description);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public ArrayList<ItemDTO> list() {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="select name, price, description from item";
		ArrayList<ItemDTO> dtos=new ArrayList<ItemDTO>();
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String name = rs.getString("name");
				int price = rs.getInt("price");
				String description = rs.getString("description");
				
				ItemDTO dto = new ItemDTO(name, price, description);
				dtos.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return dtos;
	}
	
}






















