<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>

</head>
<body>
	<h1>로그인</h1>


	<form action="${pageContext.request.contextPath}/letsgu/login"
		method="post" onsubmit="return Joinform__submit(this);">
		<div>
			아이디 : <input name="login_id" type="text" placeholder="아이디를 입력해주세요."
				autocomplete="off" />
		</div>
		<div>
			비밀번호 : <input name="password" type="password"
				placeholder="비밀번호를 입력해주세요." autocomplete="off" />
		</div>

		<div>
			<button type="submit">로그인</button>
			<button type="button"
				onclick="location.href='${pageContext.request.contextPath}/letsgu/signup'">회원가입</button>
		</div>
	</form>

	<script>
		var JoinForm__submitDone = false;

		function Joinform__submit(form) {
			if (JoinForm__submitDone) {
				alert('처리중입니다.');
				return false;
			}

			// 공백 제거
			form.login_id.value = form.login_id.value.trim();
			form.password.value = form.password.value.trim();

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

			JoinForm__submitDone = true;
			return true;
		}
	</script>


</body>
</html>
