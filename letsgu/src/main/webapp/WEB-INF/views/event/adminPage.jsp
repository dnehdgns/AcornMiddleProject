<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
</head>
<body>
	<h2>비활성화된 이벤트 목록</h2>

	<form
		action="${pageContext.request.contextPath}/letsgu/admin/list"
		method="post">

		<c:choose>
			<c:when test="${empty inactiveList}">
				<p>비활성화 이벤트가 없습니다.</p>
				<button type="submit" disabled>전체삭제</button>
			</c:when>

			<c:otherwise>
				<table>
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>지역</th>
						<th>상태</th>
						<th>등록일</th>
						<th>이미지</th>
					</tr>

					<c:forEach var="inactive" items="${inactiveList}">
						<tr>
							<td>
								<a href="${pageContext.request.contextPath}/letsgu/event/eventdetail?eventId=${inactive.eventId}">
									${inactive.eventId}
								</a>
							</td>
							<td>${inactive.title}</td>
							<td>${inactive.region}</td>
							<td>${inactive.status}</td>
							<td>${inactive.createdAt}</td>
							<td>
							<c:if test="${not empty inactive.uploadImg}">
									<img alt="이벤트 이미지"
										src="${pageContext.request.contextPath}/upload/${inactive.uploadImg}"
										width="100" height="80">
							</c:if>
							</td>
						</tr>
					</c:forEach>
				</table>

				<button type="submit"
					onclick="return confirm('비활성화된 이벤트를 모두 삭제하시겠습니까?')">전체삭제</button>
			</c:otherwise>
		</c:choose>

	</form>

</body>
</html>