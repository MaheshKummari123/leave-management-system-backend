package com.lms.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import com.lms.dto.StudentLeaveDTO;
import com.lms.entity.Leave;
import com.lms.entity.Role;
import com.lms.entity.Status;
import com.lms.entity.User;
import com.lms.exception.LeaveNotFoundException;
import com.lms.exception.UserNotFoundException;
import com.lms.repository.LeaveRepository;
import com.lms.repository.UserRepository;

@Service
public class LeaveService {
	
	@Autowired
	private LeaveRepository leaveRepo;
	
	@Autowired
	private UserRepository repo;
	
	public List<Leave> getAllLeaves(){
		return leaveRepo.findAll();
	}
	
	public List<Leave> getAllLeavesById(int id){
		return leaveRepo.findByUserId(id);
	}
	
	public Leave saveLeave(Leave leave) {

	    int userId = leave.getUser().getId();

	    User student = repo.findById(userId)
	            .orElseThrow(() -> new UserNotFoundException(userId));

	    // Auto assign faculty if not assigned
	    if (student.getFacultyId() == null) {

	        List<User> faculties =
	                repo.findByDepartmentAndRole(
	                        student.getDepartment(),
	                        Role.FACULTY);

	        if (!faculties.isEmpty()) {

	            // First faculty in same department
	            student.setFacultyId(faculties.get(0));

	            repo.save(student);
	        }
	    }

	    leave.setUser(student);

	    return leaveRepo.save(leave);
	}
	
	public List<Leave> getLastThreeLeavesByStudentId(int id){
		return leaveRepo.findTop3ByUserIdOrderByAppliedAtDesc(id);
	}
	
	public Map<String,Integer> getUserLeaveCounts(int userId){
		Map<String, Integer> counts=new HashMap<>();
		counts.put("pending", leaveRepo.countByUserIdAndStatus(userId, Status.PENDING));
		counts.put("approved", leaveRepo.countByUserIdAndStatus(userId, Status.APPROVED));
		counts.put("rejected", leaveRepo.countByUserIdAndStatus(userId, Status.REJECTED));
		
		return counts;
	}
	
	public Leave updateStatus(int id,int facultyId,Status status) {

	    Leave leave = leaveRepo.findById(id)
	            .orElseThrow(() -> new LeaveNotFoundException(id));

	    User faculty = repo.findById(facultyId)
	            .orElseThrow(() -> new UserNotFoundException(facultyId));

	    // Only Faculty/Admin can approve
	    if (faculty.getRole() != Role.FACULTY &&
	        faculty.getRole() != Role.ADMIN) {
	        throw new RuntimeException("Only Faculty/Admin can approve leaves");
	    }

	    // Prevent self approval
	    if (leave.getUser().getId() == facultyId) {
	        throw new RuntimeException("Cannot approve your own leave");
	    }

	    leave.setApprovedBy(faculty);
	    leave.setStatus(status);

	    return leaveRepo.save(leave);
	}
	
	public List<Leave> getFacultyLeavesByDept(int facId) {
	    return leaveRepo.findByUserFacultyIdIdAndStatus(
	            facId,
	            Status.PENDING
	    );
	}
	
	public Map<String, Integer> getFacultyDashboardCounts(int facultyId){

	    Map<String, Integer> counts = new HashMap<>();

	    counts.put("pending",
	            leaveRepo.countByUserFacultyIdIdAndStatus(
	                    facultyId, Status.PENDING));

	    counts.put("approved",
	            leaveRepo.countByApprovedByIdAndStatus(
	                    facultyId, Status.APPROVED));

	    counts.put("rejected",
	            leaveRepo.countByApprovedByIdAndStatus(
	                    facultyId, Status.REJECTED));

	    return counts;
	}
	
	//Search Leaves by Status
	public List<Leave> searchLeaves(Integer userId, Status status,
            LocalDate start, LocalDate end, String keyword) {

			return leaveRepo.searchLeaves(userId, status, start, end, keyword);
	}
	
