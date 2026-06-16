package com.lms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lms.dto.DepartmentDTO;
import com.lms.dto.FacultyReportDTO;
import com.lms.entity.Role;
import com.lms.entity.User;
import com.lms.exception.InvalidUsernameOrPasswordException;
import com.lms.exception.UserNotFoundException;
import com.lms.repository.LeaveRepository;
import com.lms.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private Cloudinary cloudinary;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private LeaveRepository leaveRepo;
	
	public User getUserById(int id) {
		
		return userRepo.findById(id)
			.orElseThrow(()->new UserNotFoundException(id));
	}
	
	public User login(String username,String password) {
		User loginUser=userRepo.findByUsername(username)
				.orElseThrow(()->new InvalidUsernameOrPasswordException());
		
		if(!loginUser.getPassword().equals(password)) {
			throw new InvalidUsernameOrPasswordException();
		}
		return loginUser;
		
	}
	
	// Saving User
	public User saveUser(User newUser) {
		return userRepo.save(newUser);
	}
	
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	public List<User> searchByNameAndEamil(String name, String email){
		return userRepo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, email);
	}
	
	public void deleteUser(int id) {
		User user=userRepo.findById(id)
						.orElseThrow(()->new UserNotFoundException(id));
			
		leaveRepo.deleteByUserId(user.getId());
		userRepo.delete(user);
	}
	public User updateUser(int id, User updatedUser) {

	    User user = userRepo.findById(id)
	            .orElseThrow(() ->
	                    new UserNotFoundException(id));

	    // Common Fields
	    user.setName(updatedUser.getName());
	    user.setEmail(updatedUser.getEmail());
	    user.setDepartment(updatedUser.getDepartment());
	    user.setMobileNumber(updatedUser.getMobileNumber());

	    // Student Fields
	    if (user.getRole() == Role.STUDENT) {

	        user.setRollNumber(updatedUser.getRollNumber());
	        user.setYear(updatedUser.getYear());
	        user.setSemester(updatedUser.getSemester());
	        user.setSection(updatedUser.getSection());

	    }

	    // Faculty/Admin Fields
	    else {

	        user.setEmployeeId(updatedUser.getEmployeeId());
	        user.setDesignation(updatedUser.getDesignation());
	    }

	    // Password Update
	    if (updatedUser.getPassword() != null &&
	            !updatedUser.getPassword().isBlank()) {

	        user.setPassword(updatedUser.getPassword());
	    }

	    return userRepo.save(user);
	}
	
	public List<DepartmentDTO> getDepartments() {

	    List<Object[]> data = userRepo.getDepartmentStats();

	    return data.stream()
	        .map(row -> new DepartmentDTO(
	            (String) row[0],
	            (Long) row[1],
	            (Long) row[2],
	            (Long) row[3]
	        ))
	        .toList();
	}
	
	//
	public User updateUserDepartment(int id, String dept) {
		User user=userRepo.findById(id)
					.orElseThrow(()->new UserNotFoundException(id));
		user.setDepartment(dept);
		return userRepo.save(user);
	}
	
	public List<Map<String, Object>> getStudentsWithFaculty() {
	    return userRepo.getStudentsWithFaculty();
	}
	
	public List<User> getAllFaculty(){
		return userRepo.findByRole(Role.FACULTY);
	}
	
	public User assignFaculty(int studentId, int facultyId) {

	    User student = userRepo.findById(studentId)
	            .orElseThrow(() -> new UserNotFoundException(studentId));

	    User faculty = userRepo.findById(facultyId)
	            .orElseThrow(() -> new UserNotFoundException(facultyId));

	    student.setFacultyId(faculty);

	    return userRepo.save(student);
	}
	
	public List<FacultyReportDTO> getFacultyPerformanceReport() {

	    List<Object[]> rows = userRepo.getFacultyPerformanceReport();

	    return rows.stream()
	            .map(row -> new FacultyReportDTO(
	                    ((Number) row[0]).intValue(),
	                    (String) row[1],
	                    (String) row[2],
	                    ((Number) row[3]).longValue(),
	                    ((Number) row[4]).longValue(),
	                    ((Number) row[5]).doubleValue()
	            ))
	            .toList();
	}
	
	public List<User> getAssignedStudents(int facultyId){
		return userRepo.findByFacultyIdIdAndRole(facultyId, Role.STUDENT);
	}
	
	
	
	public User uploadProfileImage(int id, MultipartFile file) throws IOException {

	    // Upload to Cloudinary
	    Map uploadResult = cloudinary.uploader().upload(
	        file.getBytes(),
	        ObjectUtils.asMap("folder", "leaveflow/profiles")
	    );

	    // Get the secure URL
	    String imageUrl = (String) uploadResult.get("secure_url");

	    // Save URL to user
	    User user = userRepo.findById(id)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setProfileImage(imageUrl);

	    return userRepo.save(user);
	}
}
