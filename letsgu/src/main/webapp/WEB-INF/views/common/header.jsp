<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
/* =========================================================
   1) HEADER ONLY (전용 네임스페이스: .site-header ...)
   ========================================================= */
.site-header {
  background-color: var(--gray-100);
  border-bottom: 1px solid var(--gray-300);
  padding: 0px 16px;
}
.site-header__inner {
  max-width: 1080px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.site-header__brand { display: flex; align-items: center; gap: 10px; }
.site-header__logo { height: 100px; display: block; }
.site-header__actions { display: flex; gap: 8px; flex-wrap: wrap; justify-content: flex-end; }
/* 헤더 전용 버튼 */
.site-header__btn {
  height: 36px;
  padding: 0 14px;
  border-radius: 999px;
  border: 1px solid var(--main-orange);
  font-size: 1.3rem;
  cursor: pointer;
  transition: filter .2s ease, background-color .2s ease, border-color .2s ease;
}
.site-header__btn--primary { background: var(--main-orange); color: #fff; }
.site-header__btn--line { background: #fff; color: var(--main-orange); }
.site-header__btn:hover { filter: brightness(0.96); }
</style>
</head>
<body>
	<!-- ===== 헤더 (전용 클래스 사용) ===== -->
<header class="site-header">
  <div class="site-header__inner">
    <div class="site-header__brand">
      <a href='${pageContext.request.contextPath}/letsgu/main' ><img class="site-header__logo" src="${pageContext.request.contextPath}/images/letsgu.png" alt="로고"></a>
    </div>
    <div class="site-header__actions">
      <c:choose>
        <c:when test="${RULE eq 'ADMIN'}">
          <button class="site-header__btn site-header__btn--primary"
                  onclick="location.href='${pageContext.request.contextPath}/admin/dashboard'">관리자 페이지</button>
          <button class="site-header__btn site-header__btn--line">회원 관리</button>
        </c:when>
        <c:when test="${RULE eq 'USER'}">
          <button class="site-header__btn site-header__btn--line"
                  onclick="location.href='${pageContext.request.contextPath}/letsgu/mypage'">마이페이지</button>
          <button class="site-header__btn site-header__btn--primary"
                  onclick="location.href='${pageContext.request.contextPath}/letsgu/logout'">로그아웃</button>
        </c:when>
        <c:otherwise>
			<c:if test="${empty LOGIN_ID}">
			  <c:choose>
			    <c:when test="${not empty eventId}">
			      <button class="site-header__btn site-header__btn--line"
			              onclick="location.href='${pageContext.request.contextPath}/letsgu/login?eventId=${eventId}'">
			        로그인
			      </button>
			    </c:when>
			    <c:when test="${not empty region}">
			      <button class="site-header__btn site-header__btn--line"
			              onclick="location.href='${pageContext.request.contextPath}/letsgu/login?region=${region}'">
			        로그인
			      </button>
			    </c:when>
			    <c:otherwise>
			      <button class="site-header__btn site-header__btn--line"
			              onclick="location.href='${pageContext.request.contextPath}/letsgu/login'">
			        로그인
			      </button>
			    </c:otherwise>
			  </c:choose>
			
			  <button class="site-header__btn site-header__btn--primary"
			          onclick="location.href='${pageContext.request.contextPath}/letsgu/signup'">
			    회원가입
			  </button>
			</c:if>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</header>
</body>
</html>