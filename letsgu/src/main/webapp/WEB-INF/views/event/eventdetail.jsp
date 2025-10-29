<%@page import="event2.Comment"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eventdetail.css">
<meta charset="UTF-8">
<title>이벤트 상세보기</title>
</head>
<body>

	<!-- 공통 header -->
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	
	<main class = "wrapper event-detail-page">
		<div class="main-title">
			<!-- 검색바 영역 -->
			<div class = "search-bar">
				<form action="${pageContext.request.contextPath}/letsgu/event/search" method="get">
					<input type="text" name="keyword" value="${keyword}" placeholder="관심 있는 이벤트를 검색해보세요 😊" required>
					<button type="submit">검색</button>
				</form>
			</div>
			
		</div>
		
		<!-- 이벤트 상세 영역 -->
		<section class = "event-detail-section">
			<!-- 1.이미지 -->
			<div class = "detail-thumb">
				<c:choose>
					<c:when test="${not empty event.uploadImg}">
						<img src="${pageContext.request.contextPath}/upload/${event.uploadImg}" alt="이벤트 이미지">
					</c:when>
					<c:otherwise>
						<div class="no-img">이미지 없음</div>
					</c:otherwise>
				</c:choose>
			</div>
			
			<!-- 2.정보 -->
			<div class="detail-info">
				<h2 class="detail-title">${event.title}</h2>
				<p class="detail-meta">
					<span class="meta-writer">작성자: <strong>${event.authorName}</strong></span>
					<span class="meta-cat">카테고리: <strong>${event.categoryName}</strong></span>
					<span class="meta-region">지역: <strong>${event.region}</strong></span>
					<span class="meta-date">작성일: 
						<fmt:formatDate value="${event.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
					</span>
				</p>
		
				<!-- 모집 정보 -->
				<c:if test="${not empty event.eventDate || event.capacity > 0}">
					<div class="detail-recruit">
						<c:if test="${not empty event.eventDate}">
							<p>모집 마감일 : 
								<fmt:formatDate value="${event.eventDate}" pattern="yyyy-MM-dd"/>
							</p>
						</c:if>
						<c:if test="${event.capacity > 0}">
							<p>모집 인원 : ${event.capacity}명</p>
						</c:if>
					</div>
				</c:if>
		
				<!-- 본문 내용 -->
				<div class="detail-description">
					<p>${event.description}</p>
				</div>
		
				<!-- 액션영역 ( 추천/비추천, 참여버튼, 북마크) -->
				<div class="detail-actions">

					<!-- 추천 / 비추천 / 북마크 -->
					<div class="like-section">
						<button class="icon-btn like-btn">
							<span class="material-symbols-outlined">favorite</span>
							<span class="like-count">20</span>
						</button>
					
						<button class="icon-btn dislike-btn">
							<span class="material-symbols-outlined">thumb_down</span>
							<span class="dislike-count">2</span>
						</button>
					
						<button class="icon-btn bookmark-btn">
							<span class="material-symbols-outlined">bookmark</span>
						</button>
					</div>
						
					<!-- 참여하기 버튼 -->
					<div class = "right-action-group">
					<c:if test="${event.categoryId == 2}">
						<c:if test="${LOGIN_ID == null || LOGIN_ID.userId != event.authorId}">
							<!-- 로그인 안 했거나, 작성자가 아닐 때만 보여줌 -->
							<div class="participate">
								<form action="${pageContext.request.contextPath}/letsgu/part/joinevent" method="post" class="inline-form">
									<input type="hidden" name="eventId" value="${event.eventId}">
									<button type="submit" class="join-btn">참여하기</button>
								</form>
							</div>
						</c:if>
					</c:if>

					<!-- 수정 / 삭제 : 작성자 본인만 -->
					<c:if test="${LOGIN_ID != null && LOGIN_ID.userId == event.authorId}">
						<div class="edit-actions">
							<a href="${pageContext.request.contextPath}/letsgu/event/update?eventId=${event.eventId}" 
							   class="edit-btn">수정</a>

							<form action="${pageContext.request.contextPath}/letsgu/event/delete" 
								  method="post" 
								  class="inline-form"
								  onsubmit="return confirm('정말 삭제하시겠습니까?');">
								<input type="hidden" name="eventId" value="${event.eventId}">
								<button type="submit" class="delete-btn">삭제</button>
							</form>
						</div>
					</c:if>
				 </div>
				</div>
			</div>
		</section>
				
		<!-- 댓글 영역  시작-->
		<section class = "comment-section">
			<h3 class="comment-title">댓글</h3>
				<!-- 댓글 내용 추가 -->
				<!-- 댓글 작성 부분 -->
				<c:if test="${LOGIN_ID != null}">
					<form action="${pageContext.request.contextPath}/letsgu/event/commentadd" method="post" class="comment-form">
						<input type="hidden" name="eventId" value="${event.eventId}">
						<input type="hidden" name="userId" value="${LOGIN_ID.userId}">
						<textarea name="content" class="comment-input" placeholder="댓글을 입력해주세요." required></textarea>
						<button type="submit" class="comment-submit-btn">등록</button>
					</form>
				</c:if>
				
				<c:if test="${LOGIN_ID == null}">
					<div class="comment-login-required">
						<a href="${pageContext.request.contextPath}/letsgu/login">로그인 후 댓글을 작성할 수 있어요.</a>
					</div>
				</c:if>
				
				<!-- 댓글 리스트 부분 추가 -->
				<div class="comment-list">
				
				<c:choose>
					<c:when test="${LOGIN_ID != null && empty commentList}">
						<p class="no-comment">아직 댓글이 없어요. 첫 댓글을 남겨주세요!</p>
					</c:when>
					
					<c:otherwise>
						<c:forEach var="cmt" items="${commentList}">
							<div class="comment-item">
								<div class="comment-header">
									<span class="comment-writer">${cmt.authorName}</span>
									<span class="comment-date">
										<fmt:formatDate value="${cmt.createTime}" pattern="yyyy-MM-dd HH:mm"/>
									</span>
								</div>
								
								<div class="comment-body">
									<p>${cmt.content}</p>
								</div>

								<c:if test="${LOGIN_ID != null && LOGIN_ID.userId == cmt.userId}">
									<form action="${pageContext.request.contextPath}/letsgu/event/commentdel" method="post" class="comment-delete-form">
										<input type="hidden" name="commentId" value="${cmt.commentId}">
										<input type="hidden" name="eventId" value="${event.eventId}">
										<button type="submit" class="comment-delete-btn">삭제</button>
									</form>
								</c:if>
							</div>
						</c:forEach>
					</c:otherwise>
					
				</c:choose>
			</div>
		</section>
		
		<!-- 목록으로 -->
		<section  class="back-list">
			<a href="${pageContext.request.contextPath}/letsgu/event/list">목록으로 돌아가기</a>
		</section >
			
	</main>

	<!-- 공통 footer -->
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />

