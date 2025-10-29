<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

  <!-- FullCalendar v5.8.0 -->
  <link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.css" rel="stylesheet"/>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.js" defer></script>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/locales-all.min.js" defer></script>
  <!-- google calendar plugin -->
  <script src="https://cdn.jsdelivr.net/npm/@fullcalendar/google-calendar@5.8.0/main.global.min.js" defer></script>

  <!-- Calendar CSS -->
  <link href="<%=request.getContextPath()%>/Css/base.css" rel="stylesheet"/>
  <link href="<%=request.getContextPath()%>/Css/MainCalendar.css" rel="stylesheet"/>

</head>
<body>

  <h1>Let's Gu event calendar</h1>
  <div id="calendar"></div>

  <script>
    window.CALENDAR_CONFIG = {
      fetchCountsUrl: '<%=request.getContextPath()%>/letsgu/cal/event/count',
      fetchDayEventsUrl: '<%=request.getContextPath()%>/letsgu/event/listByDate',
      eventDetailUrl: '<%=request.getContextPath()%>/letsgu/event/eventdetail',
      googleApiKey:'AIzaSyBhY4tnpBDoJpELdAeXET_coy8UdSjcAWg',
      locale: 'ko'
    };
  </script>

  <!-- JS -->
  <script src="<%=request.getContextPath()%>/Js/MainCalendar.js" defer></script>

</body>
</html>

<!--링크하기 <a href="<%=request.getContextPath()%>/letsgu/mainCalendar">캘린더 열기</a> -->