package org.mamute.model.interfaces;

import org.joda.time.DateTime;
import org.mamute.model.User;

import java.time.LocalDateTime;

public interface RssContent {

	User getAuthor();

	String getTitle();

	Long getId();

	LocalDateTime getCreatedAt();

	String getLinkPath();
	
}
