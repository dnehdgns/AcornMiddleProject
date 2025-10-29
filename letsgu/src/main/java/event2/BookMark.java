package event2;

import java.util.Date;

public class BookMark {
	int event_id;
	int user_id;
	Date date;
	public BookMark(int event_id, int user_id, Date date) {
		super();
		this.event_id = event_id;
		this.user_id = user_id;
		this.date = date;
	}
	
	public int getEvent_id() {
		return event_id;
	}
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "BookMark [event_id=" + event_id + ", user_id=" + user_id + ", date=" + date + "]";
	}
	
}
