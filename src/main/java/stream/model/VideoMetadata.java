package stream.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class VideoMetadata {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long videoId;
	@Column(name="file_name")
	private String fileName;
	@Column(name="content_type")
	private String contentType;
	private String title;
	@Column(length=5000)
	private String description;
	@Column(name="file_size")
	private Long fileSize;
	@Column(name="video_length")
	private Long videoLength;
	private Long views=0L;
	@Enumerated(value = EnumType.STRING)
	private StatusVideo status;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private User user;
	@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Set<GradeUserVideo> grades = new HashSet<>();
	@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Set<Comment> comments = new HashSet<>();
	
	
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public Long getVideoLength() {
		return videoLength;
	}
	public void setVideoLength(Long videoLength) {
		this.videoLength = videoLength;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Long getViews() {
		return views;
	}
	public void setViews(Long views) {
		this.views = views;
	}	
	public StatusVideo getStatus() {
		return status;
	}
	public void setStatus(StatusVideo status) {
		this.status = status;
	}
	public Set<GradeUserVideo> getGrades() {
		return grades;
	}
	public void setGrades(Set<GradeUserVideo> grades) {
		this.grades = grades;
	}
	public Set<Comment> getComments() {
		return comments;
	}
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	
	public VideoMetadataResponse convertToVideoMetadataResponse() {
		VideoMetadataResponse response = new VideoMetadataResponse();
		response.setContentType(this.contentType);
		response.setDescription(this.description);
		response.setPreviewUrl("http://localhost:8080/api/video/preview/"+this.videoId);
		response.setStreamUrl("http://localhost:8080/api/video/stream/"+this.videoId);
		response.setVideoId(this.videoId);
		response.setUserName(user.getName());
		response.setUserId(user.getUserId());
		response.setTitle(this.title);
		response.setViews(this.views);
		response.setStatus(this.status.name());
		return response;
	}
	
	
	
}
