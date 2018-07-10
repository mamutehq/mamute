package org.mamute.validators;

import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.Validator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mamute.controllers.BrutalValidator;
import org.mamute.dao.TestCase;
import org.mamute.dao.UserDAO;
import org.mamute.dto.UserPersonalInfo;
import org.mamute.factory.MessageFactory;
import org.mamute.model.User;
import org.mamute.providers.ClockProvider;
import org.mamute.util.MockClockProvider;

import javax.validation.Validation;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ResourceBundle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.SanitizedText.fromTrustedText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserPersonalInfoValidatorTest extends TestCase{

	private String validEmail;
	private Validator validator;
	private EmailValidator emailValidator;
	private MessageFactory messageFactory;
	private UserDAO users;
	private UserPersonalInfoValidator infoValidator;
	private BundleFormatter bundle;
	private MockClockProvider clockProvider;
	
	@Before
	public void setup() {
		this.bundle = mock(BundleFormatter.class);
		this.validEmail = "artur.adam@email.com.br";
		this.users = mock(UserDAO.class);
		this.validator = new MockValidator();
		this.messageFactory = new MessageFactory(mock(ResourceBundle.class));
		this.emailValidator = new EmailValidator(validator, users, messageFactory);
		javax.validation.Validator javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();
		this.clockProvider = new MockClockProvider();
		this.clockProvider.set(Clock.systemUTC());
		BrutalValidator brutalValidator = new BrutalValidator(javaxValidator, validator, messageFactory);
		this.infoValidator = new UserPersonalInfoValidator(clockProvider, validator, emailValidator, messageFactory, bundle, brutalValidator);
	}

	@Test
	public void should_pass_validation_with_not_required_elements_null() {
		User artur = user("artur com seis caracteres", validEmail);
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName(fromTrustedText(artur.getName()))
				.withEmail(artur.getEmail());
		
		assertTrue(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_under_twelve_years_old_user() {
		User artur = user("artur com seis caracteres", validEmail);
		LocalDateTime hoje = LocalDateTime.now();
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName(fromTrustedText(artur.getName()))
				.withEmail(artur.getEmail())
				.withBirthDate(hoje);
		
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_user_trying_to_update_name_before_allowed_time() {
		User artur = user("artur com seis caracteres", validEmail);
		
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName(fromTrustedText("newName"))
				.withEmail(artur.getEmail());
		
		when(bundle.getMessage("date.joda.simple.pattern")).thenReturn("dd/MM/YYYY");
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_validate_user_trying_to_update_name_after_allowed_time() {
		clockProvider.set(Clock.systemUTC());
		User artur = user(clockProvider, "artur com seis caracteres", validEmail);

		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withName(fromTrustedText("newName"))
				.withEmail(artur.getEmail());
		
		when(bundle.getMessage("date.joda.simple.pattern")).thenReturn("dd/MM/YYYY");

		clockProvider.set(Clock.fixed(LocalDateTime.now().plusDays(31).toInstant(ZoneOffset.UTC), ZoneId.systemDefault()));
		assertTrue(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_null_user(){
		UserPersonalInfo info = new UserPersonalInfo(null);
		assertFalse(infoValidator.validate(info));
	}
	
	@Test
	public void should_not_validate_invalid_mail() {
		User artur = user("artur com seis caracteres", validEmail);
		
		UserPersonalInfo info = new UserPersonalInfo(artur)
				.withEmail("invalidEmail");
		
		assertFalse(infoValidator.validate(info));
	}

	@After
	public void tearDown() {
		DateTimeUtils.setCurrentMillisSystem();
	}
}
