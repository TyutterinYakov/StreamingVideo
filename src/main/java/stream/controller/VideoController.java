package stream.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import stream.model.VideoMetadataRequest;
import stream.model.VideoMetadataResponse;

@RestController
@RequestMapping("/api/v1/video")
@CrossOrigin("*")
public class VideoController {

	private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
	
	@GetMapping("/all")
	public List<VideoMetadataResponse> getAllVideo(){
		
		return null;
	}
	
	@GetMapping("/{videoId}")
	public VideoMetadataResponse getVideoById(@PathVariable("videoId") Long id) {
		
		return null;
	}
	
	@GetMapping(value="/preview/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<StreamingResponseBody> getPreviewVideo(@PathVariable("id") Long id){
		
		return null;
	}
	
	@GetMapping("/stream/{id}")
	public ResponseEntity<StreamingResponseBody> getStreamVideo(@RequestHeader(value="Range", required = false) String httpRangeHeader,
			@PathVariable("id") Long id){
		
		return null;
	}
	
	@GetMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadVideo(VideoMetadataRequest request){
		
		return null;
	}
}
