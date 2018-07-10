package org.mamute.model;

import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

//@Entity
public class UserSession {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Index(name="session_key")
	@Column(unique=true)
	private String sessionKey;
	
	@ManyToOne
	private User user;
	
	private final LocalDateTime createdAt = LocalDateTime.now();
	
	/**
	 * @deprecated
	 */
	UserSession() {
	}

	public UserSession(User user, String sessionKey) {
		this.user = user;
		this.sessionKey = sessionKey;
	}

	public String getSessionKey() {
		return sessionKey;
	}
	
	public User getUser() {
		return user;
	}
}