	public Map<String,Integer> getAllLeaveStatusCounts(){
		Map<String, Integer> counts=new HashMap<>();
		counts.put("pending", leaveRepo.countByStatus(Status.PENDING));
		counts.put("approved", leaveRepo.countByStatus(Status.APPROVED));
		counts.put("rejected", leaveRepo.countByStatus(Status.REJECTED));
		
		return counts;
	}
	
	public void deleteLeave(int id) {
		leaveRepo.deleteById(id);
	}
	
	public List<Leave> top5Leaves(){
		return leaveRepo.findTop5ByOrderByAppliedAtDesc();
	}
	
	public List<Leave> getLeavesByAppliedDate(LocalDate date) {
	    return leaveRepo.findLeavesBetween(
	            date.atStartOfDay(),
	            date.atTime(23, 59, 59)
	    );
	}
	public List<Leave> getLeavesByAppliedDateRange(
	        LocalDate startDate,
	        LocalDate endDate) {

	    return leaveRepo.findLeavesBetween(
	            startDate.atStartOfDay(),
	            endDate.atTime(23, 59, 59)
	    );
	}
	
	//Month wise leaves
	public List<Object[]> getMonthWiseLeaves(){
			return leaveRepo.getMonthWiseLeaves();
	}
	
	public List<Object[]> getDepartmentLeaveCounts(){
		return leaveRepo.getDepartmentWiseLeaveCount();
	}
	
	public List<Object[]> getStudentLeaves(){
		return leaveRepo.getLeaveCountsByUser();
	}
	
	public int getStudentLeaveCountsById(int id) {
		return leaveRepo.countByUserId(id);
	}
	
	public List<StudentLeaveDTO> getStudentLeaveDetails(int studentId){

	    List<Leave> leaves =
	            leaveRepo.findByUserIdOrderByAppliedAtDesc(studentId);

	    return leaves.stream().map(leave -> {

	        StudentLeaveDTO dto =
	                new StudentLeaveDTO();

	        dto.setLeaveId(leave.getId());

	        dto.setStudentId(
	                leave.getUser().getId()
	        );

	        dto.setStudentName(
	                leave.getUser().getName()
	        );

	        dto.setDepartment(
	                leave.getUser().getDepartment()
	        );

	        dto.setFromDate(
	                leave.getFromDate()
	        );

	        dto.setToDate(
	                leave.getToDate()
	        );

	        dto.setReason(
	                leave.getReason()
	        );

	        dto.setStatus(
	                leave.getStatus().name()
	        );

	        dto.setAppliedAt(
	                leave.getAppliedAt()
	        );
	        
	        dto.setSection(leave.getUser().getSection());
	        
	        dto.setSemester(leave.getUser().getSemester());
	        
	        dto.setYear(leave.getUser().getYear());
	        
	        dto.setLeaveType(leave.getLeaveType());

	        dto.setApprovedBy(
	                leave.getApprovedBy() == null
	                ? "-"
	                : leave.getApprovedBy().getName()
	        );
	        dto.setProfileImage(leave.getUser().getProfileImage());

	        return dto;

	    }).toList();
	}
	List<Leave> getLeavesByFacultyId(int facultyId){
		return leaveRepo.findByUserFacultyIdId(facultyId);
	}
	
	public List<Leave> searchFacultyLeaves(
	        Integer facultyId,
	        Status status,
	        LocalDate start,
	        LocalDate end,
	        String keyword) {

	    return leaveRepo.searchFacultyLeaves(
	            facultyId,
	            status,
	            start,
	            end,
	            keyword
	    );
	}
	
	public List<Leave> getTop5FacultyLeavesByStatus(
	        int facultyId,
	        Status status) {

	    return leaveRepo.getTopFacultyLeavesByStatus(
	            facultyId,
	            status,
	            PageRequest.of(0, 5)
	    );
	}
	public List<Leave> getTop5LeavesByFacultyId(int facId) {
	    return leaveRepo.findTop5ByUserFacultyIdIdOrderByAppliedAtDesc(
	            facId
	    );
	}

}
