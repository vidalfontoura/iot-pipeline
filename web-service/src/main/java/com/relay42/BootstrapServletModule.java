package com.relay42;

import com.google.inject.servlet.ServletModule;
import com.relay42.config.PersistenceModule;
import com.relay42.shiro.modules.BootstrapShiroModule;
import com.relay42.shiro.modules.ShiroAnnotationsModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.guice.web.GuiceShiroFilter;

/**
 * Bootstrap the modules and servlets required by the application
 *
 */
public class BootstrapServletModule extends ServletModule{

    private static final String propertyPackages= GenericBootstrapConstants.JERSEY_PROPERTY_PACKAGES;
    
    @Override
    protected void configureServlets() {
        super.configureServlets();
        
        // Bind the properties file
        install(new BootstrapModule());
        
        install(new BootstrapShiroModule(getServletContext()));
        // This installs the Shiro AOP Annotations
        install(new ShiroAnnotationsModule());
        
        // This installs Cassandra Persistence Module
        install(new PersistenceModule());

        Map<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES, propertyPackages);

        filter("/*").through(GuiceShiroFilter.class);
        serve("/services/*").with(GuiceContainer.class, params);
        
    }
}