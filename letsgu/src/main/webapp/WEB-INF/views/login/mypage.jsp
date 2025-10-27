<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<script>

</script>
</head>
<body>
  <h1>마이페이지</h1>
<button type="button" onclick="location.href='${pageContext.request.contextPath}/letsgu/main'">메인페이지</button>
이름: ${user.name} <br>
이메일: ${user.email} <br>
성별: ${user.gender} <br>
연령대: ${user.agegroup} <br>
<a href="${pageContext.request.contextPath}/letsgu/logout">로그아웃</a>
</body>
</html>
