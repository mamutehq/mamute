package org.mamute.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.User;
import org.mamute.model.Vote;
import org.mamute.model.VoteType;
import org.mamute.model.vote.MassiveVote;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;
import org.mamute.providers.ClockProvider;
import org.mamute.providers.SystemUtcClockProvider;
import org.mamute.util.MockClockProvider;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class MassiveVoteTest extends TestCase {
	
	private MassiveVote massiveVote;
	private User author;
	private User massiveVoter;

	@Before
	public void setUp() throws Exception {
		massiveVote = new MassiveVote();
		author = user("Felipe", "gato-sem-gata@lalala.bla");
		massiveVoter = user("MassiveWolter", "massive-wolter@bla.bla");
	}

	@Test
	public void should_not_increase_karma_for_massive_votes() {
		ClockProvider clockProvider = new SystemUtcClockProvider();
		for (int i = 0; i < massiveVote.getMaxVotesAllowed(); i++) {
			Vote currentVote = vote(clockProvider, massiveVoter, VoteType.DOWN, 1l);
			assertTrue(massiveVote.shouldCountKarma(massiveVoter, author, currentVote));
		}
		Vote deniedVote = vote(clockProvider, massiveVoter, VoteType.DOWN, 1l);
		assertFalse(massiveVote.shouldCountKarma(massiveVoter, author, deniedVote));
	}
	
	@Test
	public void should_count_karma_if_vote_was_created_before_min_date() throws Exception {
		MockClockProvider clockProvider = new MockClockProvider();
		clockProvider.set(Clock.fixed(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
		for (int i = 0; i < massiveVote.getMaxVotesAllowed(); i++) {
			massiveVote.shouldCountKarma(massiveVoter, author, vote(clockProvider, massiveVoter, VoteType.DOWN, 1l));
		}

		clockProvider.set(Clock.fixed(LocalDateTime.now().toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
		Vote acceptedVote = vote(clockProvider, massiveVoter, VoteType.DOWN, 1l);
		assertTrue(massiveVote.shouldCountKarma(massiveVoter, author, acceptedVote));

	}
}
