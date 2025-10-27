<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인페이지</title>
<script>
	
</script>
</head>
<body>
	<h1>메인페이지</h1>

	<!-- 검색바 -->
	<form action="${pageContext.request.contextPath}/letsgu/event/search"
		method="get">
		<input type="text" name="keyword" placeholder="검색어를 입력하세요" required>
		<button type="submit">검색</button>
	</form>

	<!--버튼 -->
	<button type="button"
		onclick="location.href='${pageContext.request.contextPath}/letsgu/login'">로그인</button>
	<button type="button"
		onclick="location.href= '${pageContext.request.contextPath}/letsgu/mypage'">마이페이지</button>
	<button type="button"
		onclick="location.href= '${pageContext.request.contextPath}/letsgu/event/list'">상품
		리스트</button>

	<!-- 지역 버튼 -->
	<div>
		<button
			onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=강남구'">강남구</button>
		<button
			onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=서초구'">서초구</button>
		<button
			onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=마포구'">마포구</button>
		<button
			onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=송파구">송파구</button>
		<button
			onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=용산구'">용산구</button>
	</div>
</body>
</html>
