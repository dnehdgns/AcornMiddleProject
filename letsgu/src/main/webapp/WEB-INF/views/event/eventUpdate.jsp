<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이벤트 수정</title>
</head>
<body>

<h2>이벤트 수정</h2>

<form action="${pageContext.request.contextPath}/letsgu/event/update" 
      method="post" enctype="multipart/form-data">

    <input type="hidden" name="eventId" value="${event.eventId}">
    <input type="hidden" name="oldUploadImg" value="${event.uploadImg}">

    <p>
        카테고리:
        <select name="category_id" required>
            <option value="">선택하세요</option>
            <c:forEach var="c" items="${categoryList}">
                <option value="${c.categoryId}" 
                    <c:if test="${c.categoryId == event.categoryId}">selected</c:if>>
                    ${c.categoryName}
                </option>
            </c:forEach>
        </select>
    </p>

    <p>
        제목: 
        <input type="text" name="title" value="${event.title}" required>
    </p>

    <p>
        지역:
        <select name="region" required>
            <option value="">선택하세요</option>
            <c:forEach var="r" items="${regionList}">
                <option value="${r}" <c:if test="${r == event.region}">selected</c:if>>${r}</option>
            </c:forEach>
        </select>
    </p>

    <p>
        날짜: 
        <input type="date" name="event_date" value="${event.eventDate}">
    </p>

    <p>
        모집인원: 
        <input type="number" name="capacity" value="${event.capacity}">
    </p>

    <p>
        내용:<br>
        <textarea name="description" rows="5" cols="40">${event.description}</textarea>
    </p>

    <p>
        기존 이미지: 
        <c:if test="${not empty event.uploadImg}">
            <img src="${pageContext.request.contextPath}/upload/${event.uploadImg}" 
                 alt="이전 이미지" width="120">
        </c:if>
    </p>

    <p>
        새 이미지 업로드: 
        <input type="file" name="uploadImg" accept="image/*">
    </p>

    <p>
        <button type="submit">수정하기</button>
        <a href="${pageContext.request.contextPath}/letsgu/event/eventdetail?eventId=${event.eventId}">취소</a>
    </p>
</form>

</body>
</html>
