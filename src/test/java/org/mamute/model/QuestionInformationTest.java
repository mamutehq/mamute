package org.mamute.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.UpdateStatus.PENDING;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.QuestionInformationBuilder;
import org.mamute.providers.ClockProvider;
import org.mamute.util.MockClockProvider;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class QuestionInformationTest {

	private MockClockProvider clockProvider;
	private QuestionInformationBuilder builder;
	private Question ruby;

	@Before
	public void setup(){
		clockProvider = new MockClockProvider();
		clockProvider.set(Clock.systemUTC());
		builder = new QuestionInformationBuilder()
		.withClockProvider(clockProvider);
		ruby = new QuestionBuilder()
				.withTitle("how do i program using ruby?")
				.withClockProvider(clockProvider)
				.build();
	}
	
	@Test
	public void should_verify_if_is_before_current_information() throws InterruptedException {
		clockProvider.set(Clock.fixed(LocalDateTime.now().minusSeconds(10).toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
		QuestionInformation version = builder.build();
		clockProvider.set(Clock.systemUTC());

		ruby.enqueueChange(version, PENDING);
		QuestionInformation infoByModerator = builder.build();
		ruby.approve(infoByModerator);
		System.out.println("this.createdAt should be before");
		assertTrue(version.isBeforeCurrent());
	}
	
	@Test
	public void should_verify_if_is_before_current_information_without_edits() throws InterruptedException {
		QuestionInformation version = TimeMachine.goTo(new DateTime().minusSeconds(10)).andExecute(new Block<QuestionInformation>() {
			@Override
			public QuestionInformation run() {
				return builder.build();
			}
		});

		ruby.enqueueChange(version, PENDING);
		QuestionInformation infoByModerator = builder.build();
		ruby.approve(infoByModerator);
		System.out.println("this.createdAt should be before");
		assertTrue(version.isBeforeCurrent());
	}

	@Test
	public void should_verify_if_is_not_before_current_information() throws InterruptedException {
		QuestionInformation infoByModerator = builder.build();
		ruby.approve(infoByModerator);
		Thread.sleep(100);
		QuestionInformation version = builder.build();
		ruby.enqueueChange(version, PENDING);

		assertFalse(version.isBeforeCurrent());
	}

}
