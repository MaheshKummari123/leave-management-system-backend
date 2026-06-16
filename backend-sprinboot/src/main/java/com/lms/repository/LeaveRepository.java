package com.lms.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import com.lms.entity.Leave;
import com.lms.entity.Status;

public interface LeaveRepository extends JpaRepository<Leave, Integer> {
	List<Leave> findByUserId(int id);
	List<Leave> findTop3ByUserIdOrderByAppliedAtDesc(int id);
	List<Leave> findTop5ByOrderByAppliedAtDesc();
//	int countByStatus();
	int countByUserIdAndStatus(int userId, Status status);
	List<Leave> findByUserDepartment(String dept);
	
	int countByUserDepartmentAndStatus(String dept,Status status);
	int countByApprovedByIdAndStatus(int facultyTd,Status status);
	
	List<Leave> findByUserFacultyIdId(int facultyId);
	int countByUserFacultyIdIdAndStatus(int facultyId, Status status);
	List<Leave> findByUserFacultyIdIdAndStatus(int facultyId, Status status);
	
	
	
	int countByStatus(Status status);
	void deleteByUserId(int id);
	
	
	@Query("""
		    SELECT l FROM Leave l
		    WHERE (:userId IS NULL OR l.user.id = :userId)
		    AND (:status IS NULL OR l.status = :status)
		    AND (:start IS NULL OR l.toDate >= :start)
		    AND (:end IS NULL OR l.fromDate <= :end)
		    AND (:keyword IS NULL OR 
		         LOWER(l.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		         LOWER(l.user.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		         LOWER(l.reason) LIKE LOWER(CONCAT('%', :keyword, '%'))
		         )
		""")
		List<Leave> searchLeaves(
		    @Param("userId") Integer userId,
		    @Param("status") Status status,
		    @Param("start") LocalDate start,
		    @Param("end") LocalDate end,
		    @Param("keyword") String keyword
		);
	
	@Query("""
	        SELECT l
	        FROM Leave l
	        WHERE l.appliedAt BETWEEN :start AND :end
	    """)
	    List<Leave> findLeavesBetween(
	            @Param("start") LocalDateTime start,
	            @Param("end") LocalDateTime end);
	
	@Query("""
	           SELECT MONTH(l.appliedAt), COUNT(l)
	           FROM Leave l
	           GROUP BY MONTH(l.appliedAt)
	           ORDER BY MONTH(l.appliedAt)
	           """)
	    List<Object[]> getMonthWiseLeaves();
	   
	    @Query(value = """
	    	    SELECT u.department, COUNT(l.id)
	    	    FROM users u
	    	    LEFT JOIN leaves l
	    	        ON u.id = l.user_id
	    	    where u.department != 'ADMIN'
	    	    GROUP BY u.department
	    	    ORDER BY COUNT(l.id) DESC
	    	""", nativeQuery = true)
	    	List<Object[]> getDepartmentWiseLeaveCount();
	 
	    	@Query("""
	    		    SELECT l.user.id, COUNT(l.id)
	    		    FROM Leave l
	    		    GROUP BY l.user.id
	    		""")
	    		List<Object[]> getLeaveCountsByUser();
	  
	  int countByUserId(int userId);
	  List<Leave> findByUserIdOrderByAppliedAtDesc(int userId);
	  
	  @Query("""
			    SELECT l FROM Leave l
			    WHERE l.user.facultyId.id = :facultyId
			    AND (:status IS NULL OR l.status = :status)
			    AND (:start IS NULL OR l.toDate >= :start)
			    AND (:end IS NULL OR l.fromDate <= :end)
			    AND (:keyword IS NULL OR
			         LOWER(l.user.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			         LOWER(l.user.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			         LOWER(l.reason) LIKE LOWER(CONCAT('%', :keyword, '%')))
			""")
			List<Leave> searchFacultyLeaves(
			        @Param("facultyId") Integer facultyId,
			        @Param("status") Status status,
			        @Param("start") LocalDate start,
			        @Param("end") LocalDate end,
			        @Param("keyword") String keyword
			);
	  
	  @Query("""
			    SELECT l
			    FROM Leave l
			    WHERE l.user.facultyId.id = :facultyId
			    AND l.status = :status
			    ORDER BY l.appliedAt DESC
			""")
			List<Leave> getTopFacultyLeavesByStatus(
			        @Param("facultyId") int facultyId,
			        @Param("status") Status status,
			        Pageable pageable
			);
	  List<Leave> findTop5ByUserFacultyIdIdOrderByAppliedAtDesc(int facultyId);
}
