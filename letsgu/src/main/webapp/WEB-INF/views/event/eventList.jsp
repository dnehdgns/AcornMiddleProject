<%@page import="event1.PageHandler"%>
<%@page import="event1.Category"%>
<%@page import="event1.Event"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eventList.css">
<title>이벤트 목록</title>
</head>
<body>

	<!-- 공통 header -->
	<jsp:include page="/WEB-INF/views/common/header.jsp" />

	<main class="wrapper">
		<!--content-->
		<div class="main-title">
			<!-- 검색바 영역 -->
			<div class = "search-bar">
				<form action="${pageContext.request.contextPath}/letsgu/event/search" method="get">
					<input type="text" name="keyword" value="${keyword}" placeholder="관심 있는 이벤트를 검색해보세요 😊" required>
					<button type="submit">검색</button>
				</form>
			</div>
			
			<!-- 인기순, 지역 선택 버튼 -->
			<div class="filter-group">
				<!-- 인기순 버튼 -->
				<div class = populer-filter>
					<a href="${pageContext.request.contextPath}/letsgu/event/list?sort=popular"
					    	${param.sort eq 'popular' ? 'active' : ''}">
						🔥 인기순
					</a>
				</div>
				<!-- 지역 선택 버튼 -->
				<div class = "region-filter">
					<a href="?region=전체" class="region-btn ${param.region eq '전체' ? 'active' : ''}">전체</a>
					<c:forEach var="region" items="${regionList}">
						<a href="?region=${region}" class="region-btn ${param.region eq region ? 'active' : ''}">${region}</a>
					</c:forEach>
				</div>
			</div>
			
		</div>
		
		<!-- 메인 컨텐츠 -->
		<div class="main-content">
		
			<div class = "content1">
				<!--지역, 카테고리, 인기순 제목  -->
				<div class = "content1-header">
					<h2>
						<c:choose>
							<c:when test="${param.sort eq 'popular'}">
								🔥 인기 TOP 5 🔥
							</c:when>
					
							<c:when test="${not empty keyword}">
								"${keyword}"  검색 결과
							</c:when>
					
							<c:when test="${not empty param.region and param.region ne '전체' and not empty param.category}">
									서울시 ${param.region}  
									<c:forEach var="category" items="${categoryList}">
										<c:if test="${param.category eq category.categoryId}">
											"${category.categoryName}"
										</c:if>
									</c:forEach>
									관련 소식
							</c:when>
					
							<c:when test="${not empty param.region and param.region ne '전체' and empty param.category}">
									"서울시 ${param.region}" 이벤트
							</c:when>
					
							<c:when test="${(empty param.region or param.region eq '전체') and not empty param.category}">
								<c:forEach var="category" items="${categoryList}">
									<c:if test="${param.category eq category.categoryId}">
										"${category.categoryName}"
									</c:if>
								</c:forEach>
									관련 소식
							</c:when>
					
							<c:otherwise>
								전체 소식
							</c:otherwise>
							
						</c:choose>
					</h2>
					<a href="${pageContext.request.contextPath}/letsgu/event/reg" class="btn-write">
	            			글 등록하기
	        		</a>
				</div>
			</div>
			
			<div class = "content2">
				<!-- 카테고리 선택 버튼 -->
				<aside class = "category-filter">
					<ul>
						<!-- 전체 버튼 -->
						<li>
							<a href="${pageContext.request.contextPath}/letsgu/event/list?region=${param.region}&category=" 
							   class="${empty param.category ? 'active' : ''}">
								전체
							</a>
						</li>
		
						<!-- 각 카테고리 버튼 -->
						<c:forEach var="category" items="${categoryList}">
							<li>
								<a href="${pageContext.request.contextPath}/letsgu/event/list?region=${param.region}&category=${category.categoryId}" 
								   class="${param.category eq category.categoryId ? 'active' : ''}">
										${category.categoryName}
								</a>
							</li>
						</c:forEach>
					</ul>
					
				</aside>
				
				<!-- 이벤트 목록 출력 -->
				<section class = "event-list">
					<c:choose>
						<c:when test="${empty eventList}">
	            			<div class="no-result">
	                			<p>🔍 검색 결과가 없습니다.</p>
	                			<p>다른 지역이나 카테고리로 검색해보세요!</p>
	            			</div>
	        			</c:when>
						
						<c:otherwise>
							<c:forEach var="e" items="${eventList}">
								<!-- 이벤트 1개 -->
								<a href="${pageContext.request.contextPath}/letsgu/event/eventdetail?eventId=${e.eventId}" class="event-item">
									<!-- 왼쪽 : 텍스트 -->
									<div class="event-text">
				                    	<h3 class="event-title">${e.title}</h3>
				                    	<p class="event-desc">${e.description}</p>
				                    	<div class="event-meta">
				                        	<span>${e.region} · ${e.categoryName}</span>
				                        	<span>작성일자 ${e.createdAt} </span>
				                    	</div>
				                    	<c:if test="${isPopular}">
										    <div class="likes">
										        <span class="material-symbols-outlined">thumb_up</span>${e.likeCount} 
										        <span class="material-symbols-outlined">thumb_down</span>${e.dislikeCount}
										    </div>
										</c:if>
				                	</div>
	                		
			                		<!--오른쪽 : 이미지 썸네일  -->
			                		<c:if test="${not empty e.uploadImg}">
					                    <div class="event-img">
					                        <img src="${pageContext.request.contextPath}/upload/${e.uploadImg}" alt="(이미지 없음)">
					                    </div>
			                		</c:if>
								</a>
						
							</c:forEach>
						</c:otherwise>
					</c:choose>
				
				</section>
				
			</div>
			<!-- 페이징 -->
				<c:if test="${(empty param.region or param.region eq '전체') 
						              and (empty param.category or param.category eq '') 
						              and (empty param.sort) 
						              and (empty keyword)}">
	    
				    <div class="paging">
				        <c:if test="${page.currentGrp > 1}">
				            <a href="${pageContext.request.contextPath}/letsgu/event/list?p=${page.grpStart - 1}">이전</a>
				        </c:if>
				
				        <c:forEach var="i" begin="${page.grpStart}" end="${page.grpEnd}">
				            <a href="${pageContext.request.contextPath}/letsgu/event/list?p=${i}"
				               class="${i eq param.p ? 'active' : ''}">${i}</a>
				        </c:forEach>
				
				        <c:if test="${page.grpEnd < page.totalPage}">
				            <a href="${pageContext.request.contextPath}/letsgu/event/list?p=${page.grpEnd + 1}">다음</a>
				        </c:if>
				    </div>
				</c:if>
			
		</div>
	</main>

	<!--공통 footer -->
	<jsp:include page="/WEB-INF/views/common/footer.jsp" />


</body>
</html>