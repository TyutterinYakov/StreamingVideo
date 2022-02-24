package stream.store.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="comments")
public class Comment {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long commentId;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private VideoMetadata video;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private User user;
	@Column(length=3000)
	private String description;
	
	public Long getCommentId() {
		return commentId;
	}
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	public VideoMetadata getVideo() {
		return video;
	}
	public void setVideo(VideoMetadata video) {
		this.video = video;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
}
