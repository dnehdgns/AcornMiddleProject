<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">

  <!-- FullCalendar v5 -->
  <link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.css" rel="stylesheet"/>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.js" defer></script>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/locales-all.min.js" defer></script>
  <!-- google calendar plugin -->
  <script src="https://cdn.jsdelivr.net/npm/@fullcalendar/google-calendar@5.8.0/main.global.min.js" defer></script>
  
  <!-- Calendar CSS -->
  <link href="<%=request.getContextPath()%>/Css/base.css" rel="stylesheet"/>
  <link href="<%=request.getContextPath()%>/Css/MainCalendar.css" rel="stylesheet"/>
  <link href="<%=request.getContextPath()%>/Css/MyCalendar.css" rel="stylesheet"/>

</head>

<body>
  <!-- 참여이벤트 / 북마크이벤트 -->
  <div class="mc-tabs" id="mc-tabs">
    <button type="button" class="mc-tab" data-source="participation">참여한 이벤트</button>
    <button type="button" class="mc-tab" data-source="bookmark">북마크한 이벤트</button>
  </div>


  <h1>Let's Gu  MY event calendar</h1>
  <div id="calendar"></div>


  <script>
    window.CALENDAR_CONFIG = {
    	    fetchCountsUrl: '<%=request.getContextPath()%>/letsgu/myCalendar/count',
    	    fetchDayEventsUrl: '<%=request.getContextPath()%>/letsgu/myCalendar/list',
    	    eventDetailUrl: '<%=request.getContextPath()%>/letsgu/event/eventdetail',
    	    defaultType: '${mycalType}',
    	    
    	    googleApiKey:'AIzaSyBhY4tnpBDoJpELdAeXET_coy8UdSjcAWg',
            locale: 'ko'
    };
  </script>

  <script src="<%=request.getContextPath()%>/Js/MyCalendar.js" defer></script>
</body>
</html>