<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>프로필 수정</title>

<style>
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
  --shadow-md: 0 8px 20px rgba(0,0,0,.06);
}

* { box-sizing: border-box; }
html { font-size: 62.5%; }
body {
  font-family: 'Noto Sans KR', sans-serif;
  color: var(--text-main);
  margin: 0;
  background: var(--gray-100);
}

.page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

main {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

form {
  background: #fff;
  width: 420px;
  padding: 32px 40px;
  border-radius: var(--radius-4);
  border: 1px solid var(--gray-300);
  box-shadow: var(--shadow-md);
}

h1 {
  text-align: center;
  color: var(--main-orange);
  margin: 0 0 24px;
  font-size: 2.2rem;
  font-weight: 800;
}

form div { margin-bottom: 18px; }
form div:last-child { margin-top: 26px; display: flex; gap: 4%; }

label {
  display: block;
  font-weight: 600;
  margin-bottom: 6px;
  color: #555;
  font-size: 1.4rem;
}

input[type="text"], input[type="password"], input[type="email"], input[type="file"], select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-2);
  font-size: 1.4rem;
  background: #fff;
  transition: border-color .2s ease, box-shadow .2s ease;
}

input::placeholder { color: var(--gray-500); }
input:focus, select:focus {
  border-color: var(--main-orange);
  outline: none;
  box-shadow: 0 0 0 3px var(--light-orange);
}

button {
  height: 42px;
  border-radius: 999px;
  font-size: 1.5rem;
  font-weight: 600;
  cursor: pointer;
  transition: filter .2s ease, background-color .2s ease;
  border: 1px solid transparent;
}
button[type="submit"] {
  background: var(--main-orange);
  color: #fff;
  border-color: var(--main-orange);
  width: 48%;
}
button[type="submit"]:hover { filter: brightness(.96); }
button[type="button"] {
  background: #fff;
  color: var(--main-orange);
  border-color: var(--main-orange);
  width: 48%;
}
button[type="button"]:hover { background: var(--light-orange); }
</style>

<script>
var UpdateForm__submitDone = false;
function Updateform__submit(form) {
  if (UpdateForm__submitDone) {
    alert('처리중입니다.');
    return false;
  }

  form.username.value = form.username.value.trim();
  form.email.value = form.email.value.trim();
  form.password.value = form.password.value.trim();
  form.password2.value = form.password2.value.trim();

  if (form.username.value.length === 0) { alert('이름을 입력해주세요.'); form.username.focus(); return false; }
  if (form.email.value.length === 0) { alert('이메일을 입력해주세요.'); form.email.focus(); return false; }

  if (form.password.value || form.password2.value) {
    if (form.password.value !== form.password2.value) {
      alert('비밀번호가 일치하지 않습니다.');
      form.password2.focus();
      return false;
    }
  }

  UpdateForm__submitDone = true;
  return true;
}
</script>
</head>

<body>
<div class="page">

  <!-- ===== HEADER ===== -->
  <header>
    <c:import url="/letsgu/header"/>
  </header>

  <!-- ===== MAIN ===== -->
  <main>
    <form action="${pageContext.request.contextPath}/letsgu/update"
          method="post"
          enctype="multipart/form-data"
          onsubmit="return Updateform__submit(this);">

      <h1>프로필 수정</h1>

      <div>
        <label for="username">이름</label>
        <input id="username" name="username" type="text" value="${user.name}" placeholder="이름을 입력해주세요." autocomplete="off" />
      </div>  

      <div>
        <label for="password">비밀번호 변경</label>
        <input id="password" name="password" type="password" placeholder="새 비밀번호(비워두면 유지)" autocomplete="new-password" />
      </div>

      <div>
        <label for="password2">비밀번호 확인</label>
        <input id="password2" name="password2" type="password"  placeholder="비밀번호를 다시 입력해주세요." autocomplete="new-password" />
      </div>

      <div>
        <label for="email">이메일</label>
        <input id="email" name="email" type="email" value="${user.email}" placeholder="이메일을 입력해주세요." autocomplete="off" />
      </div>

      <div>
        <label for="age_group">연령대</label>
        <select id="age_group" name="age_group">
          <option value="">선택</option>
          <option value="10대" ${users.agegroup == '10대' ? 'selected' : ''}>10대</option>
          <option value="20대" ${users.agegroup == '20대' ? 'selected' : ''}>20대</option>
          <option value="30대" ${users.agegroup == '30대' ? 'selected' : ''}>30대</option>
          <option value="40대" ${users.agegroup == '40대' ? 'selected' : ''}>40대</option>
          <option value="50대 이상" ${user.agegroup == '50대 이상' ? 'selected' : ''}>50대 이상</option>
        </select>
      </div>

      <div>
        <label for="gender">성별</label>
        <select id="gender" name="gender">
          <option value="">선택</option>
          <option value="M" ${users.gender == 'M' ? 'selected' : ''}>남자</option>
          <option value="F" ${users.gender == 'F' ? 'selected' : ''}>여자</option>
        </select>
      </div>
	
	 <div>
        <label for="upload_img">프로필 이미지</label>
        <input id="upload_img" name="upload_img" type="file" accept="image/*" />
        <c:if test="${not empty user.uploadimg}">
          <small style="color:#888; font-size:1.2rem;">
            현재 이미지: ${users.uploadimg}
          </small>
        </c:if>
      </div>
	 
      <div>
        <button type="submit">수정 완료</button>
        <button type="button" onclick="location.href='${pageContext.request.contextPath}/letsgu/mypage'">취소</button>
      </div>

    </form>
  </main>

  <!-- ===== FOOTER ===== -->
  <footer>
    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
  </footer>

</div>
</body>
</html>
