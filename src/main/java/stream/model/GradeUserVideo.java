package stream.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class GradeUserVideo {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch=FetchType.EAGER,  cascade = CascadeType.REFRESH)
	private User user;
	@Enumerated(value = EnumType.STRING)
	private StatusGrade grade;
	@ManyToOne(fetch=FetchType.EAGER)
	private VideoMetadata video;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public StatusGrade getGrade() {
		return grade;
	}
	public void setGrade(StatusGrade grade) {
		this.grade = grade;
	}
	public VideoMetadata getVideo() {
		return video;
	}
	public void setVideo(VideoMetadata video) {
		this.video = video;
	}
	
	
	
}
