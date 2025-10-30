<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/Css/base.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/Css/mypage.css">

</head>

<body>

<header>
	<%@ include file="/WEB-INF/views/common/header.jsp" %>
</header>

<div class="main">
   <div class="card-container">
   
    <div class="card" id="card1">
      <div class="profile">
        <c:choose>
          <c:when test="${not empty user.uploadimg}">
            <img class="avatar" src="${pageContext.request.contextPath}/uploads/${user.uploadimg}" alt="프로필">
          </c:when>
          <c:otherwise>
            <img class="avatar" src="${pageContext.request.contextPath}/images/default-profile.png" alt="기본 이미지">
          </c:otherwise>
        </c:choose>

        <div class="meta">
          <strong style="font-size:1.8rem">${user.name}</strong>
          <p>아이디: ${user.id}</p>
          <p>이메일: ${user.email}</p>
          <div class="badges">
            <span class="badge">성별: ${user.gender}</span>
            <span class="badge">연령대: ${user.agegroup}</span>
          </div>
        </div>
      </div>

      <div class="actions">
        <c:if test="${sessionScope.RULE eq 'ADMIN'}">
          <button class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/dashboard'">관리자 페이지</button>
        </c:if>
        <c:if test="${sessionScope.RULE eq 'USER'}">
          <button class="btn line" onclick="location.href='${pageContext.request.contextPath}/letsgu/update'">프로필 수정</button>
        </c:if>
      </div>
    </div>

    
    <div class="card accent" id="card2">
      <div class="mypage-mycal" style="margin:0;">
        <iframe 
          src="${pageContext.request.contextPath}/letsgu/myCalendar?embed=1">
        </iframe>
      </div>
      <button class="btn"
        onclick="location.href='${pageContext.request.contextPath}/letsgu/myCalendar'">마이캘린더 보러가기
      </button>
    </div>
    
   </div>
</div>

<footer>
  <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</footer>

</body>
</html>