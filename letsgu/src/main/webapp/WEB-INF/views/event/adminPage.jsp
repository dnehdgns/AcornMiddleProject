<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminPage.css">
<title>관리자 페이지</title>
</head>
<body>

	<!-- 공통 header -->
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	
	<!-- content -->
	<main class = "wrapper">
		<section class = "admin-section">
			<h2>비활성화된 이벤트 목록</h2>
			
			<form action="${pageContext.request.contextPath}/letsgu/admin/list" method="post">

				<c:choose>
					<c:when test="${empty inactiveList}">
						<div class="no-result">
							<p>비활성화 처리된 이벤트가 없습니다.</p>
						</div>
					</c:when>

					<c:otherwise>
						<div class="inactive-grid">
							<c:forEach var="inactive" items="${inactiveList}">
								<div class="inactive-card">
									
									<!-- 썸네일 -->
									<div class="card-thumb">
										<c:choose>
											<c:when test="${not empty inactive.uploadImg}">
												<img src="${pageContext.request.contextPath}/upload/${inactive.uploadImg}" alt="이벤트 이미지">
											</c:when>
											<c:otherwise>
												<div class="no-thumb">이미지 없음</div>
											</c:otherwise>
										</c:choose>
									</div>

									<!-- 정보 영역 -->
									<div class="card-info">
										<h3 class="card-title">
											<a href="${pageContext.request.contextPath}/letsgu/event/eventdetail?eventId=${inactive.eventId}">
												${inactive.title}
											</a>
										</h3>
										<p class="card-region">${inactive.region}</p>
										<p class="card-status">상태 : <span>${inactive.status}</span></p>
										<p class="card-date">${inactive.createdAt}</p>
									</div>

								</div>
							</c:forEach>
						</div>
					</c:otherwise>
				</c:choose>

				<!-- 전체 삭제 버튼 -->
				<div class="admin-actions">
					<button type="submit"
			        		${empty inactiveList ? 'disabled' : ''}
			        		onclick="return confirm('비활성화된 이벤트를 모두 삭제하시겠습니까?')">
							전체삭제
					</button>
				</div>
			</form>
		</section>
	</main>


	<!--공통 footer -->
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>