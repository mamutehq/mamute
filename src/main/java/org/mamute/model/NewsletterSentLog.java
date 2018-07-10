package org.mamute.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

//@Entity

public class NewsletterSentLog {
	@Id
	@GeneratedValue
	private Long id;
	
	private final LocalDateTime createdAt = LocalDateTime.now();
}
