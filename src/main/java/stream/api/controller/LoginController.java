package stream.api.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import stream.api.exception.UserFoundException;
import stream.api.model.AuthRequest;
import stream.api.service.AuthenticateService;
import stream.api.service.UserDetailsServiceImpl;
import stream.store.entity.User;

@RestController
@RequestMapping("/api/login")
@CrossOrigin("*")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	private final UserDetailsServiceImpl userService;
	private final AuthenticateService authService;

	@Autowired
	public LoginController(UserDetailsServiceImpl userService,
			AuthenticateService authService) {
		super();
		this.userService = userService;
		this.authService = authService;
	}
	
	
	@PostMapping("/generate-token")
	public ResponseEntity<?> authenticate(@RequestBody AuthRequest request){
		try {
			return ResponseEntity.ok(authService.getAuthenticate(request));
		} catch(AuthenticationException ex) {
			logger.error(request.getUserName(), ex);
			return new ResponseEntity<>("Неправильный логин/пароль", HttpStatus.FORBIDDEN);
		}
	}



	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response){
		SecurityContextLogoutHandler securityHandler = new SecurityContextLogoutHandler();
		securityHandler.logout(request, response, null);
	}
	
	@GetMapping("/current-user")
	public User getCurrentUser(Principal principal) {
		logger.info(principal.getName());
		return userService.getUser(principal.getName());
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			return ResponseEntity.ok(userService.createUser(user));
		} catch(UserFoundException ex) {
			return new ResponseEntity<>("Пользователь с таким email уже зарегистрирован", HttpStatus.BAD_REQUEST);
		}
		
		
	}
	
}
