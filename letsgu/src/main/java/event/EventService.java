package event;

import java.util.List;

public class EventService {

	EventDAO dao = new EventDAO();
	
	
	// 이벤트 전체 조회 (전체 지역 + 전체 카테고리 - 페이징 포함)
	public List<Event> getEventList(int currentPage, int pageSize){
		return dao.findAllEvents(currentPage, pageSize);
	}
	
	// 이벤트 전체 조회 (전체 지역 + 특정 카테고리)
	public List<Event> getEventListByCategory(int categoryId){
		return dao.findEventsByCategory(categoryId);
	}
	
	// 이벤트 전체 조회 (특정 지역 + 전체 카테고리)
	public List<Event> getEventListByRegion(String region){
		return dao.findEventsByRegion(region);
	}
	
	// 이벤트 전체 조회 (특정 지역 + 특정 카테고리)
	public List<Event> getEventListByRegionAndCategory(String region, int categoryId){
		return dao.findEventsByRegionAndCategory(region, categoryId);
	}
	
	//지역 이름 리스트 조회
	public List<String> getRegionList(){
		return dao.findAllRegion();
	}
	
	
	//총 레코드 수 가져오기(페이징 처리)
		public int getTotalCount() {
			return dao.countAll();
			
		}
		
		//이벤트 상세조회
		public Event getEventById(int eventId) {
			return dao.findEventById(eventId);
		}
		
		
		//이벤트 등록
		public boolean regEvent(Event event) {
			int cnt = dao.insertEvent(event);
			
			boolean result = cnt > 0 ? true : false;
			return result;
		}
		
		//이벤트 수정(본인 글만)
		public boolean modifyEvent(Event event) {
			int cnt = dao.updateEvent(event);
			
			boolean result = cnt > 0 ? true : false;
			return result;
		}
		
		
		//이벤트 삭제
		public boolean deleteEvent(int eventId, int authorId) {
			int cnt = dao.deleteEvent(eventId, authorId);
			
			boolean result = cnt > 0 ? true : false;
			return result;
		}
		
		//비활성화된 이벤트 조회(관리자용)
		public List<Event> getInactiveEventList(){
			return dao.findInactiveEventList();
		}
		
		//비활성화된 이벤트 삭제(관리자용)
		public boolean deleteInactiveEvent() {
			int cnt = dao.deleteInactive();
			
			boolean result = cnt > 0 ? true : false;
			return result;
		}
		
}
