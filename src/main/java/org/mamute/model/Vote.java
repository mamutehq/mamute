package org.mamute.model;

import org.mamute.providers.ClockProvider;
import org.mamute.providers.SystemUtcClockProvider;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private final VoteType type;
    
    @ManyToOne
    private final User author;
    
    private final LocalDateTime createdAt;
    
    private LocalDateTime lastUpdatedAt;
    
    public LocalDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	/**
     * @deprecated hibernate eyes
     */
    Vote() {
    	this(new SystemUtcClockProvider(), null, null);
    }

    public Vote(User author, VoteType type) {
        this.author = author;
        this.type = type;
        ClockProvider clockProvider = new SystemUtcClockProvider();
        this.createdAt = LocalDateTime.now(clockProvider.get());
        this.lastUpdatedAt = LocalDateTime.now(clockProvider.get());
    }

    public Vote(ClockProvider clockProvider, User author, VoteType type) {
        this.author = author;
        this.type = type;
        this.createdAt = LocalDateTime.now(clockProvider.get());
        this.lastUpdatedAt = LocalDateTime.now(clockProvider.get());
    }

	public int getCountValue() {
		return type.getCountValue();
	}

	public long substitute(Vote previous, List<Vote> votes) {
		long delta = 0;
		if (votes.remove(previous))
			delta -= previous.getCountValue();
		votes.add(this);
		delta += getCountValue();
		return delta;
	}
	
	public User getAuthor() {
		return author;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vote other = (Vote) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public VoteType getType() {
        return type;
    }
    
    public boolean isUp() {
    	return type.equals(VoteType.UP);
    }

	public boolean isDown() {
		return type.equals(VoteType.DOWN);
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
    
}
