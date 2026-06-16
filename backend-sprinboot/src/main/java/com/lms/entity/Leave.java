package com.lms.entity;

import java.time.LocalDate;
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
import jakarta.persistence.Table;

@Entity
@Table(name="leaves")
public class Leave {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="from_date")
	private LocalDate fromDate;
	
	@Column(name="to_date")
	private LocalDate toDate;
	
	@Column(columnDefinition="TEXT")
	private String reason;
	
	@Enumerated(EnumType.STRING)
	private Status status=Status.PENDING;
	
	@Enumerated(EnumType.STRING)
	private LeaveType leaveType;
	
	@Column(name="applied_at")
	private LocalDateTime appliedAt;
	
	
	@ManyToOne
	@JoinColumn(name="approved_by")
	private User approvedBy;
	
	@PrePersist
	protected void onCreate() {
		this.appliedAt=LocalDateTime.now();
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	public LocalDateTime getAppliedAt() {
	    return appliedAt;
	}

	public void setAppliedAt(LocalDateTime appliedAt) {
	    this.appliedAt = appliedAt;
	}
	
	
	
	
	
	
	
}
