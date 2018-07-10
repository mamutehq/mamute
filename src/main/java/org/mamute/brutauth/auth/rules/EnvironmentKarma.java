package org.mamute.brutauth.auth.rules;

import org.mamute.auth.rules.PermissionRules;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.inject.Named;

@Named("environmentKarma")
public class EnvironmentKarma {

	private Environment environment;

	@Deprecated
	EnvironmentKarma() {
	}

	@Inject
	public EnvironmentKarma(Environment environment) {
		this.environment = environment;
	}

	public long get(PermissionRules rule) {
		String accessLevelString = environment.getProperty("permission.rule." + rule.getPermissionName());
		long karma = Long.parseLong(accessLevelString);
		return karma;
	}
}
