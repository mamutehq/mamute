package org.mamute.model.ban;

import org.mamute.model.User;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Cacheable
//@Entity
public class BlockedIp {

	@Id
	@GeneratedValue
	private Long id;

	private String ip;

	private final LocalDateTime createdAt = LocalDateTime.now();

	@ManyToOne
	private User author;

	/**
	 * @deprecated hibernate only
	 */
	BlockedIp() {
	}

	public BlockedIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public Long getId() {
		return id;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public User getAuthor() {
		return author;
	}
}
