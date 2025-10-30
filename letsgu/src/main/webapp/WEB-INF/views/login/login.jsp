<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>로그인</title>

<style>
/* ===== 색상 토큰 ===== */
:root{
  --main-orange: #FF7E36;
  --light-orange: #FFF5E6;
  --gray-100: #F5F5F5;
  --gray-300: #ddd;
  --gray-500: #999;
  --text-main: #333;

  --radius-2: 10px;
  --radius-3: 14px;
  --radius-4: 22px;
  --shadow-md: 0 8px 20px rgba(0,0,0,.06);
}

/* ===== 기본 설정 ===== */
*{ box-sizing: border-box; }
html{ font-size:62.5%; }
body{
  font-family:'Noto Sans KR', sans-serif;
  background-color: var(--gray-100);
  color: var(--text-main);
  margin:0;
  display:flex;
  flex-direction:column;
  min-height:100vh;
}


/* =========================================================
   2) MAIN / LOGIN FORM (본문 전용)
   ========================================================= */
main {
  flex:1;
  display:flex;
  justify-content:center;
  align-items:center;
  padding: 40px 16px;
}

/* 로그인 폼 카드 */
.login-card{
  background:#fff;
  width: 380px;
  padding: 32px 40px;
  border-radius: var(--radius-4);
  border: 1px solid var(--gray-300);
  box-shadow: var(--shadow-md);
}
.login-card__title{
  text-align: center;
  color: var(--main-orange);
  margin: 0 0 24px;
  font-size: 2.2rem;
  font-weight: 800;
}
.form-group{ margin-bottom: 18px; }
.form-label{
  display:block;
  font-weight:600;
  margin-bottom:6px;
  color:#555;
  font-size:1.4rem;
}
.form-input{
  width:100%;
  padding:10px 12px;
  border:1px solid var(--gray-300);
  border-radius: var(--radius-2);
  font-size:1.4rem;
  transition: border-color .2s ease, box-shadow .2s ease;
}
.form-input::placeholder{ color: var(--gray-500); }
.form-input:focus{
  border-color: var(--main-orange);
  outline:none;
  box-shadow:0 0 0 3px var(--light-orange);
}

/* 본문 버튼은 이 범위(.form-actions)에서만 스타일 */
.form-actions{
  margin-top: 26px;
  display: flex;
  justify-content: space-between;
  gap: 4%;
}
.form-actions .btn{
  width:48%;
  height:42px;
  border-radius:999px;
  font-size:1.5rem;
  font-weight:600;
  cursor:pointer;
  transition: filter .2s ease, background-color .2s ease, border-color .2s ease;
  border:1px solid transparent;
}
.btn--line{
  background:#fff;
  color: var(--main-orange);
  border-color: var(--main-orange);
}
.btn--line:hover{ background: var(--light-orange); }
.btn--primary{
  background: var(--main-orange);
  color:#fff;
  border-color: var(--main-orange);
}
.btn--primary:hover{ filter: brightness(.96); }



/* 반응형: 작은 화면 폼/버튼 줄바꿈 보정 */
@media (max-width: 420px){
  .login-card{ width:100%; padding:24px; }
  .form-actions{ flex-direction:column; }
  .form-actions .btn{ width:100%; }
}
</style>

<script>
var JoinForm__submitDone = false;
function Joinform__submit(form) {
  if (JoinForm__submitDone) {
    alert('처리중입니다.');
    return false;
  }

  form.LOGIN_ID.value = form.LOGIN_ID.value.trim();
  form.PASSWORD.value = form.PASSWORD.value.trim();

  if (form.LOGIN_ID.value.length === 0) {
    alert('아이디를 입력해주세요.');
    form.LOGIN_ID.focus();
    return false;
  }
  if (form.PASSWORD.value.length === 0) {
    alert('비밀번호를 입력해주세요.');
    form.PASSWORD.focus();
    return false;
  }

  JoinForm__submitDone = true;
  return true;
}
</script>
</head>

<body>
<!-- ===== HEADER (전용 클래스 사용) ===== -->
<header>
	<c:import url="/letsgu/header"/>
</header>

<!-- ===== MAIN ===== -->
<main>
<form action="${pageContext.request.contextPath}/letsgu/login" method="post">
  <input type="hidden" name="eventId" value="${eventId}" />
  <!-- 아이디/비밀번호 입력 -->
</form>
  <form class="login-card"
        action="${pageContext.request.contextPath}/letsgu/login"
        method="post"
        onsubmit="return Joinform__submit(this);">

    <h1 class="login-card__title">로그인</h1>

    <div class="form-group">
      <label class="form-label" for="login_id">아이디</label>
      <input class="form-input" id="login_id" name="LOGIN_ID" type="text"
             placeholder="아이디를 입력해주세요." autocomplete="off" />
    </div>

    <div class="form-group">
      <label class="form-label" for="password">비밀번호</label>
      <input class="form-input" id="password" name="PASSWORD" type="password"
             placeholder="비밀번호를 입력해주세요." autocomplete="off" />
    </div>

    <div class="form-actions">
    	<input type="hidden" name="eventId" value="${eventId}" />
      <button type="button" class="btn btn--line"
              onclick="location.href='${pageContext.request.contextPath}/letsgu/signup'">회원가입</button>
      <button type="submit" class="btn btn--primary">로그인</button>
    </div>
  </form>
</main>

<!-- ===== 푸터 ===== -->
<footer>
  <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</footer>
</body>
</html>
