package com.lms.dto;

public class FacultyReportDTO {

    private int facultyId;
    private String facultyName;
    private String department;
    private Long totalStudents;
    private Long leavesManaged;
    private Double approvalRate;

    public FacultyReportDTO(
            int facultyId,
            String facultyName,
            String department,
            Long totalStudents,
            Long leavesManaged,
            Double approvalRate) {

        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.department = department;
        this.totalStudents = totalStudents;
        this.leavesManaged = leavesManaged;
        this.approvalRate = approvalRate;
    }

    // Getters
    public int getFacultyId() { return facultyId; }
    public String getFacultyName() { return facultyName; }
    public String getDepartment() { return department; }
    public Long getTotalStudents() { return totalStudents; }
    public Long getLeavesManaged() { return leavesManaged; }
    public Double getApprovalRate() { return approvalRate; }
}