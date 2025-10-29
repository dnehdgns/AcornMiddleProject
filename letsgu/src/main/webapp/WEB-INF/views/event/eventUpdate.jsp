<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eventForm.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=close" />
<title>이벤트 수정</title>
</head>
<body>

<!-- 공통 header -->
	<jsp:include page="/WEB-INF/views/common/header.jsp" />

	<!-- content -->
	<main class="wrapper">
		<section class="event-form-section">

			<div class="form-header">
				<h2>글 수정하기</h2>
				<a href="${pageContext.request.contextPath}/letsgu/event/eventdetail?eventId=${event.eventId}" class="close-btn">
					<span class="material-symbols-outlined">close</span>
				</a>
			</div>

			<div class="event-form">
				
				<!-- 카테고리 선택 (GET 방식) -->
				<form action="${pageContext.request.contextPath}/letsgu/event/update" method="get" class="category-form">
					<input type="hidden" name="eventId" value="${event.eventId}">
					<div class="form-group">
						<label>카테고리</label>
						<select name="category_id" onchange="this.form.submit()">
							<option value="">선택하세요</option>
							<c:forEach var="c" items="${categoryList}">
								<option value="${c.categoryId}"
									<c:if test="${c.categoryId == selectedCategory}">selected</c:if>>
									${c.categoryName}
								</option>
							</c:forEach>
						</select>
					</div>
				</form>

				<!-- 실제 수정 폼 (POST 방식) -->
				<form action="${pageContext.request.contextPath}/letsgu/event/update"
				      method="post" enctype="multipart/form-data">

					<input type="hidden" name="eventId" value="${event.eventId}">
					<input type="hidden" name="category_id" value="${selectedCategory}">
					<input type="hidden" name="oldUploadImg" value="${event.uploadImg}">

					<!-- 지역 선택 -->
					<div class="form-group">
						<label>지역</label>
						<select name="region" required>
							<option value="">선택하세요</option>
							<c:forEach var="r" items="${regionList}">
								<option value="${r}" <c:if test="${r == event.region}">selected</c:if>>${r}</option>
							</c:forEach>
						</select>
					</div>

					<!-- 제목 -->
					<div class="form-group">
						<label>제목</label>
						<input type="text" name="title" value="${event.title}" required>
					</div>

					<!-- 내용 -->
					<div class="form-group">
						<label>내용</label>
						<textarea name="description" rows="6">${event.description}</textarea>
					</div>

					<!-- 카테고리가 2번일 때만 표시 -->
					<c:if test="${selectedCategory == 2}">
						<div class="form-row">
							<div class="form-group">
								<label>모집날짜</label>
								<input type="date" name="event_date" 
									value="<fmt:formatDate value='${event.eventDate}' pattern='yyyy-MM-dd'/>">
							</div>
							<div class="form-group">
								<label>모집인원</label>
								<input type="number" name="capacity" value="${event.capacity}" min="1" max="100">
							</div>
						</div>
					</c:if>

					<!-- 기존 이미지 -->
					<div class="form-group">
						<label>기존 이미지</label>
						<c:if test="${not empty event.uploadImg}">
							<img src="${pageContext.request.contextPath}/upload/${event.uploadImg}" 
							     alt="이전 이미지" width="120" height="120"
							     style="object-fit:cover;border-radius:0.8rem;border:0.1rem solid var(--gray-300);">
						</c:if>
						<c:if test="${empty event.uploadImg}">
							<p style="color:var(--gray-500);">등록된 이미지가 없습니다.</p>
						</c:if>
					</div>

					<!-- 새 이미지 업로드 -->
					<div class="form-group">
						<label>새 이미지 업로드</label>
						<div class="file-upload">
							<label for="uploadImg" class="file-label">파일 선택</label>
							<input type="file" id="uploadImg" name="uploadImg" accept="image/*" onchange="showFileName(this)">
							<span id="file-name"></span>
						</div>
					</div>

					<!-- 버튼 -->
					<div class="form-actions">
						<button type="submit" class="submit-btn">수정하기</button>
					</div>
				</form>
			</div>
		</section>
	</main>


	<!--공통 footer -->
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />


<!-- 파일명 표시 -->
	<script>
		function showFileName(input) {
			const fileName = document.getElementById("file-name");
			if (input.files.length > 0) fileName.textContent = input.files[0].name;
		}
	</script>

</body>
</html>
