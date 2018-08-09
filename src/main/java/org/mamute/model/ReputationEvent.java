package org.mamute.model;


import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@SQLDelete(sql = "update ReputationEvent set deleted = true where id = ?")
@Where(clause = "deleted = 0")
@Entity
public class ReputationEvent {

	public static final ReputationEvent IGNORED_EVENT = new ReputationEvent(EventType.IGNORED, null, null);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private EventType type;
	
	private int karmaReward;

	@Any(metaColumn= @Column(name = "context_type"))
	@AnyMetaDef(idType = "long", metaType="string", metaValues = {
			@MetaValue(value = "QUESTION", targetEntity = Question.class),
			@MetaValue(value = "NEWS", targetEntity = News.class) 
	})
	@JoinColumn(name = "context_id")
	private ReputationEventContext context;
	
	@ManyToOne
	private User user;
	
	private LocalDateTime date = LocalDateTime.now();

	private boolean deleted;

	@Deprecated
	ReputationEvent() {
	}

	public ReputationEvent(EventType type, ReputationEventContext context, User user) {
		this.type = type;
		this.karmaReward = type.reward();
		this.context = context;
		this.user = user;
	}
	
	public Integer getKarmaReward() {
		return karmaReward;
	}
	
	public Long getId() {
		return id;
	}
	
	public EventType getType() {
		return type;
	}
	
	public ReputationEventContext getContext() {
		return context;
	}
	
	public User getUser() {
		return user;
	}
	
}