</body>
<script>
document.addEventListener("DOMContentLoaded", () => {
	  const postId = ${event.eventId};
	  const contextPath = '${pageContext.request.contextPath}';

	  // 공통 fetch 처리 함수
	  function sendAction(endpoint, action, onSuccess) {
	    fetch(contextPath+endpoint, {
	      method: 'POST',
	      headers: { 'Content-Type': 'application/json' },
	      credentials: 'include',
	      body: JSON.stringify({ postId, action })
	    })
	    .then(response => {
	      if (!response.ok) {
	        alert('로그인이 필요합니다.');
	        return null;
	      }
	      return response.json();
	    })
	    .then(data => {
	      if (data && data.success && typeof onSuccess === 'function') {
	        onSuccess(data);
	      }
	    });
	  }

	  // 버튼 상태 업데이트 함수
	  function updateButtonText(button, isActive, activeText, inactiveText) {
	    button.innerText = isActive ? activeText : inactiveText;
	  }

	  function updateCount(id, label, count, suffix = "") {
	    const el = document.getElementById(id);
	    if (el) el.textContent = label + count + suffix;
	  }

	  // 참여 버튼
	  const joinBtn = document.getElementById("joinBtn");
	  if (joinBtn) {
	    joinBtn.addEventListener("click", () => {
	      const isJoined = joinBtn.innerText.includes("취소");
	      
	      // 모집 인원과 현재 참여 인원 비교
	      const joinCountText = document.getElementById("joinCount").textContent;
	      const currentJoinCount = parseInt(joinCountText.replace(/\D/g, ""), 10); // 숫자만 추출

	      const maxCapacity = ${event.capacity}; // JSP에서 렌더링된 값 사용

	      if (!isJoined && currentJoinCount >= maxCapacity) {
	        alert("모집이 끝났습니다.");
	        return; // 함수 종료
	      }

	      sendAction('/letsgu/event/join', isJoined ? 'remove' : 'add', data => {
	        updateButtonText(joinBtn, !isJoined, "참여 취소", "참여");
	        updateCount("joinCount", "현재 참여 인원: ", data.joinCount, "명");
	      });
	    });
	  }

	  // 북마크 버튼
	  const bookmarkBtn = document.getElementById("bookmarkBtn");
	  if (bookmarkBtn) {
	    bookmarkBtn.addEventListener("click", () => {
	      const isBookmarked = bookmarkBtn.innerText.includes("해제");
	      sendAction('/letsgu/event/bookmark', isBookmarked ? 'remove' : 'add', () => {
	        updateButtonText(bookmarkBtn, !isBookmarked, "북마크 해제", "북마크");
	        alert(isBookmarked ? "북마크가 해제되었습니다!" : "북마크 완료!");
	      });
	    });
	  }

	  // 추천 버튼
	  const likeBtn = document.getElementById("likeBtn");
	  const dislikeBtn = document.getElementById("dislikeBtn");

	  if (likeBtn) {
	    likeBtn.addEventListener("click", () => {
	      const isLiked = likeBtn.innerText.includes("취소");
	      const isDisliked = dislikeBtn.innerText.includes("취소");

	      sendAction('/letsgu/event/like', isLiked ? 'removelike' : 'like', data => {
	        updateButtonText(likeBtn, !isLiked, "추천 취소", "추천");
	        updateCount("likeCount", "추천수: ", data.likeCount);

	        // 추천을 눌렀을 때 비추천이 활성화되어 있으면 UI 초기화
	        if (!isLiked && isDisliked) {
	          updateButtonText(dislikeBtn, false, "비추천 취소", "비추천");
	          updateCount("likeCount", "추천수: ", data.likeCount);
	          updateCount("dislikeCount", "비추천수: ", data.dislikeCount);
	        }
	      });
	    });
	  }

	  // 비추천 버튼
	  if (dislikeBtn) {
	    dislikeBtn.addEventListener("click", () => {
	      const isDisliked = dislikeBtn.innerText.includes("취소");
	      const isLiked = likeBtn.innerText.includes("취소");

	      sendAction('/letsgu/event/like', isDisliked ? 'removedislike' : 'dislike', data => {
	        updateButtonText(dislikeBtn, !isDisliked, "비추천 취소", "비추천");
	        updateCount("dislikeCount", "비추천수: ", data.dislikeCount);

	        // 비추천을 눌렀을 때 추천이 활성화되어 있으면 UI 초기화
	        if (!isDisliked && isLiked) {
	          updateButtonText(likeBtn, false, "추천 취소", "추천");
	          updateCount("likeCount", "추천수: ", data.likeCount);
	          updateCount("dislikeCount", "비추천수: ", data.dislikeCount);
	        }
	      });
	    });
	  }

	});
</script>
</html>
