package org.mamute.model;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Embeddable
public class Moderation {

	/**
	 * @deprecated hibernate only
	 */
	protected Moderation() {
		
	}
	public Moderation(User moderator) {
		this.moderatedBy = moderator;
	}

	private LocalDateTime moderatedAt = LocalDateTime.now();

	@ManyToOne(optional = true)
	private User moderatedBy;
	
	public LocalDateTime getModeratedAt() {
		return moderatedAt;
	}

}
