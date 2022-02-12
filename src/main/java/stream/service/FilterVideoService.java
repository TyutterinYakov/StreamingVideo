package stream.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stream.dao.UserRepository;
import stream.dao.VideoMetadataRepository;
import stream.exception.UserNotFoundException;
import stream.model.StatusVideo;
import stream.model.User;
import stream.model.VideoMetadata;
import stream.model.VideoMetadataResponse;

@Service
public class FilterVideoService {

	private final UserRepository userDao;
	private final VideoMetadataRepository videoDao;
	
	@Autowired
	public FilterVideoService(UserRepository userDao, VideoMetadataRepository videoDao) {
		super();
		this.userDao = userDao;
		this.videoDao = videoDao;
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

}
