package com.lms.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	@Column(length=100)
	private String name;
	
	@Column(unique=true, nullable=false,length=50)
	private String username;
	
	@Column(nullable=false, length=100)
	private String password;
	
	@Column(length=100)
	private String email;
	
	@Column(length=50)
	private String department;
	
	// Profile
	@Column(length = 500)
	private String profileImage;

	// Common
	@Column(length = 15)
	private String mobileNumber;

	// Student Fields
	@Column(length = 20)
	private String rollNumber;      // S190237

	@Enumerated(EnumType.STRING)
	@Column(nullable=true)
	private Year year;             // 1st Year, 2nd Year

	@Column(length = 20)
	private String semester;

	@Enumerated(EnumType.STRING)
	private Section section;       // A, B, C

	// Faculty/Admin Fields
	@Column(length = 20)
	private String employeeId;      // FAC101, ADM001

	@Column(length = 100)
	private String designation;     // Professor, HOD, Administrator
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Role role;
	
	@ManyToOne
	@JoinColumn(name="faculty_id")
	private User facultyId;
	
	@Column(name="created_at",updatable=false)
	private LocalDateTime createdAt;
	
	
	
	//Getters and Setters
	
	@PrePersist
	public void onCreate() {
		this.createdAt=LocalDateTime.now();
	}
	
	public void setFacultyId(User facultyId) {
		this.facultyId=facultyId;
	}
	public User getFacultyId() {
		return facultyId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}
	
	
	
	
	
	
	
	
}
