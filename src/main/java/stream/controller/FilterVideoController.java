package stream.controller;

import java.security.Principal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stream.exception.BadRequestException;
import stream.exception.UserNotFoundException;
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
	public ResponseEntity<?> getAccessVideoChannelUser(@PathVariable("userId") Long id) throws UserNotFoundException, BadRequestException{
		
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
	
}
