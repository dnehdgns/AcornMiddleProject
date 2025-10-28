(() => {
  const log = (...args) => console.log('[Calendar]', ...args);

  //날짜 YYYY-MM-DD
  function toYMD(d) {
    if (!d) return '';
    if (d instanceof Date) return d.toISOString().slice(0, 10);
    const s = String(d);
    return s.length >= 10 ? s.slice(0, 10) : s;
  }

  //특수문자 이스케이프
  function esc(s) {
    if (s == null) return '';
    return String(s)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }
  
  // 모달
  function ensureModal() {
    let modal = document.getElementById('mc-modal');
    if (modal) return modal;

    modal = document.createElement('div');
    modal.id = 'mc-modal';
    modal.className = 'mc-modal hidden';
    modal.innerHTML = `
      <div class="mc-modal-backdrop" data-close="true"></div>
      <div class="mc-modal-dialog" role="dialog" aria-modal="true" aria-labelledby="mc-modal-title">
        <div class="mc-modal-header">
          <h3 id="mc-modal-title">이벤트</h3>
          <button type="button" class="mc-modal-close" data-close="true" aria-label="닫기">✕</button>
        </div>
        <div class="mc-modal-body" id="mc-modal-body"></div>
        <div class="mc-modal-footer">
          <button type="button" class="mc-btn" data-close="true">확인</button>
        </div>
      </div>
    `;
    document.body.appendChild(modal);

    modal.addEventListener('click', (e) => {
      if (e.target.dataset.close === 'true') closeModal();
    });
    document.addEventListener('keydown', (e) => {
      if (e.key === 'Escape' && !modal.classList.contains('hidden')) closeModal();
    });
    return modal;
  }
  
  function openModal(title, html) {
    const modal = ensureModal();
    modal.querySelector('#mc-modal-title').textContent = title || '이벤트';
    modal.querySelector('#mc-modal-body').innerHTML = html || '';
    modal.classList.remove('hidden');
  }
  function closeModal() {
    const modal = document.getElementById('mc-modal');
    if (modal) modal.classList.add('hidden');
  }

  // 날짜 이벤트 목록 모달 열기 
  async function openDayModal(dateStr) {
    const cfg = window.CALENDAR_CONFIG || {};
    const endpoint = cfg.fetchDayEventsUrl;
    let bodyHTML = '';

	
    try {
      const res = await fetch(`${endpoint}?date=${encodeURIComponent(dateStr)}`, {
        headers: { 'Accept': 'application/json' }
      });
      if (!res.ok) throw new Error('HTTP ' + res.status);
	  
      const json = await res.json();
      const items = json.items || [];

	  if (items.length) {
	    const detailBase = (window.CALENDAR_CONFIG && window.CALENDAR_CONFIG.eventDetailUrl) || '/letsgu/event/eventdetail';
		
		//목록생성
		bodyHTML = `
		  <ul class="mc-list">
		    ${items.map(it => {
		      const id = it.eventId; 
		      const href = id != null ? `${detailBase}?eventId=${encodeURIComponent(id)}` : '#';
		      const title = esc(it.title ?? '제목 없음');
		      const region = esc(it.region ?? '');
		      const eventDate = esc(it.eventDate ?? '');
		      const capacity = it.capacity ?? '';
		      const desc = esc(it.description ?? '');
		      const status = esc(it.status ?? '');
			  const statusClass = status === 'ACTIVE' ? 'active' : 'inactive'
		      const authorId = it.authorId ?? '';

		      return `
		        <li class="mc-list-item ${statusClass}"">
		          <a class="mc-link" href="${href}">
		            <div class="mc-list-title">${title}</div>
		            <div class="mc-list-sub"> ${eventDate}</div>
		            <div class="mc-list-sub"> ${region}</div>
		            <div class="mc-list-sub"> 모집 인원: ${capacity}명</div>
		            <div class="mc-list-sub"> ${desc || '내용 없음'}</div>
		            <div class="mc-list-sub"> ${status}</div>
		          </a>
		        </li>
		      `;
		      }).join('')}
		    </ul>
		  `;
	  } else {
	    bodyHTML = `<p class="mc-empty">등록된 이벤트가 없습니다.</p>`;
	  }
	
    } catch (err) {
      console.error('[openDayModal]', err);
      bodyHTML = `<p class="mc-error">이벤트를 불러오지 못했습니다.</p>`;
    }
    openModal(`이벤트 (${dateStr})`, bodyHTML);
  }

  // 날짜별 이벤트 카운트 
  async function fetchCounts(startStr, endStr) {
    try {
      const cfg = window.CALENDAR_CONFIG || {};
      const base = cfg.fetchCountsUrl;
      const start = toYMD(startStr);
      const end = toYMD(endStr);
      const url = `${base}?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`;

      const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
      if (!res.ok) throw new Error('HTTP ' + res.status);
      const data = await res.json();
      return data.counts || data.eventCount || {};
    } catch (e) {
      console.error('[fetchCounts]', e);
      return {};
    }
  }

  // 배지 렌더링
  function renderBadges(calendarEl, countMap) {
    // 기존 배지 제거
    calendarEl.querySelectorAll('.mc-day-count').forEach(el => el.remove());

    // 새 배지 렌더
    Object.entries(countMap).forEach(([dateStr, count]) => {
      if (!count) return;
      const cell = calendarEl.querySelector(`.fc-daygrid-day[data-date="${dateStr}"] .fc-daygrid-day-frame`);
      if (!cell) return;

      const badge = document.createElement('button');
      badge.className = 'mc-day-count';
      badge.type = 'button';
      badge.textContent = count;
      badge.setAttribute('aria-label', `${dateStr} 일정 ${count}개`);
	  
      badge.addEventListener('click', () => openDayModal(dateStr));
      cell.appendChild(badge);
    });
  }

  //배지 갱신
  async function updateBadges(startStr, endStr) {
    const calendarEl = document.getElementById('calendar');
    if (!calendarEl) return;
    const counts = await fetchCounts(startStr, endStr);
    renderBadges(calendarEl, counts);
  }

  //초기화
  const onReady = () => {
    const calendarEl = document.getElementById('calendar');
    if (!calendarEl) return console.error('Calendar element not found.');
    if (!window.FullCalendar) return console.error('FullCalendar not loaded.');

    const cfg = window.CALENDAR_CONFIG || {};

    const calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',
      height: '70vh',
      locale: cfg.locale || 'ko',
      headerToolbar: { left: 'prev,next today', center: 'title', right: '' },
      buttonText: { today: 'today' },

      // 날짜 클릭 모달 오픈
      dateClick(arg) {
        openDayModal(arg.dateStr); 
      },

      // 뷰 바뀔 때마다 배지 갱신
      datesSet(info) {
        updateBadges(toYMD(info.start), toYMD(info.end));
      }
    });

    calendar.render();

    // 초기 배지 갱신
    try {
      const v = calendar.view;
      updateBadges(toYMD(v.currentStart), toYMD(v.currentEnd));
    } catch (e) {
      console.error(e);
    }
  };

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', onReady);
  } else {
    onReady();
  }
})();
