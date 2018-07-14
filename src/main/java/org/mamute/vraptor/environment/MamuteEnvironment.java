package org.mamute.vraptor.environment;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.stereotype.Service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

@ApplicationScoped
@Service
public class MamuteEnvironment {

	private final Properties properties = new Properties();
	
	@Inject
	public MamuteEnvironment() throws IOException {
		InputStream stream = MamuteEnvironment.class.getResourceAsStream("/mamute.properties");
		properties.load(stream);
	}
	
	public boolean has(String key) {
		return properties.containsKey(key);
	}
	
	public String get(String key) {
		if (has(key)) {
			return resolveEnv(properties.get(key).toString());
		}
			
		throw new NoSuchElementException("Key " + key + " not found in environment");
			
	}

	private String resolveEnv(String value) {
		if (value.matches("\\$\\{.+\\}")) {
			String envVar = System.getenv(value);
			if (envVar == null) {
				throw new NoSuchElementException("Environment variable " + value + " not defined!");
			}
		}
		return value;
	}
	
}
