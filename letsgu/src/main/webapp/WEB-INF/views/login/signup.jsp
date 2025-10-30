<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>

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

/* ===== 리셋/기본 ===== */
*{ box-sizing: border-box; }
html{ font-size:62.5%; }
body{
  font-family:'Noto Sans KR', sans-serif;
  color: var(--text-main);
  margin:0;
  background: var(--gray-100);
}

/* ===== 페이지 레이아웃: 헤더/메인/푸터 컬럼 ===== */
.page{
  min-height:100vh;
  display:flex;
  flex-direction:column;
}

/* =========================
   2) MAIN ONLY (폼 카드)
   ========================= */
main{ 
  flex:1; 
  display:flex; 
  align-items:center; 
  justify-content:center; 
  padding: 24px; 
}
form{
  background:#fff;
  width: 380px;
  padding: 32px 40px;
  border-radius: var(--radius-4);
  border: 1px solid var(--gray-300);
  box-shadow: var(--shadow-md);
}
h1{
  text-align:center;
  color: var(--main-orange);
  margin: 0 0 24px;
  font-size: 2.2rem;
  font-weight: 800;
}
form div{ margin-bottom:18px; }
form div:last-child{ margin-top:26px; display:flex; gap:4%; }

/* 라벨 */
form label{
  display:block; font-weight:600; margin-bottom:6px; color:#555; font-size:1.4rem;
}

/* 인풋/셀렉트 */
input[type="text"], input[type="password"], input[type="email"], select{
  width:100%; padding:10px 12px; border:1px solid var(--gray-300);
  border-radius: var(--radius-2); font-size:1.4rem; background:#fff;
  transition: border-color .2s ease, box-shadow .2s ease;
}
input::placeholder{ color: var(--gray-500); }
input:focus, select:focus{
  border-color: var(--main-orange);
  outline: none;
  box-shadow: 0 0 0 3px var(--light-orange);
}

/* 버튼 */
button{
   height:42px; border-radius:999px; font-size:1.5rem; font-weight:600;
  cursor:pointer; transition: filter .2s ease, background-color .2s ease; border:1px solid transparent;
}
button[type="submit"]{ background: var(--main-orange); color:#fff; border-color: var(--main-orange);    width: 48%; }
button[type="submit"]:hover{ filter: brightness(.96)  ;    }
button[type="button"]{ background:#fff; color: var(--main-orange); border-color: var(--main-orange); width: 48%;}
button[type="button"]:hover{ background: var(--light-orange); }


</style>

<script>
var JoinForm__submitDone = false;
function Joinform__submit(form) {
  if (JoinForm__submitDone) {
    alert('처리중입니다.');
    return false;
  }
  form.login_id.value  = form.login_id.value.trim();
  form.password.value  = form.password.value.trim();
  form.password2.value = form.password2.value.trim();
  form.username.value  = form.username.value.trim();
  form.email.value     = form.email.value.trim();

  if (form.login_id.value.length === 0) { alert('아이디를 입력해주세요.'); form.login_id.focus(); return false; }
  if (form.password.value.length === 0) { alert('비밀번호를 입력해주세요.'); form.password.focus(); return false; }
  if (form.password.value !== form.password2.value) { alert('비밀번호가 일치하지 않습니다.'); form.password2.focus(); return false; }
  if (form.username.value.length === 0) { alert('이름을 입력해주세요.'); form.username.focus(); return false; }
  if (form.email.value.length === 0) { alert('이메일을 입력해주세요.'); form.email.focus(); return false; }
  if (form.age_group.value.length === 0) { alert('연령대를 선택해주세요.'); form.age_group.focus(); return false; }
  if (form.gender.value.length === 0) { alert('성별을 선택해주세요.'); form.gender.focus(); return false; }

  JoinForm__submitDone = true;   // 중복 제출 방지
  return true;                   // 실제 제출 허용
}
</script>
</head>

<body>
<div class="page"><!-- 레이아웃 래퍼 시작 -->

 <!-- ===== HEADER (전용 클래스 사용) ===== -->
<header>
	<c:import url="/letsgu/header"/>
</header>

  <!-- ===== 메인(폼) ===== -->
  <main>
    <form action="${pageContext.request.contextPath}/letsgu/signup"
          method="post"
          onsubmit="return Joinform__submit(this);">

      <h1>회원가입</h1>

      <div>
        <label for="login_id">아이디</label>
        <input id="login_id" name="login_id" type="text" placeholder="아이디를 입력해주세요." autocomplete="off" />
      </div>

      <div>
        <label for="password">비밀번호</label>
        <input id="password" name="password" type="password" placeholder="비밀번호를 입력해주세요." autocomplete="off" />
      </div>

      <div>
        <label for="password2">비밀번호 확인</label>
        <input id="password2" name="password2" type="password" placeholder="비밀번호를 다시 입력해주세요." autocomplete="off" />
      </div>

      <div>
        <label for="username">이름</label>
        <input id="username" name="username" type="text" placeholder="이름을 입력해주세요." autocomplete="off" />
      </div>

      <div>
        <label for="email">이메일</label>
        <input id="email" name="email" type="email" placeholder="이메일을 입력해주세요." autocomplete="off" />
      </div>

      <div>
        <label for="age_group">연령대</label>
        <select id="age_group" name="age_group">
          <option value="">선택</option>
          <option value="10대">10대</option>
          <option value="20대">20대</option>
          <option value="30대">30대</option>
          <option value="40대">40대</option>
          <option value="50대 이상">50대 이상</option>
        </select>
      </div>

      <div>
        <label for="gender">성별</label>
        <select id="gender" name="gender">
          <option value="">선택</option>
          <option value="M">남자</option>
          <option value="F">여자</option>
        </select>
      </div>

      <div>
		<button type="submit" class="btn btn--primary">가입</button>
        <button type="button" class="btn btn--line"
              onclick="location.href='${pageContext.request.contextPath}/letsgu/main'">취소</button>
      </div>
    </form>
  </main>

<!-- ===== 푸터 ===== -->
<footer>
  <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</footer>


</div><!-- /.page -->
</body>
</html>
