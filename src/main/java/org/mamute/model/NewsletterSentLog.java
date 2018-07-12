package org.mamute.model;

import org.mamute.providers.ClockProvider;
import org.mamute.providers.SystemUtcClockProvider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Entity
@Table(name = "NewsletterSentLog")
public class NewsletterSentLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Transient
	private final ClockProvider clockProvider;

	public NewsletterSentLog() {
		this(new SystemUtcClockProvider());
	}

	public NewsletterSentLog(ClockProvider clockProvider) {
		this.clockProvider = clockProvider;
		this.createdAt = LocalDateTime.now(clockProvider.get());
	}

	private final LocalDateTime createdAt;
}
