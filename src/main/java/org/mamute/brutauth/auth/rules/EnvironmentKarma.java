package org.mamute.brutauth.auth.rules;

import org.mamute.auth.rules.PermissionRules;
import org.mamute.vraptor.environment.MamuteEnvironment;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.inject.Named;

@Named("environmentKarma")
public class EnvironmentKarma {

	private MamuteEnvironment environment;

	@Deprecated
	EnvironmentKarma() {
	}

	@Inject
	public EnvironmentKarma(MamuteEnvironment environment) {
		this.environment = environment;
	}

	public long get(PermissionRules rule) {
		String accessLevelString = environment.get("permission.rule." + rule.getPermissionName());
		long karma = Long.parseLong(accessLevelString);
		return karma;
	}
}
