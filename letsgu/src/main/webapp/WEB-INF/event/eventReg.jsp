<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이벤트 등록</title>
</head>
<body>

	<h2>이벤트 등록</h2>
	
	<form action="${pageContext.request.contextPath}/letsgu/event/reg" method="get">
    카테고리:
    <select name="category_id" onchange="this.form.submit()">
        <option value="">선택하세요</option>
        <c:forEach var="c" items="${categoryList}">
            <option value="${c.categoryId}" <c:if test="${c.categoryId == selectedCategory}">selected</c:if>>
                ${c.categoryName}
            </option>
        </c:forEach>
    </select>
</form>

	<form action="${pageContext.request.contextPath}/letsgu/event/reg"
		method="post" enctype="multipart/form-data">

		<input type="hidden" name="category_id" value="${selectedCategory}">

		<p>
        제목: <input type="text" name="title" required>
    </p>

    <p>
        지역:
        <select name="region" required>
            <option value="">선택하세요</option>
            <option value="강남구">강남구</option>
            <option value="마포구">마포구</option>
            <option value="서초구">서초구</option>
            <option value="종로구">종로구</option>
            <option value="용산구">용산구</option>
            <option value="은평구">은평구</option>
        </select>
    </p>

	<c:if test="${selectedCategory == 2}">
	    <p>모집날짜: <input type="date" name="event_date"></p>
	    <p>모집인원: <input type="number" name="capacity"></p>
	</c:if>
    <p>
        내용:<br>
        <textarea name="description" rows="5" cols="40"></textarea>
    </p>

    <p>
        이미지 업로드: <input type="file" name="uploadImg" accept="image/*">
    </p>

    <p>
        <button type="submit">등록하기</button>
    </p>
</form>


</body>
</html>
