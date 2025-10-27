<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>comment</h2>

<form action="${pageContext.request.contextPath}/letsgu/event/commentadd" method="post">
  <input type="text" name="eventid" id="eventid" > <br>
  <input type="text" name="userid" id="userid"><br>
  
  <label for="content">댓글</label><br>
  <textarea id="content" name="content" rows="5" cols="50"></textarea><br>
  
  <button type="submit">등록</button>
</form>
</body>
</html>