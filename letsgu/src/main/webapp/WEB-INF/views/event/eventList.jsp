<%@page import="event.PageHandler"%>
<%@page import="event.Category"%>
<%@page import="event.Event"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>조회하기</title>
</head>
<body>

	<div>
		<a href="${pageContext.request.contextPath}/letsgu/event/reg">게시글
			등록</a>
	</div>

	<div>
		<a href="${pageContext.request.contextPath}/letsgu/admin/list">관리자 페이지</a>
	</div>

	<div>
		<h3>지역</h3>
		<ul>
			<li><a
				href="${pageContext.request.contextPath}/letsgu/event/list?region=전체&category=${param.category}">전체
					보기</a></li>

			<c:forEach var="region" items="${regionList}">
				<li><a
					href="${pageContext.request.contextPath}/letsgu/event/list?region=${region}&category=${param.category}">${region}</a>
				</li>
			</c:forEach>
		</ul>
	</div>

	<div>
		<h3>카테고리</h3>
		<ul>
			<li><a
				href="${pageContext.request.contextPath}/letsgu/event/list?region=${param.region}&category=">전체
					보기</a></li>

			<c:forEach var="category" items="${categoryList}">
				<li><a
					href="${pageContext.request.contextPath}/letsgu/event/list?region=${param.region}&category=${category.categoryId}">${category.categoryName}</a>
				</li>
			</c:forEach>
		</ul>
	</div>

	<h2>
		<c:choose>
			<c:when test="${not empty param.category}">
            ${eventList[0].categoryName} 카테고리 이벤트 목록
        </c:when>
			<c:otherwise>
            전체 이벤트 목록
        </c:otherwise>
		</c:choose>
	</h2>

	<table>
		<tr>
			<th>번호</th>
			<th>카테고리</th>
			<th>제목</th>
			<th>지역</th>
			<th>모집일</th>
			<th>상태</th>
			<th>사진</th>
		</tr>

		<c:forEach var="event" items="${eventList}">
			<tr>
				<td><a
					href="${pageContext.request.contextPath}/letsgu/event/eventdetail?eventId=${event.eventId}">
						${event.eventId} </a></td>
				<td>${event.categoryName}</td>
				<td>${event.title}</td>
				<td>${event.region}</td>
				<td>${event.eventDate}</td>
				<td>${event.status}</td>
				<td><c:if test="${not empty event.uploadImg}">
						<img alt="이벤트 이미지"
							src="${pageContext.request.contextPath}/upload/${event.uploadImg}"
							width="100" height="80">
					</c:if></td>
			</tr>
		</c:forEach>
	</table>

	<div>
		<c:if test="${empty param.category}">
			<c:if test="${page.currentGrp > 1}">
				<a
					href="${pageContext.request.contextPath}/letsgu/event/list?p=${page.grpStart - 1}">이전</a>
			</c:if>

			<c:forEach var="i" begin="${page.grpStart}" end="${page.grpEnd}">
				<a
					href="${pageContext.request.contextPath}/letsgu/event/list?p=${i}">${i}</a>
			</c:forEach>

			<c:if test="${page.grpEnd < page.totalPage}">
				<a
					href="${pageContext.request.contextPath}/letsgu/event/list?p=${page.grpEnd + 1}">다음</a>
			</c:if>
		</c:if>

	</div>

</body>
</html>