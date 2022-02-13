package stream.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRange;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import stream.Utils.Utils;
import stream.dao.UserRepository;
import stream.dao.VideoMetadataRepository;
import stream.exception.NotFoundException;
import stream.model.StatusVideo;
import stream.model.StreamBytesInfo;
import stream.model.User;
import stream.model.VideoMetadata;
import stream.model.VideoMetadataRequest;
import stream.model.VideoMetadataResponse;

import static java.nio.file.StandardOpenOption.WRITE;
import static java.nio.file.StandardOpenOption.CREATE;

@Service
public class VideoService {

	private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
	
	@Value("${data.folder}")
	private String dataFolder;
	
	private final VideoMetadataRepository videoDao;
	private final FrameGrabberService frameService;
	private final UserRepository userDao;
	
	@Autowired
	public VideoService(VideoMetadataRepository videoDao, FrameGrabberService frameService, UserRepository userDao) {
		super();
		this.videoDao = videoDao;
		this.frameService = frameService;
		this.userDao = userDao;
	}
	
	public List<VideoMetadataResponse> getAllAccessVideo(){
		return videoDao.findAllByStatus(StatusVideo.PUBLIC)
				.stream().map(
						vmd->{
							return vmd.convertToVideoMetadataResponse();
						}).collect(Collectors.toList());
	}
	

	public VideoMetadataResponse getVideoById(Long id) throws NotFoundException {
		VideoMetadata video = videoDao.findByVideoIdAndStatus(id, StatusVideo.PUBLIC.name(), 
				StatusVideo.LINK.name()).orElseThrow(NotFoundException::new);
		video.setViews(video.getViews()+1L);
		videoDao.save(video);
		return video.convertToVideoMetadataResponse();
	}
	
	@Transactional
	public void saveNewVideo(VideoMetadataRequest request, String userName) throws NotFoundException {
		if(request.getFile()==null) {
			throw new NotFoundException();
		}
		User user = userDao.findByEmail(userName).orElseThrow(()->
				new UsernameNotFoundException(userName));
		
		
		VideoMetadata video = new VideoMetadata();
		video.setFileName(request.getFile().getOriginalFilename());
		video.setContentType(request.getFile().getContentType());
		video.setDescription(request.getDescription());
		video.setFileSize(request.getFile().getSize());
		video.setUser(user);
		video = videoDao.save(video);
		
		generatePreviewImage(request, video);
	}

	
	public Optional<InputStream> getPreviewInputStream(Long id){
		return videoDao.findById(id)
				.flatMap(vmd->{
					Path previewImagePath = Path.of(dataFolder,
							vmd.getVideoId().toString(),
							Utils.removeFileExt(vmd.getFileName())+".jpeg");
					if(!Files.exists(previewImagePath)) {
						return Optional.empty();
					}
					try {
						return Optional.of(Files.newInputStream(previewImagePath));
					} catch(IOException ex) {
						logger.error("", ex);
						return Optional.empty();
					}
				});
	}
	
	
	public Optional<StreamBytesInfo> getStreamBytes(Long id, HttpRange range) throws NotFoundException{
		VideoMetadata video = videoDao.findById(id).orElseThrow(NotFoundException::new);
		Path path = Path.of(dataFolder, Long.toString(id), video.getFileName());
		if(!Files.exists(path)) {
			logger.error(dataFolder);
			throw new NotFoundException();
		}
		try {
			Long fileSize =Files.size(path); 
			Long chunkSize = fileSize/20; 
			if(range==null) {
				return Optional.of(new StreamBytesInfo(
						out->Files.newInputStream(path).transferTo(out),
						fileSize, 0L, fileSize, video.getContentType()
						));
			}
			Long rangeStart = range.getRangeStart(0);
			Long rangeEnd = rangeStart+chunkSize;
			if(rangeEnd>=fileSize) {
				rangeEnd=fileSize-1;
			}
			Long finalRangeEnd = rangeEnd;
			return Optional.of(new StreamBytesInfo(
					out->{
						try(InputStream is = Files.newInputStream(path)) {
							is.skip(rangeStart);
							byte[] bytes = is.readNBytes((int)(finalRangeEnd-rangeStart+1));
							out.write(bytes);
						}
					}, fileSize, rangeStart, finalRangeEnd, video.getContentType()));
		} catch(IOException ex) {
			logger.error("",ex);
			throw new NotFoundException();
			
		}
	}
	
	
	private VideoMetadata generatePreviewImage(VideoMetadataRequest request, VideoMetadata video) {
		Path directory = Path.of(dataFolder, video.getVideoId().toString());
		try {
			Files.createDirectories(directory);
			Path file = Path.of(directory.toString(), request.getFile().getOriginalFilename());
			try(OutputStream os = Files.newOutputStream(file, CREATE, WRITE)){
				request.getFile().getInputStream().transferTo(os);
			}
			Long lengthVideo = frameService.generatePreviewPictures(file);
			video.setVideoLength(lengthVideo);
			return videoDao.save(video);
		} catch(IOException ex) {
			logger.error("", ex);
			throw new IllegalStateException();
		}
	}
	
	
	
	
}
