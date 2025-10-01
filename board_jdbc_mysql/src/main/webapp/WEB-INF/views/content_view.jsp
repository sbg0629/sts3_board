<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table width ="500" border ="1">
			<form method ="post" action="modify">
<!-- 		없으면	java.lang.NumberFormatException: null -->
<!-- 			no값을 넘겨 줘야함 -->
			<input type="hidden" name="boardNo" value="${content_view.boardNo}">
				<tr>
					<td>번호</td>
					<td>
						${content_view.boardNo}
					</td>
				</tr>
				<tr>
					<td>히트</td>
					<td>
						${content_view.boardHit}
					</td>
				</tr>
				<tr>
					<td>이름</td>
					<td>
<%-- 						${content_view.boardName} 수정 사용 전--%>
						<input type="text" name="boardName" value="${content_view.boardName}">
					</td>
				</tr>
				<tr>
					<td>제목</td>
					<td>
<%-- 						${content_view.boardTitle} 수정 사용 전--%>
						<input type="text" name="boardTitle" value="${content_view.boardTitle}">
					</td>
				</tr>
				<tr>
					<td>내용</td>
					<td>
<%-- 					${content_view.boardContent} 수정 사용 전--%>
						<input type="text" name="boardContent" value="${content_view.boardContent}">
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input type="submit" value="수정">
						&nbsp;&nbsp;<a href="list">목록보기</a>
						&nbsp;&nbsp;<a href="delect?boardNo=${content_view.boardNo}">삭제</a>
					</td>
				</tr>
			</form>
	</table>
</body>
</html>