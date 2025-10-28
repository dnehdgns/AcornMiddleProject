(() => {
  const esc = (s) => (s == null ? '' : String(s)
    .replace(/&/g,'&amp;').replace(/</g,'&lt;')
    .replace(/>/g,'&gt;').replace(/"/g,'&quot;'));
  const toYMD = (d) => (d instanceof Date ? d.toISOString().slice(0,10) : String(d).slice(0,10));

  // ----- Modal 재사용 (MainCalendar와 동일) -----
  function ensureModal(){
    let m = document.getElementById('mc-modal');
    if (m) return m;
    m = document.createElement('div');
    m.id='mc-modal';
    m.className='mc-modal hidden';
    m.innerHTML = `
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
      </div>`;
    document.body.appendChild(m);
    m.addEventListener('click', (e)=>{ if(e.target.dataset.close==='true') closeModal();});
    document.addEventListener('keydown', (e)=>{ if(e.key==='Escape' && !m.classList.contains('hidden')) closeModal();});
    return m;
  }
  function openModal(title, html){
    const m = ensureModal();
    m.querySelector('#mc-modal-title').textContent = title || '이벤트';
    m.querySelector('#mc-modal-body').innerHTML = html || '';
    m.classList.remove('hidden');
  }
  function closeModal(){ const m=document.getElementById('mc-modal'); if(m) m.classList.add('hidden'); }

  // API
  async function fetchCounts(startStr, endStr, source){
    const cfg = window.CALENDAR_CONFIG || {};
    const base = cfg.fetchCountsUrl;
    const url = `${base}?start=${encodeURIComponent(toYMD(startStr))}&end=${encodeURIComponent(toYMD(endStr))}&source=${encodeURIComponent(source)}`;
    const res = await fetch(url, { headers: {'Accept':'application/json'} });
    if(!res.ok) throw new Error('HTTP '+res.status);
    const data = await res.json();
    return data.counts || data.eventCount || {};
  }

  async function fetchListByDate(dateStr, source){
    const cfg = window.CALENDAR_CONFIG || {};
    const base = cfg.fetchDayEventsUrl;
    const url = `${base}?date=${encodeURIComponent(dateStr)}&source=${encodeURIComponent(source)}`;
    const res = await fetch(url, { headers: {'Accept':'application/json'} });
    if(!res.ok) throw new Error('HTTP '+res.status);
    return res.json(); // { items: [...] }
  }

  // Badge 
  function renderBadges(calendarEl, countMap, onClick){
    calendarEl.querySelectorAll('.mc-day-count').forEach(el=>el.remove());
    Object.entries(countMap).forEach(([dateStr, count])=>{
      if(!count) return;
      const frame = calendarEl.querySelector(`.fc-daygrid-day[data-date="${dateStr}"] .fc-daygrid-day-frame`);
      if(!frame) return;
      const b = document.createElement('button');
      b.className='mc-day-count';
      b.type='button';
      b.textContent = count;
      b.setAttribute('aria-label', `${dateStr} 일정 ${count}개`);
      b.addEventListener('click', ()=>onClick(dateStr));
      frame.appendChild(b);
    });
  }

  // List Modal 
  async function openDayModal(dateStr, source){
    try{
      const json = await fetchListByDate(dateStr, source);
      const items = json.items || [];
      const cfg = window.CALENDAR_CONFIG || {};
      const detailBase = cfg.eventDetailUrl || '/letsgu/event/eventdetail';

      let html = '';
      if(items.length === 0){
        html = `<p class="mc-empty">등록된 이벤트가 없습니다.</p>`;
      }else{
        html = `
          <ul class="mc-list">
            ${items.map(it=>{
              const id = it.eventId ?? it.id;
              const href = id!=null ? `${detailBase}?id=${encodeURIComponent(id)}` : '#';
              const title = esc(it.title ?? '제목 없음');
              const date  = esc(it.eventDate ?? '');
              const region= esc(it.region ?? '');
              const cap   = it.capacity ?? '';
              const desc  = esc(it.description ?? '');
              const status= String(it.status ?? '').toUpperCase();
              const statusClass = status==='ACTIVE' ? 'active':'inactive';
              return `
                <li class="mc-list-item ${statusClass}">
                  <a class="mc-link" href="${href}">
                    <div class="mc-list-title">${title}</div>
                    <div class="mc-list-sub"> ${date}</div>
                    <div class="mc-list-sub"> ${region}</div>
                    <div class="mc-list-sub"> ${cap}명</div>
                    <div class="mc-list-sub"> ${desc || '내용 없음'}</div>
                    <div class="mc-list-sub status-label">${status}</div>
                  </a>
                </li>
              `;
            }).join('')}
          </ul>`;
      }
      openModal(`마이 이벤트 (${dateStr}) — ${source==='participation'?'참여':'북마크'}`, html);
    }catch(e){
      console.error(e);
      openModal('오류', `<p class="mc-error">데이터를 불러오지 못했습니다.</p>`);
    }
  }

  //초기화
  const onReady = async () => {
    const cfg = window.CALENDAR_CONFIG || {};
    const calendarEl = document.getElementById('calendar');
    if(!calendarEl || !window.FullCalendar) return;

    // participation | bookmark
    let currentSource = cfg.defaultSource || 'participation';

    const calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',
      height: '70vh',
      locale: cfg.locale || 'ko',
      headerToolbar: { left: 'prev,next today', center: 'title', right: '' },
      buttonText: { today: '오늘' },
      dateClick(arg){ openDayModal(arg.dateStr, currentSource); },
      datesSet: async (info)=>{
        try{
          const counts = await fetchCounts(info.start, info.end, currentSource);
          renderBadges(calendarEl, counts, (d)=>openDayModal(d, currentSource));
        }catch(e){ console.error(e); }
      }
    });
    calendar.render();

    // 탭 클릭으로 source 변경
    const tabs = document.getElementById('mc-tabs');
    if(tabs){
      tabs.addEventListener('click', async (e)=>{
        const btn = e.target.closest('.mc-tab');
        if(!btn) return;
        // 탭 활성화 
        tabs.querySelectorAll('.mc-tab').forEach(t=>t.classList.remove('active'));
        btn.classList.add('active');

        currentSource = btn.dataset.source || 'participation';
        // 뷰 범위 다시 가져와 배지 갱신
        const v = calendar.view;
        try{
          const counts = await fetchCounts(v.currentStart, v.currentEnd, currentSource);
          renderBadges(calendarEl, counts, (d)=>openDayModal(d, currentSource));
        }catch(err){ console.error(err); }
      });
    }

    // 초기 배지
    const v = calendar.view;
    try{
      const counts = await fetchCounts(v.currentStart, v.currentEnd, currentSource);
      renderBadges(calendarEl, counts, (d)=>openDayModal(d, currentSource));
    }catch(err){ console.error(err); }
  };

  if (document.readyState === 'loading') document.addEventListener('DOMContentLoaded', onReady);
  else onReady();
})();
