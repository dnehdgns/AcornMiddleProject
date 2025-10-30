<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>메인페이지</title>
<style>
/* ===== 색상 토큰 ===== */
:root {
  --main-orange: #FF7E36;
  --light-orange: #FFF5E6;
  --gray-100: #F5F5F5;
  --gray-300: #ddd;
  --gray-500: #999;
  --text-main: #333;
  --radius-2: 10px;
  --radius-3: 14px;
  --radius-4: 22px;
  --shadow-md: 0 8px 20px rgba(0, 0, 0, 0.06);
}
/* ===== 전체 기본 설정 ===== */
* { box-sizing: border-box; }
html { font-size: 62.5%; }
body {
  font-family: 'Noto Sans KR', sans-serif;
  background-color: var(--gray-100);
  color: var(--text-main);
  margin: 0;
}
r { filter: brightness(0.96); }

/* ===== 플로팅 배지 ===== */
.floating-calendar-badge {
  position: fixed;
  top: 15rem;
  right: 10rem;
  background-color: var(--main-orange);
  color: #fff;
  font-size: 1.5rem;
  font-weight: 600;
  text-decoration: none;
  padding: 1.5rem 2rem;
  border-radius: 999px;
  box-shadow: var(--shadow-md);
  display: flex;
  align-items: center;
  transition: all 0.25s ease;
  z-index: 1000;
}
.floating-calendar-badge:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(255, 126, 54, 0.3);
  filter: brightness(0.95);
}

/* =========================================================
   2) MAIN (본문 전용)
   ========================================================= */
main {
  text-align: center;
  padding: 55px 16px 40px;
  max-width: 1080px;
  margin: 0 auto;
  height :58vh;
}
.text p {
  font-size: 2.4rem;
  font-weight: 700;
  margin: 4px 0;
}
#changingText { color: var(--main-orange); font-size: 4rem;: }
/* 검색 섹션 (흰색 박스) */
.search-wrap {
  margin-top: 18px;
  padding: 16px;
  background: #fff;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-4);
  box-shadow: var(--shadow-md);
}
.search {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}
.search select,
.search input,
.search button {
  height: 40px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-2);
  font-size: 1.5rem;
  box-sizing: border-box;
}
.search select { padding: 0 10px; }
.search input { width: 260px; padding: 0 12px; }
.search button {
  padding: 0 16px;
  background-color: var(--main-orange);
  color: #fff;
  border: 1px solid var(--main-orange);
  cursor: pointer;
  border-radius: 999px;
}
.search button:hover { filter: brightness(0.96); }
/* 메뉴 리스트 */
.menuListSection { margin-top: 28px; }
.MenuList {
  list-style: none;
  display: flex;
  justify-content: center;
  gap: 12px;
  padding: 0;
  margin: 0;
  flex-wrap: wrap;
}
.menu {
  width: 130px;
  height: 70px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fff;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-3);
  box-shadow: var(--shadow-md);
  transition: transform 0.08s ease, border-color 0.2s ease, box-shadow 0.2s ease;
}
.menu:hover {
  border-color: var(--main-orange);
  box-shadow: 0 8px 20px rgba(255, 126, 54, 0.18);
  transform: translateY(-1px);
}
.menu a {
  text-decoration: none;
  color: var(--text-main);
  font-weight: 700;
  font-size: 1.5rem;
}
/* 반응형 */
@media (max-width: 520px) {
  .search { flex-direction: column; align-items: stretch; }
  .search input { width: 100%; }
}
</style>
</head>
<body>
<!-- ===== 헤더 (전용 클래스 사용) ===== -->
<header>
	<%@ include file="/WEB-INF/views/common/header.jsp" %>
</header>
<!-- ===== 메인 ===== -->
<main>

   <a href="${pageContext.request.contextPath}/letsgu/mainCalendar" class="floating-calendar-badge">
     <span>  🗓️ 메인 캘린더</span>
   </a>
	 
  <div class="text">
    <p>LET'S GU에서</p>
    <p id="changingText">레츠고</p>
    <p>찾고 계신가요?</p>
  </div>
  <!-- 검색 영역 -->
  <div class="search-wrap">
    <div class="search">
      <select name="region">
        <option value="">지역 선택</option>
        <option>서울</option>
        <option>부산</option>
        <option>대전</option>
        <option>광주</option>
      </select>
		<form action="${pageContext.request.contextPath}/letsgu/event/list" method="get">
		  <input type="hidden" name="region" value="전체">
		  <input type="text" name="keyword" class="keyword" placeholder="검색어를 입력하세요">
		  <button type="submit">검색</button>
		</form>

    </div>
  </div>
  <!-- 메뉴 리스트 -->

	<div class="menuListSection">
		<div class="MenuList">
			<button class="menu"
				onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=강남구'">강남구</button>
			<button class="menu"
				onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=서초구'">서초구</button>
			<button class="menu"
				onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=마포구'">마포구</button>
			<button class="menu"
				onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=송파구">송파구</button>
			<button class="menu"
				onclick="location.href='${pageContext.request.contextPath}/letsgu/event/list?region=용산구'">용산구</button>
		</div>
	</div>

</main>
<!-- ===== 푸터 ===== -->
<footer>
  <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</footer>
<script>
const texts = ["취미 모임", "동아리", "이벤트", "클럽 활동", "친목 모임", "스터디 그룹", "지역 모임", "오프라인 모임", "커뮤니티", "프로젝트 팀"];
let index = 0;
setInterval(() => {
  index = (index + 1) % texts.length;
  document.getElementById("changingText").innerText = texts[index];
}, 1500);
</script>
</body>
</html>