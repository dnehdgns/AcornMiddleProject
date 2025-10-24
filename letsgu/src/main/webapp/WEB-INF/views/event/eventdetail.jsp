<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이벤트 상세보기</title>
</head>
<body>

<h2>이벤트 상세보기</h2>

<!-- 이미지 -->
<c:if test="${not empty event.uploadImg}">
    <p><img src="${pageContext.request.contextPath}/upload/${event.uploadImg}" width="300"></p>
</c:if>

<!-- 제목 -->
<h3>${event.title}</h3>

<!-- 글쓴이 -->
<p>작성자: ${event.authorName}</p>

<!-- 카테고리 -->
<p>카테고리: ${event.categoryName}</p>

<!-- 지역 -->
<p>지역: ${event.region}</p>

<!-- 내용 -->
<p>내용:<br>${event.description}</p>

<!-- 모집 관련 정보 -->
<c:if test="${not empty event.eventDate}">
    <p>모집 종료일: 
        <fmt:formatDate value="${event.eventDate}" pattern="yyyy-MM-dd"/>
    </p>
</c:if>

<c:if test="${event.capacity > 0}">
    <p>모집 인원: ${event.capacity} 명</p>
</c:if>

<!-- 작성일자 -->
<p>작성일자: 
    <fmt:formatDate value="${event.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
</p>

<hr>

<!-- 수정 / 삭제 버튼 (작성자만 보이게) -->
<c:if test="${loginId != null && loginId.user_id == event.authorId}">
    <a href="${pageContext.request.contextPath}/letsgu/event/update?eventId=${event.eventId}">
        <button>수정</button>
    </a>


    <form action="${pageContext.request.contextPath}/letsgu/event/delete" method="post" style="display:inline;">
        <input type="hidden" name="eventId" value="${event.eventId}">
        <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
    </form>

</c:if>

<!-- 목록으로 -->
<p><a href="${pageContext.request.contextPath}/letsgu/event/list">목록으로</a></p>

</body>
</html>
