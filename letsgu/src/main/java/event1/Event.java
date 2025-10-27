package event1;

import java.sql.Date;

public class Event {
	private int eventId; // 이벤트 ID (PK)
	private int authorId; // 작성자 ID (USER FK)
	private int categoryId; // 카테고리 ID (CATEGORY FK)
	private String title; // 이벤트 제목
	private String region; // 지역명
	private Date eventDate; // 모집 종료일
	private int capacity; // 모집 인원 수
	private String description; // 이벤트 내용
	private String status; // 상태 (ACTIVE / INACTIVE)
	private Date createdAt; // 작성일자
	private String uploadImg; // 업로드된 이미지 파일명

	// 출력용
	private String categoryName; // 카테고리 이름
	private String authorName;

	public Event() {
	}

	// 이벤트 등록용
	public Event(int authorId, int categoryId, String title, String region, Date eventDate, int capacity,
			String description, String uploadImg) {
		super();
		this.authorId = authorId;
		this.categoryId = categoryId;
		this.title = title;
		this.region = region;
		this.eventDate = eventDate;
		this.capacity = capacity;
		this.description = description;
		this.uploadImg = uploadImg;
	}
	

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", authorId=" + authorId + ", categoryId=" + categoryId + ", categoryName="
				+ categoryName + ", title=" + title + ", region=" + region + ", eventDate=" + eventDate + ", capacity="
				+ capacity + ", description=" + description + ", status=" + status + ", createdAt=" + createdAt
				+ ", uploadImg=" + uploadImg + "]";
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getUploadImg() {
		return uploadImg;
	}

	public void setUploadImg(String uploadImg) {
		this.uploadImg = uploadImg;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

}
