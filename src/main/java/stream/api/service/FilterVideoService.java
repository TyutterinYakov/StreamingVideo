package stream.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stream.api.exception.NotFoundException;
import stream.api.exception.UserNotFoundException;
import stream.api.model.GradeVideoRequest;
import stream.api.model.VideoMetadataResponse;
import stream.store.dao.GradeUserVideoRepository;
import stream.store.dao.UserRepository;
import stream.store.dao.VideoMetadataRepository;
import stream.store.entity.GradeUserVideo;
import stream.store.entity.StatusGrade;
import stream.store.entity.StatusVideo;
import stream.store.entity.User;
import stream.store.entity.VideoMetadata;

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
	


	@Transactional
	public List<VideoMetadataResponse> getAccessVideoUser(Long id) throws UserNotFoundException {
		User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
		return videoDao.findAllByUserAndStatus(user, StatusVideo.PUBLIC).stream().map(
				vmd->{
					return vmd.convertToVideoMetadataResponse();
				}).collect(Collectors.toList());
	}
	
	@Transactional
	public List<VideoMetadataResponse> getAllVideoUser(String userName) throws UserNotFoundException{
		User user = userDao.findByEmail(userName).orElseThrow(UserNotFoundException::new);
		return videoDao.findByUserAndStatusIsNot(user, StatusVideo.DELETE).stream().map(
				vmd->{
					return vmd.convertToVideoMetadataResponse();
				}).collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteVideoUser(Long id, String userName) throws NotFoundException, UserNotFoundException {
		User user = userDao.findByEmail(userName).orElseThrow(UserNotFoundException::new);		
		VideoMetadata video = videoDao.findByVideoIdAndUser(id, user).orElseThrow(NotFoundException::new);
		video.setStatus(StatusVideo.DELETE);
		videoDao.save(video);
	}

	@Transactional
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
	@Transactional
	public void changeAccessVideoUser(Long videoId, String userName, String status) throws UserNotFoundException, NotFoundException {
		User user = userDao.findByEmail(userName).orElseThrow(UserNotFoundException::new);
		VideoMetadata video = videoDao.findByUserAndVideoIdAndStatusIsNot(user, videoId, StatusVideo.DELETE).orElseThrow(NotFoundException::new);
		for(StatusVideo s : StatusVideo.values()) {
			if(s.name().equals(status)) {
				video.setStatus(s);
				videoDao.save(video);
				break;
			}
		}
		
	}
	@Transactional
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
