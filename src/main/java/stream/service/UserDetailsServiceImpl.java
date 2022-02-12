package stream.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import stream.dao.UserRepository;
import stream.exception.UserFoundException;
import stream.model.AuthRequest;
import stream.model.Role;
import stream.model.User;
import stream.security.JwtTokenProvider;
import stream.security.SecurityUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	
	private final UserRepository userDao;
	
	@Autowired
	public UserDetailsServiceImpl(UserRepository userDao) {
		super();
		this.userDao = userDao;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return SecurityUser.fromUser(getUserByUsername(username));
	}


	public User getUser(String userName) throws UsernameNotFoundException {
		loadUserByUsername(userName);
		User user = userDao.findByEmail(userName).orElseThrow(()->
			new UsernameNotFoundException(userName));
		user.setPassword("");
		return user;
	}


	public User createUser(User user) throws UserFoundException {
		
		Optional<User> local = userDao.findByEmail(user.getEmail());
		if(local.isPresent()) {
			throw new UserFoundException();
		}
		user.setPassword(this.passwordEncoder().encode(user.getPassword()));
		user.setRole(Role.USER);
		userDao.save(user);
		return user;
	}
	
	
	@Transactional
	private User getUserByUsername(String userName) {
		return userDao.findByEmail(userName).orElseThrow(()->
			new UsernameNotFoundException(userName));
	}
	
	
	private PasswordEncoder passwordEncoder()
	{
	    return new BCryptPasswordEncoder(12);
	}

}
