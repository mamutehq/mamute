package org.mamute.providers;

import org.hibernate.SessionFactory;
import org.hibernate.tool.schema.TargetType;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import java.util.EnumSet;

// TODO: Remove this entire class once conversion to Spring Boot is done.

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class SessionFactoryCreator {

	private final MamuteDatabaseConfiguration cfg;
	private SessionFactory factory;

	/**
	 * @deprecated CDI eyes only
	 */
	public SessionFactoryCreator() {
		this(null);
	}
	
	@Inject
	public SessionFactoryCreator(MamuteDatabaseConfiguration cfg) {
		this.cfg = cfg;
	}
	
	@PostConstruct
	public void init() {
		this.factory = cfg.buildSessionFactory();

	}

	@Produces
	@javax.enterprise.context.ApplicationScoped
	public SessionFactory getInstance() {
		return factory;
	}

	void destroy(@Disposes SessionFactory factory) {
		if (!factory.isClosed()) {
			factory.close();
		}
		factory = null;
	}

	public void dropAndCreate() {
		destroy(this.factory);
		cfg.getSchema().drop(EnumSet.of(TargetType.DATABASE), null);
		cfg.getSchema().create(EnumSet.of(TargetType.DATABASE), null);
		init();
	}

	public void drop() {
		factory.close();
		factory = null;
		cfg.getSchema().drop(EnumSet.of(TargetType.DATABASE), null);
		init();
	}


}
