package sru.edu.SchoolRouteMgt.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sru.edu.SchoolRouteMgt.domain.*;
import sru.edu.SchoolRouteMgt.repository.*;

@Service
public class UserService {
		
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	
	public void save(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); 
		String encodedPassword = encoder.encode(user.getPassword()); // Encrypts the password // 
		user.setPassword(encodedPassword);
		userRepo.save(user);
	}
	/*
	public void save(User user) {
	    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); 
	    String encodedPassword = encoder.encode(user.getPassword()); // Encrypts the password // 
	    user.setPassword(encodedPassword);
	    userRepo.save(user);
	    sendPasswordEmail(user);
	}

	*/
	@Autowired
	private JavaMailSender mailSender;

	public void sendPasswordEmail(User user) {
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setFrom("pasha.avdejevs@gmail.com");
	    message.setTo(user.getEmail());
	    message.setSubject("Your SchoolRouteMgt Password");
	    message.setText("Dear " + user.getFirstName() + ",\n\n" +
	        "Your password for SchoolRouteMgt is: " + user.getPassword() + "\n\n" +
	        "Please keep this information secure and do not share it with anyone.\n\n" +
	        "Best regards,\n" +
	        "SchoolRouteMgt Team");
	    mailSender.send(message);
	}

	
	
	
	public List<User> listAll(){
		return userRepo.findAll();
	}
	public User get(Long id){
		return userRepo.findById(id).get();
	}
	
	public List<Role> getRoles(){
		return roleRepo.findAll();
	}
	
	
}

