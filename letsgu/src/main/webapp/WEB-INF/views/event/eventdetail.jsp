<%@page import="event2.Comment"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이벤트 상세보기</title>
</head>
<body>

<h2>이벤트 상세보기</h2>

<!-- 이미지 -->
<c:if test="${not empty event.uploadImg}">
    <p><img src="${pageContext.request.contextPath}/upload/${event.uploadImg}" width="300"></p>
</c:if>

<!-- 제목 -->
<h3>${event.title}</h3>

<!-- 글쓴이 -->
<p>작성자: ${event.authorName}</p>

<!-- 카테고리 -->
<p>카테고리: ${event.categoryName}</p>

<!-- 지역 -->
<p>지역: ${event.region}</p>

<!-- 내용 -->
<p>내용:<br>${event.description}</p>

<!-- 모집 관련 정보 -->
<c:if test="${not empty event.eventDate}">
    <p>모집 종료일: 
        <fmt:formatDate value="${event.eventDate}" pattern="yyyy-MM-dd"/>
    </p>
</c:if>

<c:if test="${event.capacity > 0}">
    <p>모집 인원: ${event.capacity} 명</p>
</c:if>

<p id="joinCount">현재 참여 인원: ${joinCount}명</p>
<p id="likeCount">추천수: ${likeCount}</p>
<p id="dislikeCount">비추천수: ${dislikeCount}</p>

<!-- 작성일자 -->
<p>작성일자: 
    <fmt:formatDate value="${event.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
</p>

<c:if test="${LOGIN_ID != null}">

<c:set var="joined" value="${joined}" />
<c:set var="liked" value="${liked}" />
<c:set var="disliked" value="${disliked}" />
<c:set var="bookmarked" value="${bookmarked}" />

<!-- 참여 버튼 -->
<button type="button" id="joinBtn">
  <c:choose>
    <c:when test="${joined}">참여 취소</c:when>
    <c:otherwise>참여</c:otherwise>
  </c:choose>
</button>

<!-- 추천 버튼 -->
<button type="button" id="likeBtn">
  <c:choose>
    <c:when test="${liked}">추천 취소</c:when>
    <c:otherwise>추천</c:otherwise>
  </c:choose>
</button>

<!-- 비추천 버튼 -->
<button type="button" id="dislikeBtn">
  <c:choose>
    <c:when test="${disliked}">비추천 취소</c:when>
    <c:otherwise>비추천</c:otherwise>
  </c:choose>
</button>

<!-- 북마크 버튼 -->
<button type="button" id="bookmarkBtn">
  <c:choose>
    <c:when test="${bookmarked}">북마크 해제</c:when>
    <c:otherwise>북마크</c:otherwise>
  </c:choose>
</button>

</c:if>



<hr>

<!-- 수정 / 삭제 버튼 (작성자만 보이게) -->
<c:if test="${LOGIN_ID != null && LOGIN_ID.userId == event.authorId}">
    <a href="${pageContext.request.contextPath}/letsgu/event/update?eventId=${event.eventId}">
        <button>수정</button>
    </a>


    <form action="${pageContext.request.contextPath}/letsgu/event/delete" method="post" style="display:inline;">
        <input type="hidden" name="eventId" value="${event.eventId}">
        <button type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
    </form>

</c:if>

<form action="${pageContext.request.contextPath}/letsgu/event/commentadd" method="post">
  <input type="hidden" name="eventid" id="eventid" value="${event.eventId}"> <br>
  <input type="hidden" name="userid" id="userid" value="${LOGIN_ID.getName()}"><br>
  
  <label for="content">댓글</label><br>
  <textarea id="content" name="content" rows="5" cols="50"></textarea><br>
  
  <button type="submit">등록</button>
</form>

<c:forEach var="comItem" items="${commentList}">
<fmt:formatDate value="${comItem.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
  <p><strong>${comItem.commentId}${comItem.userId}</strong>: ${comItem.content}</p>
  <form action="${pageContext.request.contextPath}/letsgu/event/commentdel" method="post">
  <input type="hidden" name="eventid" id="eventid" value="${event.eventId}"> <br>
  <input type="hidden" name="userid" id="userid" value="${LOGIN_ID.getName()}"><br>
  <input type="hidden" name="commentid" id="commentid" value="${comItem.commentId}"><br>
  <button type="submit">삭제</button>
</form>
</c:forEach>

<!-- 목록으로 -->
<p><a href="${pageContext.request.contextPath}/letsgu/event/list">목록으로</a></p>

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
