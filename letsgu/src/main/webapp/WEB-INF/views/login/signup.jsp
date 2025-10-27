<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script>
var JoinForm__submitDone = false;

function Joinform__submit(form) {
  if (JoinForm__submitDone) {
    alert('처리중입니다.');
    return false;
  }

  // 공백 제거
  form.login_id.value  = form.login_id.value.trim();
  form.password.value  = form.password.value.trim();
  form.password2.value = form.password2.value.trim();
  form.username.value  = form.username.value.trim();

  // 아이디
  if (form.login_id.value.length === 0) {
    alert('아이디를 입력해주세요.');
    form.login_id.focus();
    return false;
  }
  // 비밀번호
  if (form.password.value.length === 0) {
    alert('비밀번호를 입력해주세요.');
    form.password.focus();
    return false;
  }
  // 비밀번호 확인
  if (form.password.value !== form.password2.value) {
    alert('비밀번호가 일치하지 않습니다.');
    form.password2.focus();
    return false;
  }
  // 이름
  if (form.username.value.length === 0) {
    alert('이름을 입력해주세요.');
    form.username.focus();
    return false;
  }
  //이메일
  if (form.email.value.length === 0) {
	    alert('이메일을 입력해주세요.');
	    form.email.focus();
	    return false;
	  }
  // (선택) 연령대/성별 검증
  if (form.age_group.value.length === 0) {
    alert('연령대를 선택해주세요.');
    form.age_group.focus();
    return false;
  }
  if (form.gender.value.length === 0) {
    alert('성별을 선택해주세요.');
    form.gender.focus();
    return false;
  }

  // 통과 → 중복 제출 방지 후 제출 허용
  //form.submit();
  JoinForm__submitDone = true;
  return true;
  
} 
</script>
</head>
<body>
  <h1>회원가입</h1>

  <form action="${pageContext.request.contextPath}/letsgu/signup" method="post"
        onsubmit="return Joinform__submit(this);">
    <div>
      아이디 :
      <input name="login_id" type="text" placeholder="아이디를 입력해주세요." autocomplete="off" />
    </div>
    <div>
      비밀번호 :
      <input name="password" type="password" placeholder="비밀번호를 입력해주세요." autocomplete="off" />
    </div>
    <div>
      비밀번호 확인 :
      <input name="password2" type="password" placeholder="비밀번호를 다시 입력해주세요." autocomplete="off" />
    </div>
    <div>
      이름 :
      <input name="username" type="text" placeholder="이름을 입력해주세요." autocomplete="off" />
    </div>
    <div>
      이메일 :
      <input name="email" type="text" placeholder="이메일을 입력해주세요." autocomplete="off" />
    </div>
    <div>
      연령대 :
      <select name="age_group">
        <option value="">선택</option>
        <option value="10대">10대</option>
        <option value="20대">20대</option>
        <option value="30대">30대</option>
        <option value="40대">40대</option>
        <option value="50대 이상">50대 이상</option>
      </select>
    </div>
    <div>
      성별 :
      <select name="gender">
        <option value="">선택</option>
        <option value="M">남자</option>
        <option value="F">여자</option>
      </select>
    </div>
    <div>
      <button type="submit">가입</button>
      <button type="button" onclick="location.href='${pageContext.request.contextPath}/letsgu/main'">취소</button>
    </div>
  </form>
  
  
</body>
</html>
