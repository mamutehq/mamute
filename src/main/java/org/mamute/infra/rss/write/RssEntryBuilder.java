package org.mamute.infra.rss.write;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RssEntryBuilder {

	private String name;
	private String title;
	private String link;
	private String guid;
	private ZonedDateTime date;
	private RssImageEntry image;
	private static final String PATTERN = "EEE, dd MMM yyy HH:mm:ss Z";
	static final DateTimeFormatter RSS_DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

	public RssEntryBuilder withAuthor(String name) {
		this.name = name;
		return this;
	}

	public RssEntryBuilder withTitle(String title) {
		this.title = title;
		return this;
	}

	public RssEntryBuilder withLink(String link) {
		this.link = link;
		return this;
	}

	public RssEntryBuilder withId(String guid) {
		this.guid = guid;
		return this;
	}
	
	public RssEntryBuilder withDate(LocalDateTime date) {
		this.date = date.atZone(ZoneId.systemDefault());
		return this;
	}
	
	public RssEntryBuilder withImage(RssImageEntry image){
		this.image = image;
		return this;
	}

	public RssEntry build() {
		return new RssEntry(title, link, guid, RSS_DATE_FORMATTER.format(date), name, image);
	}
	
}
