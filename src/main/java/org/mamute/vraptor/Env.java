package org.mamute.vraptor;

import org.springframework.core.env.Environment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

@ApplicationScoped
@Named("brutalEnv")
public class Env {

	@Inject private Environment env;
	@Inject private ServletContext context;

	public String host() {
		return env.getProperty("host");
	}

	public String s3Host() {
		return env.getProperty("s3.host");
	}

	public String getHostAndContext() {
		return host() + context.getContextPath();
	}
}