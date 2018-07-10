package org.mamute.model;

import java.time.ZoneId;
import java.util.Comparator;

public class VoteComparator implements Comparator<Vote> {

	@Override
	public int compare(Vote vote1, Vote vote2) {
		if (vote1.getCreatedAt().equals(vote2.getCreatedAt()))
			return 0;
		return (Long) vote1.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
				- (Long) vote2.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < 0l ? -1 : 1;
	}
}
