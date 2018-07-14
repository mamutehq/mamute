package org.mamute.model.interfaces;

import org.joda.time.DateTime;
import org.mamute.model.User;

import java.time.LocalDateTime;

public interface Touchable {
	LocalDateTime getLastUpdatedAt();
	User getLastTouchedBy();
	LocalDateTime getCreatedAt();
	User getAuthor();
	boolean isEdited();
}
