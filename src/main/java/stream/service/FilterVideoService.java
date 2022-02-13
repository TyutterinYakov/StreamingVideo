package stream.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stream.dao.GradeUserVideoRepository;
import stream.dao.UserRepository;
import stream.dao.VideoMetadataRepository;
import stream.exception.NotFoundException;
import stream.exception.UserNotFoundException;
import stream.model.GradeUserVideo;
import stream.model.GradeVideoRequest;
import stream.model.StatusGrade;
import stream.model.StatusVideo;
import stream.model.User;
import stream.model.VideoMetadata;
import stream.model.VideoMetadataResponse;

@Service
public class FilterVideoService {

	private final UserRepository userDao;
	private final VideoMetadataRepository videoDao;
	private final GradeUserVideoRepository gradeDao;
	
	@Autowired
	public FilterVideoService(UserRepository userDao, VideoMetadataRepository videoDao,
			GradeUserVideoRepository gradeDao) {
		super();
		this.userDao = userDao;
		this.videoDao = videoDao;
		this.gradeDao = gradeDao;
	}
	



	public List<VideoMetadataResponse> getAccessVideoUser(Long id) throws UserNotFoundException {
		User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
		return videoDao.findAllByUserAndStatus(user, StatusVideo.PUBLIC).stream().map(
				vmd->{
					return vmd.convertToVideoMetadataResponse();
				}).collect(Collectors.toList());
	}
	

	public List<VideoMetadataResponse> getAllVideoUser(String userName) throws UserNotFoundException{
		User user = userDao.findByEmail(userName).orElseThrow(UserNotFoundException::new);
		return videoDao.findByUserAndStatusIsNot(user, StatusVideo.DELETE).stream().map(
				vmd->{
					return vmd.convertToVideoMetadataResponse();
				}).collect(Collectors.toList());
	}
	
	public void deleteVideoUser(Long id, String userName) throws NotFoundException, UserNotFoundException {
		User user = userDao.findByEmail(userName).orElseThrow(UserNotFoundException::new);		
		VideoMetadata video = videoDao.findByVideoIdAndUser(id, user).orElseThrow(NotFoundException::new);
		video.setStatus(StatusVideo.DELETE);
		videoDao.save(video);
	}

	public void gradeVideoAdd(String userName, GradeVideoRequest request) throws UserNotFoundException, NotFoundException {
		User user = userDao.findByEmail(userName).orElseThrow(UserNotFoundException::new);
		VideoMetadata video = videoDao.findByVideoIdAndStatus(request.getVideoId(), 
				StatusVideo.LINK.name(), StatusVideo.PUBLIC.name()).orElseThrow(NotFoundException::new);
		Optional<GradeUserVideo> grade =  gradeDao.findByUserAndVideo(user, video);
		if(request.getGrade().equals(StatusGrade.LIKE.name())) {
			gradeDao.save(setStatusGrade(grade, StatusGrade.LIKE, user, video));
		} else if(request.getGrade().equals(StatusGrade.DISLIKE.name())){
			gradeDao.save(setStatusGrade(grade, StatusGrade.DISLIKE, user, video));
		} else {
			if(grade.isPresent()) {
				gradeDao.delete(grade.get());
			}
		}
		
	}
	
	
	private GradeUserVideo setStatusGrade(Optional<GradeUserVideo> opt, StatusGrade status, User user, VideoMetadata video) {
		GradeUserVideo grade = new GradeUserVideo();
		if(opt.isPresent()) {
			grade = opt.get();
			grade.setGrade(status);
		} else {
			grade.setGrade(status);
			grade.setUser(user);
			grade.setVideo(video);
		}
		return grade;		
	}




}
