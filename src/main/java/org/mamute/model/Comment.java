package org.mamute.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.mamute.model.interfaces.Flaggable;
import org.mamute.model.interfaces.Notifiable;
import org.mamute.model.interfaces.Votable;
import org.mamute.providers.ClockProvider;
import org.mamute.providers.SystemUtcClockProvider;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SQLDelete(sql = "update Comment set deleted = true where id = ?")
@Where(clause = "deleted = 0")
@Entity
public class Comment implements Notifiable, Votable, Flaggable {
    
	public static final int COMMENT_MIN_LENGTH = 15;
	public static final String ERROR_NOT_EMPTY = "comment.errors.not_empty";
    public static final String ERROR_LENGTH = "comment.errors.length";
	private static final int COMMENT_MAX_LENGTH = 600;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    @NotEmpty(message = ERROR_NOT_EMPTY)
    @Length(min = COMMENT_MIN_LENGTH, max = COMMENT_MAX_LENGTH , message = ERROR_LENGTH)
    private String comment;

    @Lob
    @NotEmpty(message = ERROR_NOT_EMPTY)
    @Length(min = COMMENT_MIN_LENGTH , message = ERROR_LENGTH)
    private String htmlComment;

    @ManyToOne(optional = false)
    private final User author;
    
    private final LocalDateTime createdAt;
    
    private LocalDateTime lastUpdatedAt;
    
	@JoinTable(name = "Comment_Votes")
	@OneToMany
	private final List<Vote> votes = new ArrayList<>();
	
	private long voteCount = 0;
	
	@JoinTable(name = "Comment_Flags")
	@OneToMany
	private final List<Flag> flags = new ArrayList<>();
	
	@Embedded
	private final ModerationOptions moderationOptions = new ModerationOptions();

	private boolean deleted;

	@Transient
	private final ClockProvider clockProvider;

	/**
     * @deprecated hibernate eyes
     */
    Comment() {
    	this(new SystemUtcClockProvider(), null, MarkedText.notMarked(""));
    }

	public Comment(User author, MarkedText comment) {
		this.clockProvider = new SystemUtcClockProvider();
		this.createdAt = LocalDateTime.now(clockProvider.get());
		this.lastUpdatedAt = LocalDateTime.now(clockProvider.get());
		this.author = author;
		setComment(comment);
	}

	public Comment(ClockProvider clockProvider, User author, MarkedText comment) {
    	this.clockProvider = clockProvider;
    	this.createdAt = LocalDateTime.now(clockProvider.get());
    	this.lastUpdatedAt = LocalDateTime.now(clockProvider.get());
		this.author = author;
		setComment(comment);
    }

	public void setComment(MarkedText comment) {
		this.comment = comment.getPure();
        this.htmlComment = comment.getMarked();
	}
    
    public User getAuthor() {
		return author;
	}
    
    public String getHtmlComment() {
		return htmlComment;
	}

	public String getTypeName() {
		return Comment.class.getSimpleName();
	}
	
	public Long getId() {
		return id;
	}
	
	public String getComment() {
		return comment;
	}

    public Class getType() {
        return Comment.class;
    }

    @Override
    public String getTrimmedContent() {
        String comment = getHtmlComment();
        if (comment.length() < 90)
            return comment;
        return comment.substring(0, 90) + "...";
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getTypeNameKey() {
        return "comment.type_name";
    }

	public LocalDateTime getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void add(Flag flag) {
		flags.add(flag);
	}
	
	public boolean alreadyFlaggedBy(User user) {
		for (Flag flag : flags) {
			if (flag.createdBy(user))
				return true;
		}
		return false;
	}

	@Override
	public void substitute(Vote previous, Vote vote) {
		this.voteCount += vote.substitute(previous, votes);
	}
	
	public void remove(Vote previous) {
		votes.remove(previous);
		this.voteCount -= previous.getCountValue();
//		addUserInteraction(vote.getAuthor());
	}

	@Override
	public long getVoteCount() {
		return voteCount;
	}
	
	public List<Flag> getFlags() {
		return flags;
	}

	@Override
	public void remove() {
		moderationOptions.remove();
	}

	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public boolean isVisible() {
		return this.moderationOptions.isVisible();
	}

	@Override
	public boolean isVisibleForModeratorAndNotAuthor(User user) {
		return !this.isVisible() && user != null && user.isModerator();
	}

	@Override
	public String getEmailTemplate() {
		return "comment_notification_mail";
	}
	public List<Vote> getVotes() {
		return Collections.unmodifiableList(votes);
	}

	@Deprecated
	@Override
	public Question getQuestion() {
		throw new UnsupportedOperationException();
	}

	public boolean hasAuthor(User user) {
		return user.getId().equals(author.getId());
	}
}
