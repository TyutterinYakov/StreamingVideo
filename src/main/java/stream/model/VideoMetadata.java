package stream.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@Column(length=5000)
	private String description;
	@Column(name="file_size")
	private Long fileSize;
	@Column(name="video_length")
	private Long videoLength;
	
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
	
	public VideoMetadataResponse convertToVideoMetadataResponse() {
		VideoMetadataResponse response = new VideoMetadataResponse();
		response.setContentType(this.contentType);
		response.setDescription(this.description);
		response.setPreviewUrl("/api/v1/video/preview/"+this.videoId);
		response.setStreamUrl("/api/v1/video/stream/"+this.videoId);
		response.setVideoId(this.videoId);
		return response;
	}
	
	
	
}
