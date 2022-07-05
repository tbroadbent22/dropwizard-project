/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld;

import com.example.dropwizard.helloworld.keycloak.KeycloakConfig;
import com.example.dropwizard.helloworld.keycloak.KeycloakJettyAuthenticatorExt;
import com.example.dropwizard.helloworld.keycloak.KeycloakResolver;
import com.example.dropwizard.project.helloworld.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.security.Constraint;
import org.pac4j.dropwizard.Pac4jBundle;
import org.pac4j.dropwizard.Pac4jFactory;

public class HelloWorldApplication extends Application<HelloWorldConfiguration>
{
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }
    
 final Pac4jBundle<HelloWorldConfiguration> bundle = new Pac4jBundle<HelloWorldConfiguration>() {
        @Override
        public Pac4jFactory getPac4jFactory(HelloWorldConfiguration configuration) {
            return configuration.getPac4jFactory();
        }
    };
    
    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addBundle(bundle);
    }

    @Override
    public void run(HelloWorldConfiguration configuration,
                    Environment environment)
    {
        final HelloWorldResource resource = new HelloWorldResource(
            configuration.getTemplate(),
            configuration.getDefaultName()
        );
        
        System.out.println("ENV: " + environment);
        environment.jersey().register(resource);
        initCors(environment);
        initAuth(environment);
    }
    
    private void initCors(final Environment environment) {
         final FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
            filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
            filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,PUT,PATCH,DELETE,OPTIONS");
            filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,If-Modified-Since,Cache-Control,Tenant,Csrf-Token,X-TENANT-ID,CA-AGENT-SERVICE,CAF-Correlation-Id");
            filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
            filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
            filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    }
    
    private void initAuth(final Environment environment) {
         final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        environment.getApplicationContext().setSecurityHandler(securityHandler);
        final Constraint constraint = new Constraint();
        constraint.setAuthenticate(true);

        final Set<String> authRoles = new HashSet<String>();
        authRoles.add("USER");
        securityHandler.setRoles(authRoles);
        constraint.setRoles(authRoles.toArray(new String[0]));

        final ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec("/*");
        constraintMapping.setConstraint(constraint);
        securityHandler.addConstraintMapping(constraintMapping);

        KeycloakConfig config = getConfig();
        final KeycloakJettyAuthenticatorExt keycloak = new KeycloakJettyAuthenticatorExt();
        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
        final KeycloakResolver keycloakResolver = new KeycloakResolver(config);
        keycloak.setConfigResolver(keycloakResolver);

        final SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.getSessionCookieConfig().setSecure(false);
        
        environment.servlets().setSessionHandler(sessionHandler);
    }

    private KeycloakConfig getConfig()
    {
        KeycloakConfig config = new KeycloakConfig();
        config.setAuthServerUrl("http://localhost:8081");
        config.setSecret("34f37982-733c-49ed-a840-11166f044ef8");
        config.setPassword("password");
        config.setRealm("dev");
        config.setUser("tony");
        config.setResource("dropwizard-project");
        
        return config;
    }
}
