package stream.model;

import java.util.HashSet;
import java.util.Set;

public class VideoMetadataResponse {

	private Long videoId;
	private String title;
	private String userName;
	private Long userId;
	private String description;
	private String contentType;
	private String previewUrl;
	private String streamUrl;
	private Long views;
	private String status;
	
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPreviewUrl() {
		return previewUrl;
	}
	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	public String getStreamUrl() {
		return streamUrl;
	}
	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getViews() {
		return views;
	}
	public void setViews(Long views) {
		this.views = views;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
	
	
}
