package stream.service;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import stream.dao.UserRepository;
import stream.model.AuthRequest;
import stream.model.User;
import stream.security.JwtTokenProvider;

@Service
public class AuthenticateService {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticateService.class); 
	private final AuthenticationManager authManager;
	private final JwtTokenProvider jwtProvider;
	private final UserRepository userDao;
	
	@Autowired
	public AuthenticateService(AuthenticationManager authManager, JwtTokenProvider jwtProvider,
			UserRepository userDao) {
		super();
		this.authManager = authManager;
		this.jwtProvider = jwtProvider;
		this.userDao = userDao;
	}


	@Transactional
	public Map<Object, Object> getAuthenticate(AuthRequest request) throws UsernameNotFoundException, AuthenticationException {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
			User user = userDao.findByEmail(request.getUserName()).orElseThrow(()->
					new UsernameNotFoundException(request.getUserName()));
			logger.info(user.getEmail());
			String token = jwtProvider.createToken(request.getUserName(), user.getRole().name());
			Map<Object, Object> response = new HashMap<>();
			response.put("username", request.getUserName());
			response.put("token", token);
			return response;
	}


}
