package com.lms.dto;

public class DepartmentDTO {
	private String name;
    private long totalUsers;
    private long students;
    private long faculty;

    public DepartmentDTO(String name, long totalUsers, long students, long faculty) {
        this.name = name;
        this.totalUsers = totalUsers;
        this.students = students;
        this.faculty = faculty;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTotalUsers() {
		return totalUsers;
	}

	public void setTotalUsers(long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public long getStudents() {
		return students;
	}

	public void setStudents(long students) {
		this.students = students;
	}

	public long getFaculty() {
		return faculty;
	}

	public void setFaculty(long faculty) {
		this.faculty = faculty;
	}
    
    
}
