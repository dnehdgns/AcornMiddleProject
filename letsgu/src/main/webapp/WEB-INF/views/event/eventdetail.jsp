<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/Css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Css/eventdetail.css">
<meta charset="UTF-8">
<title>이벤트 상세보기</title>
</head>
<body>

	<!-- 공통 header -->
	<c:import url="/letsgu/header?eventId=${event.eventId}"/>
	
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
							<p class="recruit-deadline">마감일 : 
								<fmt:formatDate value="${event.eventDate}" pattern="yyyy-MM-dd"/>
							</p>
						</c:if>
						<c:if test="${event.capacity > 0}">
							<p class="recruit-capacity">모집 인원 : ${event.capacity}명</p>
							<p class="recruit-current">현재 참여 인원 : ${joinCount}명</p>
						</c:if>
					</div>
				</c:if>
		
				<!-- 본문 내용 -->
				<div class="detail-description">
					<p>${event.description}</p>
				</div>
		
				<!-- 액션영역 ( 추천/비추천, 참여버튼, 북마크) -->
				
			<c:if test="${event.status != 'INACTIVE'}">
			    <div class="detail-actions">
			
			        <!-- 추천 / 비추천 / 북마크 -->
			        <c:if test="${LOGIN_ID != null && LOGIN_ID.userId != event.authorId}">
			            <div class="like-section">
			                <!-- 좋아요 버튼 -->
			                <form action="${pageContext.request.contextPath}/letsgu/event/like" method="post" class="inline-form">
			                    <input type="hidden" name="eventId" value="${event.eventId}">
			                    <button type="submit" id="likeBtn"
			                            class="icon-btn like-btn ${liked ? 'active' : ''}">
			                        <span class="material-symbols-outlined">favorite</span>
			                        <span class="like-count">${likeCount}</span>
			                    </button>
			                </form>
			
			                <!-- 싫어요 버튼 -->
			                <form action="${pageContext.request.contextPath}/letsgu/event/dislike" method="post" class="inline-form">
			                    <input type="hidden" name="eventId" value="${event.eventId}">
			                    <button type="submit" id="dislikeBtn"
			                            class="icon-btn dislike-btn ${disliked ? 'active' : ''}">
			                        <span class="material-symbols-outlined">thumb_down</span>
			                        <span class="dislike-count">${dislikeCount}</span>
			                    </button>
			                </form>
			
			                <!-- 북마크 버튼 -->
			                <form action="${pageContext.request.contextPath}/letsgu/event/bookmark" method="post" class="inline-form">
			                    <input type="hidden" name="eventId" value="${event.eventId}">
			                    <button type="submit" id="bookmarkBtn"
			                            class="icon-btn bookmark-btn ${bookmarked ? 'active' : ''}">
			                        <span class="material-symbols-outlined">bookmark</span>
			                    </button>
			                </form>
			            </div>
			        </c:if>
			
			        <!-- 참여하기 버튼 -->
			        <div class="right-action-group">
			            <c:if test="${event.categoryId == 2 && LOGIN_ID != null && LOGIN_ID.userId != event.authorId}">
			                <div class="participate">
			                    <form action="${pageContext.request.contextPath}/letsgu/event/join" method="post" class="inline-form">
			                        <input type="hidden" name="eventId" value="${event.eventId}">
			                        <button type="submit" class="join-btn ${joined ? 'active' : ''}">
			                            <c:choose>
			                                <c:when test="${joined}">참여취소</c:when>
			                                <c:otherwise>참여하기</c:otherwise>
			                            </c:choose>
			                        </button>
			                    </form>
			                </div>
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
			</c:if>
		</section>
				
		<!-- 댓글 영역  시작-->
		<section class = "comment-section">
			<h3 class="comment-title">댓글</h3>
				<!-- 댓글 내용 추가 -->
				<!-- 댓글 작성 부분 -->
				<c:if test="${LOGIN_ID != null && event.status != 'INACTIVE' }">
					<form action="${pageContext.request.contextPath}/letsgu/event/commentadd" method="post" class="comment-form">
						<input type="hidden" name="eventId" value="${event.eventId}">
						<input type="hidden" name="userId" value="${LOGIN_ID.name}">
						<textarea name="content" class="comment-input" placeholder="댓글을 입력해주세요." required></textarea>
						<button type="submit" class="comment-submit-btn">등록</button>
					</form>
				</c:if>
				
				<c:if test="${LOGIN_ID == null}">
					<div class="comment-login-required">
						<a href="${pageContext.request.contextPath}/letsgu/login?eventId=${event.eventId}">로그인 후 댓글을 작성할 수 있어요.</a>
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
									<span class="comment-writer">${cmt.userId}</span>
									<span class="comment-date">
										<fmt:formatDate value="${cmt.createTime}" pattern="yyyy-MM-dd HH:mm"/>
									</span>
								</div>
								
								<div class="comment-body">
									<p>${cmt.content}</p>
								</div>

								<c:if test="${LOGIN_ID != null && LOGIN_ID.name == cmt.userId}">
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
	  const maxCapacity = ${event.capacity};

	  // 공통 fetch 처리 함수
	  function sendAction(endpoint, action, onSuccess) {
		  console.log("check1");
	    fetch(contextPath + endpoint, {
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

	  // 참여 버튼
	  const joinBtn = document.querySelector(".join-btn");
	  const joinCountEl = document.querySelector(".recruit-current");

	  if (joinBtn && joinCountEl) {
	    joinBtn.addEventListener("click", (e) => {
	      e.preventDefault();
	      const isJoined = joinBtn.classList.contains("active");
	      const currentJoinCount = parseInt(joinCountEl.textContent.replace(/\D/g, ""), 10);

	      if (!isJoined && currentJoinCount >= maxCapacity) {
	        alert("모집이 끝났습니다.");
	        return;
	      }

	      sendAction('/letsgu/event/join', isJoined ? 'remove' : 'add', data => {
	        joinBtn.classList.toggle("active", !isJoined);
	        alert(isJoined ? "참여 취소 되었습니다.!" : "참여 완료!");
	        joinBtn.innerText = isJoined ? "참여하기" : "참여취소";
	        joinCountEl.textContent = "현재 참여 인원 : "+ data.joinCount +"명";
	      });
	    });
	  }

	  // 북마크 버튼
	  const bookmarkBtn = document.getElementById("bookmarkBtn");
	  if (bookmarkBtn) {
	    bookmarkBtn.addEventListener("click", () => {
			const isBookmarked = bookmarkBtn.classList.contains("active");
			sendAction('/letsgu/event/bookmark', isBookmarked ? 'remove' : 'add', () => {
				bookmarkBtn.classList.toggle("active", !isBookmarked);
				alert(isBookmarked ? "북마크가 해제되었습니다!" : "북마크 완료!");
	      });
	    });
	  }

	  // 추천 / 비추천 버튼
	  const likeBtn = document.getElementById("likeBtn");
	  const dislikeBtn = document.getElementById("dislikeBtn");
	  const likeCountEl = document.querySelector(".like-count");
	  const dislikeCountEl = document.querySelector(".dislike-count");

	  if (likeBtn && dislikeBtn && likeCountEl && dislikeCountEl) {
	    likeBtn.addEventListener("click", () => {
	      const isLiked = likeBtn.classList.contains("active");
	      const isDisliked = dislikeBtn.classList.contains("active");

	      sendAction('/letsgu/event/like', isLiked ? 'removelike' : 'like', data => {
	        likeBtn.classList.toggle("active", !isLiked);
	        likeCountEl.textContent = data.likeCount;

	        if (!isLiked && isDisliked) {
	          dislikeBtn.classList.remove("active");
	          dislikeCountEl.textContent = data.dislikeCount;
	        }
	      });
	    });

	    dislikeBtn.addEventListener("click", () => {
	      const isDisliked = dislikeBtn.classList.contains("active");
	      const isLiked = likeBtn.classList.contains("active");

	      sendAction('/letsgu/event/like', isDisliked ? 'removedislike' : 'dislike', data => {
	        dislikeBtn.classList.toggle("active", !isDisliked);
	        dislikeCountEl.textContent = data.dislikeCount;

	        if (!isDisliked && isLiked) {
	          likeBtn.classList.remove("active");
	          likeCountEl.textContent = data.likeCount;
	        }
	      });
	    });
	  }
	});
</script>
</html>