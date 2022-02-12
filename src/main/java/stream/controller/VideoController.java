package stream.controller;

import java.io.InputStream;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import stream.exception.NotFoundException;
import stream.model.StreamBytesInfo;
import stream.model.VideoMetadataRequest;
import stream.model.VideoMetadataResponse;
import stream.service.VideoService;

@RestController
@RequestMapping("/api/video")
@CrossOrigin("*")
public class VideoController {

	private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
	
	private final VideoService videoService;
	
	@Autowired
	public VideoController(VideoService videoService) {
		super();
		this.videoService = videoService;
	}

	
	@GetMapping("/all")
	public List<VideoMetadataResponse> getAllVideo(){
		
		return videoService.getAllAccessVideo();
	}
	
	@GetMapping("/{videoId}")
	public ResponseEntity<?> getVideoById(@PathVariable("videoId") Long id) throws NotFoundException {
		
			return ResponseEntity.ok(videoService.getVideoById(id));
	}
	
	@GetMapping(value="/preview/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<StreamingResponseBody> getPreviewVideo(@PathVariable("id") Long id) throws NotFoundException{
		InputStream is = videoService.getPreviewInputStream(id).orElseThrow(NotFoundException::new);
		return ResponseEntity.ok(is::transferTo);
	}
	
	@GetMapping("/stream/{id}")
	public ResponseEntity<StreamingResponseBody> getStreamVideo(@RequestHeader(value="Range", required = false) String httpRangeHeader,
			@PathVariable("id") Long id) throws NotFoundException{
		
		List<HttpRange> httpRangeList =  HttpRange.parseRanges(httpRangeHeader);
		StreamBytesInfo stream = videoService.getStreamBytes(id, httpRangeList.size()>0?httpRangeList.get(0):null)
				.orElseThrow(NotFoundException::new);
		
		Long byteLength = stream.getRangeEnd()-stream.getRangeStart()+1;
        ResponseEntity.BodyBuilder builder = ResponseEntity.
        		status(httpRangeList.size() > 0 ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .header("Content-Type", stream.getContentType())
                .header("Accept-Ranges", "bytes")
                .header("Content-Length", Long.toString(byteLength));
				
		if(httpRangeList.size()>0) {
			builder.header("Content-Range", 
					"bytes "+stream.getRangeStart()+
					"-"+stream.getRangeEnd()+
					"/"+stream.getFileSize());
		}
		logger.info("Providing bytes from {} to {}% of overall  video.",
				stream.getRangeStart(), stream.getRangeEnd(),
				new DecimalFormat("###.##")
					.format(100.0*stream.getRangeStart()/stream.getFileSize()));
				
		return builder.body(stream.getResponse());
	}
	
	@PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<?> uploadVideo(VideoMetadataRequest request, Principal principal){
		try {
			videoService.saveNewVideo(request, principal.getName());
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	
	
	
	
	
	@ExceptionHandler
	public ResponseEntity<Void> notFoundException(NotFoundException ex){
		logger.error("",ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
