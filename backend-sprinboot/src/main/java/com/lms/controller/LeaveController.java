package com.lms.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.dto.StudentLeaveDTO;
import com.lms.entity.Leave;
import com.lms.entity.Status;
import com.lms.service.LeaveService;

@RestController
public class LeaveController {

	@Autowired
	private LeaveService service;
	
	@GetMapping("/list")
	public List<Leave> listLeaves(){
		return service.getAllLeaves();
	}
	
	//Get All leaves of students by student id
	@GetMapping("/list/{id}")
	public List<Leave> listLeavesByUserId(@PathVariable int id){
		return service.getAllLeavesById(id);
	}
	
	//Get Top 3 leaves of student by Student Id
	@GetMapping("/recent-lists/{id}")
	public List<Leave> lastLeavesByuserId(@PathVariable int id){
		return service.getLastThreeLeavesByStudentId(id);
	}
	
	
	@PostMapping("/leave")
	public Leave newLeave(@RequestBody Leave newLeave) {
		return service.saveLeave(newLeave);
	}
	
	//Counting leaves of students based on Status
	@GetMapping("/student/leave-counts/{id}")
	public Map<String, Integer> studentLeaveCounts(@PathVariable int id){
		return service.getUserLeaveCounts(id);
	}
	
	//Updating Status
	@PutMapping("/faculty/leave-status-change/{id}")
	public Leave changeStatus(@PathVariable int id,@RequestBody Map<String, String> body) {
		Status status=Status.valueOf(body.get("status"));
		int facultyId=Integer.parseInt(body.get("facultyId"));
		
		return service.updateStatus(id,facultyId, status);		
	}
	
	//Get Leaves by dept 
	@GetMapping("/faculty/leave-requests/{id}")
	public List<Leave> getLeaveRequestsByDept(@PathVariable int id){
		return service.getFacultyLeavesByDept(id);
	}
	
	@GetMapping("/faculty/leave-counts/{id}")
	public Map<String, Integer> facultyLeaveRequestCounts(@PathVariable int id){
		return service.getFacultyDashboardCounts(id);
	}
	
	@GetMapping("/leave/search")
	public List<Leave> searchLeaves(
	        @RequestParam(required = false) Integer userId,
	        @RequestParam(required = false) Status status,
	        @RequestParam(required = false) 
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
	        @RequestParam(required = false) 
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
	        @RequestParam(required = false) String keyword
	) {
	    return service.searchLeaves(userId, status, start, end, keyword);
	}
	
	@GetMapping("/admin/leave-counts")
	public Map<String, Integer> studentLeaveCounts(){
		return service.getAllLeaveStatusCounts();
	}
	
	@DeleteMapping("/leave/delete/{id}")
	public void deleteLeave(@PathVariable int id) {
		service.deleteLeave(id);
	}
	
	@GetMapping("/recent-leaves")
	public List<Leave> top5Leaves(){
		return service.top5Leaves();
	}
	
	
	@GetMapping("/search-leaves")
	public ResponseEntity<List<Leave>> searchLeaves(
	        @RequestParam String startDate,
	        @RequestParam(required = false) String endDate) {

	    LocalDate start = LocalDate.parse(startDate);

	    if (endDate == null || endDate.isBlank()) {

	        return ResponseEntity.ok(
	                service.getLeavesByAppliedDate(start)
	        );
	    }

	    return ResponseEntity.ok(
	            service.getLeavesByAppliedDateRange(
	                    start,
	                    LocalDate.parse(endDate)
	            )
	    );
	}
	
	@GetMapping("/month-wise-leaves")
    public List<Map<String, Object>> getMonthWiseLeaves() {

        List<Object[]> results = service.getMonthWiseLeaves();

        String[] months = {
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };

        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("month", months[(Integer) row[0]]);
            map.put("leaves", row[1]);
            response.add(map);
        }

        return response;
    }
	
	@GetMapping("/department-leave-counts")
	public List<Object[]> getDepartmentWiseLeaves(){
		return service.getDepartmentLeaveCounts();
	}
	
	@GetMapping("/students-leave-counts")
	public List<Object[]> getStudentLeaves(){
		return service.getStudentLeaves();
	}
	@GetMapping("/student-leave-counts/{studentId}")
	public int getStudentLeavesById(@PathVariable int studentId) {
		return service.getStudentLeaveCountsById(studentId);
	}
	
	@GetMapping("/faculty/student-details/{studentId}")
	public List<StudentLeaveDTO> getStudentDetails(
	        @PathVariable int studentId){

	    return service.getStudentLeaveDetails(studentId);
	}
	
	@GetMapping("/faculty/search-leaves")
	public List<Leave> searchFacultyLeaves(
	        @RequestParam Integer facultyId,
	        @RequestParam(required = false) Status status,
	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate start,
	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate end,
	        @RequestParam(required = false) String keyword) {

	    return service.searchFacultyLeaves(
	            facultyId,
	            status,
	            start,
	            end,
	            keyword
	    );
	}
	
	@GetMapping("/faculty/top5-leaves-by-status")
	public List<Leave> getTop5LeavesByFacultyId(@RequestParam int facultyId,@RequestParam Status status){
		return service.getTop5FacultyLeavesByStatus(facultyId, status);
	}
	@GetMapping("/faculty/top5-leaves/{facultyId}")
	public List<Leave> getTop5LeavesByFacultyIdOnly(@PathVariable int facultyId){
		return service.getTop5LeavesByFacultyId(facultyId);
	}
	
	@GetMapping("/faculty/student-leaves/{facultyId}")
	public List<Leave> getStudentLeavesByFacultyId(@PathVariable int facultyId){
		return service.getTop5LeavesByFacultyId(facultyId);
	}
	
	
	
}
