package event2;

import java.sql.Clob;
import java.sql.Date;

public class Comment {
	int comment_id;
	int event_id;
	int user_id;
	String content;
	Date create_time;
	
	public Comment() {}
	
	public Comment(int comment_id, int event_id, int user_id, String content, Date create_time) {
		super();
		this.comment_id = comment_id;
		this.event_id = event_id;
		this.user_id = user_id;
		this.content = content;
		this.create_time = create_time;
	}
	
	
	public int getComment_id() {
		return comment_id;
	}


	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
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


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


	@Override
	public String toString() {
		return "Comment [comment_id=" + comment_id + ", event_id=" + event_id + ", user_id=" + user_id + ", content="
				+ content + ", create_time=" + create_time + "]";
	}

}
