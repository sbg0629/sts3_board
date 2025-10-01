<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table width ="500" border ="1">
		<tr>
			<td>번호</td>
			<td>이름</td>
			<td>제목</td>
			<td>날짜</td>
			<td>히트</td>
		</tr>
<!-- 		조회결과 -->
<!-- 			list: 모델 객체서 보낸 이름 (3번 줄 필요)-->
		<c:forEach var="dto" items="${list}">
			<tr>
				<td>${dto.boardNo}</td>
				<td>${dto.boardName}</td>
<%-- 				<td>${dto.boardTitle}</td>  상세 내용 걸기 전--%>
				<td>
<!-- 				컨트롤러단 호출한다 -->
				<a href="content_view?boardNo=${dto.boardNo}">${dto.boardTitle}</a>
				
				</td>
				<td>${dto.boardDate}</td>
				<td>${dto.boardHit}</td>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="5">
<!-- 			write view 컨트롤러단 호출 -->
				<a href="write_view">글 작성</a>
			</td>
		</tr>
	</table>
</body>
</html>