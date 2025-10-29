package event2;

import java.util.Date;

public class Comment {
    private int commentId;
    private int eventId;
    private String userId;
    private String content;
    private Date createTime;

    public Comment() {}

    public Comment(int commentId, int eventId, String userId, String content, Date createTime) {
        this.commentId = commentId;
        this.eventId = eventId;
        this.userId = userId;
        this.content = content;
        this.createTime = createTime;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}