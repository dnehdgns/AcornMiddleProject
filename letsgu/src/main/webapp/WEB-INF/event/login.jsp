<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>

<h2>테스트 로그인 페이지</h2>
<form action="${pageContext.request.contextPath}/letsgu/login" method="post">
    <p>
        로그인할 user_id 입력 (1~5): 
        <input type="number" name="user_id" required>
    </p>
    <button type="submit">로그인</button>
</form>

</body>
</html>
