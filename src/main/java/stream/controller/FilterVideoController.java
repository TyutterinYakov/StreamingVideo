package stream.controller;

import java.security.Principal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stream.exception.BadRequestException;
import stream.exception.NotFoundException;
import stream.exception.UserNotFoundException;
import stream.model.GradeVideoRequest;
import stream.service.FilterVideoService;

@RestController
@RequestMapping("/api/filter")
@CrossOrigin("*")
public class FilterVideoController {

	private static final Logger logger = LoggerFactory.getLogger(FilterVideoService.class);
	
	private final FilterVideoService filterService;

	public FilterVideoController(FilterVideoService filterService) {
		super();
		this.filterService = filterService;
	}
	
	//Получение видео на канале
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getAccessVideoChannelUser(@PathVariable("userId") Long id) 
			throws UserNotFoundException, BadRequestException{
		if(id==null||id==0) {
			throw new BadRequestException();
		}
		return ResponseEntity.ok(filterService.getAccessVideoUser(id));
	}
	
	//Получение списка видео для творческой студии(Даже скрытых)
	@PreAuthorize("hasAuthority('user:read')")
	@GetMapping("/user/all")
	public ResponseEntity<?> getAllVideoUser(Principal principal) throws UserNotFoundException{
		return ResponseEntity.ok(filterService.getAllVideoUser(principal.getName()));
	}
	
	@DeleteMapping("/user/{id}")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<?> deleteVideoUser(Principal principal, @PathVariable("id") Long id) 
			throws NotFoundException, UserNotFoundException{
		filterService.deleteVideoUser(id, principal.getName());
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/user/grade")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<?> gradeVideoUser(GradeVideoRequest request, Principal principal) 
			throws UserNotFoundException, NotFoundException{
		filterService.gradeVideoAdd(principal.getName(), request);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/user/access")
	@PreAuthorize("hasAuthority('user:read')")
	public ResponseEntity<?> changeVideoAccess(Long videoId, String status, Principal principal) 
			throws UserNotFoundException, NotFoundException{
		filterService.changeAccessVideoUser(videoId, principal.getName(), status);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	
	
	
	
	
	@ExceptionHandler
	public ResponseEntity<Void> userNotFoundException(UserNotFoundException ex){
		logger.error("",ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ExceptionHandler
	public ResponseEntity<Void> badRequestException(BadRequestException ex){
		logger.error("",ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	@ExceptionHandler
	public ResponseEntity<Void> notFoundException(NotFoundException ex){
		logger.error("",ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	@ExceptionHandler
	public ResponseEntity<Void> nullPointerException(NullPointerException ex){
		logger.error("",ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
}
