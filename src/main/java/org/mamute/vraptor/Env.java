package org.mamute.vraptor;

import org.mamute.vraptor.environment.MamuteEnvironment;
import org.springframework.core.env.Environment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

@ApplicationScoped
@Named("brutalEnv")
public class Env {

	@Inject private MamuteEnvironment env;
	@Inject private ServletContext context;

	public String host() {
		return env.get("host");
	}

	public String s3Host() {
		return env.get("s3.host");
	}

	public String getHostAndContext() {
		return host() + context.getContextPath();
	}
}