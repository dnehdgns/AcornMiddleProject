package event1;

public class PageHandler {

	private int pageSize;
	private int grpSize;
	private int totalRecords;
	private int currentPage;

	private int totalPage;
	private int currentGrp;
	private int grpStart;
	private int grpEnd;

	
	public PageHandler() {
	}

	public PageHandler(int pageSize, int grpSize, int totalRecords, int currentPage) {
		this.pageSize = pageSize;
		this.grpSize = grpSize;
		this.totalRecords = totalRecords;
		this.currentPage = currentPage;

		pageCalc();
	}

	
	private void pageCalc() {
		int remain = totalRecords % pageSize;

		if (remain == 0) {
			totalPage = totalRecords / pageSize;
		} else {
			totalPage = (totalRecords / pageSize) + 1;
		}

		int remain2 = currentPage % grpSize;

		if (remain2 == 0) {
			currentGrp = currentPage / grpSize;
		} else {
			currentGrp = (currentPage / grpSize) + 1;
		}

		grpStart = (currentGrp - 1) * grpSize + 1;
		grpEnd = currentGrp * grpSize;

		if (grpEnd > totalPage) {
			grpEnd = totalPage;
		}
	}

	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getGrpSize() {
		return grpSize;
	}

	public void setGrpSize(int grpSize) {
		this.grpSize = grpSize;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentGrp() {
		return currentGrp;
	}

	public void setCurrentGrp(int currentGrp) {
		this.currentGrp = currentGrp;
	}

	public int getGrpStart() {
		return grpStart;
	}

	public void setGrpStart(int grpStart) {
		this.grpStart = grpStart;
	}

	public int getGrpEnd() {
		return grpEnd;
	}

	public void setGrpEnd(int grpEnd) {
		this.grpEnd = grpEnd;
	}

}
