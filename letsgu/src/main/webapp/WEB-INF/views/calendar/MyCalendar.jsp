<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>마이 캘린더</title>

  <!-- FullCalendar v5 -->
  <link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.css" rel="stylesheet"/>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.js" defer></script>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/locales-all.min.js" defer></script>

  <!-- 스타일 -->
  <link href="<%=request.getContextPath()%>/Css/MainCalendar.css" rel="stylesheet"/>
  <link href="<%=request.getContextPath()%>/Css/MyCalendar.css" rel="stylesheet"/>

  <style>
    /* 상단 탭 */
    .mc-tabs { max-width: 1000px; margin: 24px auto 0; display: flex; gap: 8px; }
    .mc-tab { padding: 8px 14px; border:1px solid #ddd; border-radius: 20px; cursor:pointer; background:#f6f6f6; }
    .mc-tab.active { background:#222; color:#fff; }
  </style>
</head>
<body>
  <!-- 상단 탭: 참여 / 북마크 -->
  <div class="mc-tabs" id="mc-tabs">
    <button type="button" class="mc-tab active" data-source="participation">참여한 이벤트</button>
    <button type="button" class="mc-tab" data-source="bookmark">북마크한 이벤트</button>
  </div>

  <div id="calendar"></div>

  <script>
    window.CALENDAR_CONFIG = {
      // counts / list (마이 캘린더 전용 엔드포인트)
      fetchCountsUrl: '<%=request.getContextPath()%>/letsgu/mypage/cal/count',
      fetchDayEventsUrl: '<%=request.getContextPath()%>/letsgu/mypage/event/listByDate',
      eventDetailUrl: '<%=request.getContextPath()%>/letsgu/event/eventdetail',
      locale: 'ko',

      // 참여 or 북마크
      defaultSource: 'participation'
    };
  </script>

  <script src="<%=request.getContextPath()%>/js/MyCalendar.js" defer></script>
</body>
</html>