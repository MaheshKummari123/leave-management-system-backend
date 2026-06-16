package com.lms.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.lms.entity.Role;
import com.lms.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
	List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
	
	List<User> findByDepartmentAndRole(String department, Role role);
	
	@Query("""
		    SELECT u.department, COUNT(u),
		           SUM(CASE WHEN u.role = 'STUDENT' THEN 1 ELSE 0 END),
		           SUM(CASE WHEN u.role = 'FACULTY' THEN 1 ELSE 0 END)
		    FROM users u
		    GROUP BY u.department
		""")
		List<Object[]> getDepartmentStats();
	
		@Query(value = """
				SELECT
				    s.id,
				    s.name,
				    s.department,
				    f.id AS facultyId,
				    f.name AS facultyName
				FROM users s
				LEFT JOIN users f ON s.faculty_id = f.id
				WHERE s.role = 'STUDENT'
				""", nativeQuery = true)
				List<Map<String, Object>> getStudentsWithFaculty();
		List<User> findByRole(Role role);
		
		@Query(value = """
			    SELECT
			        f.id,
			        f.name,
			        f.department,

			        COUNT(DISTINCT s.id) AS total_students,

			        COUNT(DISTINCT l.id) AS leaves_managed,

			        ROUND(
			            COUNT(DISTINCT CASE
			                WHEN l.status = 'APPROVED' THEN l.id
			            END)
			            * 100.0 /
			            CASE
			                WHEN COUNT(DISTINCT l.id) = 0 THEN 1
			                ELSE COUNT(DISTINCT l.id)
			            END,
			            2
			        ) AS approval_rate

			    FROM users f

			    LEFT JOIN users s
			        ON s.faculty_id = f.id
			        AND s.role = 'STUDENT'

			    LEFT JOIN leaves l
			        ON l.approved_by = f.id

			    WHERE f.role = 'FACULTY'

			    GROUP BY f.id, f.name, f.department

			    ORDER BY f.name
			    """, nativeQuery = true)
			List<Object[]> getFacultyPerformanceReport();
			
			List<User> findByFacultyIdIdAndRole(int facultyId,Role role);
			
			
			@Modifying
			@Transactional
			@Query("""
				       UPDATE users u
				       SET u.department = :newDept
				       WHERE u.department = :oldDept
				       """)
				void updateDepartmentName(
				        @Param("oldDept") String oldDept,
				        @Param("newDept") String newDept
				);
}
