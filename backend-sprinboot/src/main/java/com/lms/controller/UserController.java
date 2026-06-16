package com.lms.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.lms.dto.DepartmentDTO;
import com.lms.dto.FacultyReportDTO;
import com.lms.entity.User;
import com.lms.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private Cloudinary cloudinary;
	
	@Autowired
	private UserService service;
	
	@PostMapping("/login")
	public User validateLogin(@RequestBody User user) {
		return service.login(user.getUsername(), user.getPassword());
							
	}
	
	@PostMapping("/user")
	public User newUser(@RequestBody User newUser) {
		return service.saveUser(newUser);
	}
	
	@GetMapping("/user")
	public List<User> getAllUsers(){
		return service.getAllUsers();
	}
	
	@GetMapping("/user/search")
	public List<User> searchUser(
			@RequestParam(required=false) String name, 
			@RequestParam(required=false) String email){
		if ((name == null || name.isEmpty()) && (email == null || email.isEmpty())) {
	        return List.of(); // return empty instead of all users
	    }

	    return service.searchByNameAndEamil(
	            name == null ? "" : name,
	            email == null ? "" : email
	    );
	}
	
	@DeleteMapping("/user/delete/{id}")
	public void deleteUserById(@PathVariable int id) {
		service.deleteUser(id);
	}
	
	@PutMapping("/user/update/{id}")
	public User updateUser(
	        @PathVariable int id,
	        @RequestBody User updatedUser) {

	    System.out.println("===== UPDATE API HIT =====");
	    System.out.println("Role = " + updatedUser.getRole());
	    System.out.println("Year = " + updatedUser.getYear());
	    System.out.println("Section = " + updatedUser.getSection());
	    System.out.println("Semester = " + updatedUser.getSemester());

	    return service.updateUser(id, updatedUser);
	}
	
	@GetMapping("/user/departments")
	public List<DepartmentDTO> getDepartments(){
		return service.getDepartments();
	}
	
	@PutMapping("/user/{id}/department")
	public User updateUserDepartment(@PathVariable int id, @RequestParam String dept) {
		return service.updateUserDepartment(id, dept);
	}
	
	@GetMapping("/assign-faculty")
	public List<Map<String, Object>> getUserWithFaculty() {
	    return service.getStudentsWithFaculty();
	}
	
	@GetMapping("user/faculty")
	public List<User> getFacultyOnly(){
		return service.getAllFaculty();
	}
	
	@PutMapping("/assign-faculty/{studentId}")
	public User assignFaculty(
	        @PathVariable int studentId,
	        @RequestParam int facultyId) {

	    return service.assignFaculty(studentId, facultyId);
	}
	
	@GetMapping("/faculty-report")
	public List<FacultyReportDTO> getFacultyReport() {
	    return service.getFacultyPerformanceReport();
	}
	
	@GetMapping("/assigned-students/{facultyId}")
	public List<User> getAssignedStudents(@PathVariable int facultyId){
		return service.getAssignedStudents(facultyId);
	}
	
	@PostMapping(
		    value = "/user/upload-image/{id}",
		    consumes = "multipart/form-data"
		)
		public User uploadProfileImage(
		        @PathVariable int id,
		        @RequestParam("file") MultipartFile file)
		        throws IOException {

		    System.out.println("=== UPLOAD API HIT ===");
		    System.out.println("ID = " + id);
		    System.out.println("FILE = " + file.getOriginalFilename());

		    return service.uploadProfileImage(id, file);
		}
	
	@PutMapping("/department/update")
	public ResponseEntity<String> updateDepartmentName(
	        @RequestParam String oldDept,
	        @RequestParam String newDept
	) {
		
		 System.out.println("UPDATE DEPARTMENT HIT");
		    System.out.println(oldDept);
		    System.out.println(newDept);

	    service.updateDepartmentName(
	            oldDept,
	            newDept
	    );

	    return ResponseEntity.ok(
	            "Department Updated Successfully"
	    );
	}
	
	
}
