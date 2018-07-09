package org.mamute.testcase;

import br.com.caelum.vraptor.ioc.cdi.CDIBasedContainer;
import br.com.caelum.vraptor.test.container.CdiContainer;
import org.mamute.dao.TestCase;

import javax.enterprise.inject.spi.CDI;

import static br.com.caelum.vraptor.environment.ServletBasedEnvironment.ENVIRONMENT_PROPERTY;

public abstract class CDITestCase extends TestCase{

	protected static CDIBasedContainer cdiBasedContainer;

	static{
		System.setProperty(ENVIRONMENT_PROPERTY, "test");
		CdiContainer cdiContainer = new CdiContainer();
		cdiContainer.start();
		cdiBasedContainer = CDI.current().select(CDIBasedContainer.class).get();
	}
	
}
