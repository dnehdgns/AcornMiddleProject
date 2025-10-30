<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<style>
/* =========================
   0) 컬러 토큰
   ========================= */
:root{
  --main-orange:#FF7E36;
  --light-orange:#FFF5E6;
  --gray-100:#F5F5F5;
  --gray-300:#ddd;
  --gray-500:#999;
  --text-main:#333;
  --radius-2:12px;
  --radius-3:18px;
  --radius-4:24px;
  --shadow-md:0 8px 20px rgba(0,0,0,.06);
}
*{box-sizing:border-box}
html{font-size:62.5%}
body{
  margin:0;
  font-family:'Noto Sans KR', system-ui, -apple-system, Segoe UI, Roboto, Helvetica, Arial, sans-serif;
  color:var(--text-main);
  background:#fff; /* 배경 신경쓰지 말기: 순백 */
}
/* =========================
   4) 카드 그리드 — 블록 분리
   ========================= */
.main{
	width :1240px;
	justify-self: anchor-center;
}
.grid{
  display:grid; gap:14px; margin-top:100px; margin-bottom:150px;
  grid-template-columns: 1.2fr 1fr;
}
@media (max-width:840px){ .grid{ grid-template-columns:1fr } }
.card{
  background:#fff;
  border:1px solid var(--gray-300);
  border-radius:var(--radius-4);
  box-shadow:var(--shadow-md);
  padding:50px;
  height: auto;
}
.card .title{ font-size:1.6rem; font-weight:700; margin-bottom:12px }
/* 서비스 리스트 */
.service-list{ display:flex; flex-direction:column; gap:10px }
.service-item{
  display:flex; align-items:center; gap:12px;
  padding:12px 14px; font-size:1.4rem;
  background:var(--gray-100);
  border:1px solid var(--gray-300); border-radius:16px;
}
.service-item .ico{ font-size:2rem }
/* 강조 카드(우측) */
.card.accent{
  background:var(--light-orange);
  border-color:var(--main-orange);
}
/* =========================
   5) 프로필(데이터 블록)
   ========================= */
.profile{
 gap:80px; align-items:center; margin-top:14px;     place-self: center;
}
.avatar{
  width:200px; height:200px; border-radius:50%;
  object-fit:cover; border:2px solid var(--gray-300);
}
.meta{ font-size:1.4rem }
.meta p{ margin:2px 0 }
.badges{ display:flex; gap:6px; margin-top:6px }
.badge{
  font-size:1.2rem; padding:6px 10px; border-radius:999px;
  background:var(--gray-100); border:1px solid var(--gray-300);
}
/* =========================
   6) 버튼(포인트 컬러)
   ========================= */
.actions{    margin-top: 14px;
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    align-content: space-between;
    justify-content: flex-end;
    }
.btn{
  display:inline-flex; align-items:center; justify-content:center;
  gap:6px; padding:10px 16px; border-radius:999px;
  border:1px solid var(--main-orange);
  background:var(--main-orange); color:#fff;
  font-size:1.35rem; cursor:pointer;
}
.btn.line{
  background:#fff; color:var(--main-orange); border-color:var(--main-orange); width: 100%;
}
.btn.gray{
  background:#fff; color:var(--text-main); border-color:var(--gray-300);
}
</style>
</head>
<body>
<!-- ===== HEADER (전용 클래스 사용) ===== -->
<header>
	<%@ include file="/WEB-INF/views/common/header.jsp" %>
</header>
<!-- ===== 본문 ===== -->
<div class="main">
  <!-- 블록 2: 카드 그리드 -->
  <section class="grid">
    <!-- 좌측 카드: 서비스 + 프로필 -->
    <div class="card">
     <!-- 프로필 블록 -->
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
          <strong style="font-size:1.6rem">${user.name}</strong>
          <p>아이디: ${user.id}</p>
          <p>이메일: ${user.email}</p>
          <div class="badges">
            <span class="badge">성별: ${user.gender}</span>
            <span class="badge">연령대: ${user.agegroup}</span>
          </div>
        </div>
      </div>
      <!-- 권한별 버튼 블록 -->
      <div class="actions">
        <c:if test="${sessionScope.RULE eq 'ADMIN'}">
          <button class="btn" onclick="location.href='${pageContext.request.contextPath}/admin/dashboard'">관리자 페이지</button>
        </c:if>
        <c:if test="${sessionScope.RULE eq 'USER'}">
          <button class="btn line" onclick="location.href='${pageContext.request.contextPath}/letsgu/update'">프로필 수정</button>
        </c:if>
      </div>
    </div>
    <!-- 우측 카드: 포인트 강조(문구/CTA용) -->
   <div class="card accent" >
      <!-- <div class="bookmark-count">
 		 북마크한 수 : ${bookmarkCount}
	 </div> -->
      <div class="mypage-mycal" style="margin:0;">
    <iframe
	    src="${pageContext.request.contextPath}/letsgu/myCalendar?embed=1"
	      style="
    width: 50vw;
    height: 60vh;
    border: 0;
    overflow: hidden;
    transform: scale(0.7);
    display: block;" >
       </iframe>
      </div>
     <button class="btn"
        onclick="location.href='${pageContext.request.contextPath}/letsgu/myCalendar'">마이캘린더 보러가기
     </button>
    </div>
</section>
<!-- ===== 푸터 ===== -->
<footer>
  <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</footer>
</body>
</html>