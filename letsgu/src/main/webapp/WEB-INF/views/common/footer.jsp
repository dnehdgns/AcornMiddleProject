<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
/* =========================================================
   3) FOOTER ONLY (전용 네임스페이스 권장)
   ========================================================= */
.site-footer{
  background:#fff;
  border-top:1px solid var(--gray-300);
  color:var(--gray-500);
  font-size:1.3rem;
  text-align:center;
  padding:20px 10px;
  line-height:1.8;
}
.site-footer__inner{
  max-width:1080px;
  margin:0 auto;
  padding:30px 16px 20px;
  text-align:center;
}
.site-footer__title{
  font-weight:700;
  font-size:1.6rem;
  color:var(--text-main);
  margin-bottom:6px;
}
.site-footer__desc{
  font-size:1.4rem;
  color:var(--gray-500);
  margin-bottom:16px;
}
.site-footer__members{
  margin-top:10px;
  font-size:1.3rem;
}
.site-footer__members span{
  color:var(--text-main);
  font-weight:600;
}
.site-footer__copy{
  border-top:1px solid var(--gray-300);
  margin-top:20px;
  padding-top:10px;
  font-size:1.2rem;
  color:var(--gray-500);
}
</style>
</head>
<body>
	<!-- ===== FOOTER (전용 클래스 사용) ===== -->
<footer class="site-footer">
  <div class="site-footer__inner">
    <div class="site-footer__title">LET’S GU PROJECT TEAM</div>
    <div class="site-footer__desc">지역 기반 소모임과 공간 연결을 위한 웹 플랫폼, <span style="color:var(--main-orange);font-weight:600;">Let's Gu</span></div>
    <div class="site-footer__members">
      <p><span>조장</span> 우동훈</p>
      <p><span>팀원</span> 박성욱 · 김민희 · 이혜린</p>
    </div>
    <div class="site-footer__copy">© 2025 LET'S GU. All rights reserved.</div>
  </div>
</footer>
</body>
</html>