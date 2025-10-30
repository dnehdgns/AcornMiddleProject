(() => {
  const log = (...args) => console.log('[MyCalendar]', ...args);

  // ===== 유틸 =====
  function toYMD(d) {
    if (!d) return '';
    if (d instanceof Date) return d.toISOString().slice(0, 10);
    const s = String(d);
    return s.length >= 10 ? s.slice(0, 10) : s;
  }

  function esc(s) {
    if (s == null) return '';
    return String(s)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }

  // ===== 상태 및 헬퍼 =====
  function normalizeType(t) {
    const v = String(t || '').toLowerCase();
    return v === 'participation' ? 'participation' : 'bookmark';
  }

  function setActiveTabUI(nextType) {
    document.querySelectorAll('.mc-tab[data-source]').forEach(btn => {
      const t = (btn.dataset.source || '').toLowerCase();
      const isActive = t === nextType;
      btn.classList.toggle('is-active', isActive);
      btn.setAttribute('aria-selected', isActive ? 'true' : 'false');
      btn.setAttribute('tabindex', isActive ? '0' : '-1');
    });
  }

  const cfg = window.CALENDAR_CONFIG || {};
  let currentType = normalizeType(cfg.defaultType || 'bookmark');

  function withType(url) {
    const u = new URL(url, location.origin);
    u.searchParams.set('type', currentType);
    return u.toString();
  }
  
  
  // ===== 모달 =====
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
          <h3 id="mc-modal-title" class="mc-modal-title">이벤트</h3>
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

  
  function getTypeLabel() {
    return currentType === 'participation' ? '참여 이벤트' : '북마크 이벤트';
  }
  
  // ===== API 호출 =====
  async function openDayModal(dateStr) {
    const endpoint = cfg.fetchDayEventsUrl;
    let bodyHTML = '';

    try {
      if (!endpoint) throw new Error('fetchDayEventsUrl missing');
      const res = await fetch(
        withType(`${endpoint}?date=${encodeURIComponent(dateStr)}`),
        { headers: { 'Accept': 'application/json' } }
      );
      if (!res.ok) throw new Error('HTTP ' + res.status);

      const json = await res.json();
      const items = json.items || [];

      if (items.length) {
        const detailBase = cfg.eventDetailUrl || '/event/detail';
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
              const status = String(it.status ?? '').toUpperCase();
              const statusClass = (status === 'ACTIVE') ? 'active' : 'inactive';
              return `
                <li class="mc-list-item ${statusClass}">
                  <a class="mc-link" href="${href}">
                    <div class="mc-list-title">${title}</div>
                    <div class="mc-list-sub-1">
                      <div class="mc-list-sub">${eventDate}</div>
                      <div class="mc-list-sub">${region}</div>
                    </div>
                    <div class="mc-list-sub-2">
                      <div class="mc-list-sub">${desc || '내용 없음'}</div>
                    </div>
                    <div class="mc-list-sub-3">
                      <div class="mc-list-sub">모집 인원: ${capacity}명</div>
                      <div class="mc-list-sub status-label ${statusClass}">${status}</div>
                    </div>
                  </a>
                </li>
              `;
            }).join('')}
          </ul>
        `;
      } else {
		bodyHTML = `
		  <p class="mc-empty">등록된 이벤트가 없습니다.</p>
		  <a class="mc-empty-link" href="/letsgu/letsgu/event/list">이벤트 리스트로 이동하기</a>
		`;
      }

    } catch (err) {
      console.error('[openDayModal]', err);
      bodyHTML = `<p class="mc-error">이벤트를 불러오지 못했습니다.</p>`;
    }
    openModal(`${dateStr} ${getTypeLabel()}`, bodyHTML);
  }
//날짜별 이벤트 카운트
  async function fetchCounts(start, end) {
    const base = cfg.fetchCountsUrl;
    if (!base) return {};
    const url = withType(`${base}?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`);
    const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
    if (!res.ok) throw new Error('HTTP ' + res.status);
    return (await res.json()).counts || {};
  }
//배지 랜더링
  function renderBadges(calendarEl, countMap) {
	//기존 배지 제서
    calendarEl.querySelectorAll('.mc-day-count').forEach(el => el.remove());
	
	//새 배지 렌더
    Object.entries(countMap).forEach(([dateStr, count]) => {
      if (!count) return;
      const cell = calendarEl.querySelector(`.fc-daygrid-day[data-date="${dateStr}"] .fc-daygrid-day-frame`);
      if (!cell) return;
	  
      const badge = document.createElement('button');
      badge.className = 'mc-day-count';
      badge.type = 'button';
      badge.textContent = String(count);
	  
	  const label = `${count} event${count === 1 ? '' : 's'}`;
	  badge.textContent = label;

	  badge.setAttribute('aria-label', `${dateStr} 일정 ${count}개`);

	  badge.addEventListener('click', () => openDayModal(dateStr));
	  cell.appendChild(badge);
    });
  }
//배지 갱신
  async function updateBadges(startStr, endStr) {
    const calendarEl = document.getElementById('calendar');
    if (!calendarEl) return;
    try {
      const counts = await fetchCounts(startStr, endStr);
      renderBadges(calendarEl, counts);
    } catch (e) {
      console.error('[fetchCounts]', e);
      renderBadges(calendarEl, {}); // 실패해도 이전 배지 제거
    }
  }

  // ===== 초기화 =====
  const onReady = () => {
    const calendarEl = document.getElementById('calendar');
    if (!calendarEl) return console.error('Calendar element not found.');
    if (!window.FullCalendar) return console.error('FullCalendar not loaded.');

    const hasGcal = !!cfg.googleApiKey;
    const calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',
      height: '70vh',
      locale: cfg.locale || 'ko',
      headerToolbar: { left: 'prev,next', center: 'title', right: 'today' },
      buttonText: { today: 'Today' },
      ...(hasGcal ? {
        googleCalendarApiKey: cfg.googleApiKey,
        eventSources: [{
          googleCalendarId: 'ko.south_korea#holiday@group.v.calendar.google.com',
          display: 'background',
          className: 'gg-holiday-bg'
        }]
      } : {}),
      dateClick(arg) {
        openDayModal(arg.dateStr);
      },
      datesSet(info) {
        updateBadges(toYMD(info.start), toYMD(info.end));
      }
    });

    calendar.render();
    window._calendarInstance = calendar;

    // 초기 탭 UI 동기화 + 초기 배지
    setActiveTabUI(currentType);
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

  // ===== 탭 전환 (mc-tab 클릭) =====
  document.addEventListener('click', (e) => {
    const btn = e.target.closest('.mc-tab[data-source]');
    if (!btn) return;

    const next = normalizeType(btn.dataset.source);
    if (next === currentType) return;

    currentType = next;
    log('탭 전환:', currentType);
    setActiveTabUI(currentType);

    const cal = window._calendarInstance;
    if (cal) {
      const v = cal.view;
      updateBadges(toYMD(v.currentStart), toYMD(v.currentEnd)).catch(err => {
        console.error('[updateBadges]', err);
      });
    }
  });
})();
