package model;

import java.time.LocalDateTime;

public class Member {
	private int id;
	private String memberNo;
	private String name;
	private String email;
	private String passwordHash;
	private LocalDateTime createdAt;
	
	//空建構子(給JDBC用)
	public Member() {}

	
	//建構子(快速建立物件用)
	public Member(String memberNo, String name, String email, String passwordHash) {
		
		this.memberNo = memberNo;
		this.name = name;
		this.email = email;
		this.passwordHash = passwordHash;
		this.createdAt=LocalDateTime.now();
	}
	
	public Member(String name, String email, String passwordHash) {
	    this.name = name;
	    this.email = email;
	    this.passwordHash = passwordHash;
	    this.createdAt = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getMemberNo() {
		return memberNo;
	}


	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPasswordHash() {
		return passwordHash;
	}


	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public String toString() {
		return "Member{" +
	           "id=" + id +
	           ", memberNo='" + memberNo +'\'' +
	           ", name='" + name +'\'' +
	           ", email='" + email + '\'' +
	           ", createdAt=" + createdAt +
	           '}';
	}
	
	
	
	
}