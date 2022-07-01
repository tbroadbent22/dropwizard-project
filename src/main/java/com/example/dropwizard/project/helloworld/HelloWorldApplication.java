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

public class HelloWorldApplication extends Application<HelloWorldConfiguration>
{
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }
    
    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
    }

    //TODO setup keycloak config within run
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

        final ConstraintMapping constraintMapping = new ConstraintMapping();
        constraintMapping.setPathSpec("/*");
        constraintMapping.setConstraint(constraint);
        securityHandler.addConstraintMapping(constraintMapping);
        
        final ConstraintMapping constraintMappingExclusion = new ConstraintMapping();
        constraintMappingExclusion.setPathSpec("/hello-world");
        constraintMappingExclusion.setMethodOmissions(new String[]{"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"});
        constraintMappingExclusion.setConstraint(constraint);
        securityHandler.addConstraintMapping(constraintMappingExclusion);

//        final ConstraintMapping constraintMapping = new ConstraintMapping();
//        final Constraint constraint = new Constraint(Constraint.__BASIC_AUTH);

        KeycloakConfig config = getConfig();
        final KeycloakJettyAuthenticatorExt keycloak = new KeycloakJettyAuthenticatorExt();
        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
        final KeycloakResolver keycloakResolver = new KeycloakResolver(config);
        keycloak.setConfigResolver(keycloakResolver);

        final SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.getSessionCookieConfig().setSecure(false);
        
        environment.servlets().setSessionHandler(sessionHandler);
    }

//    private void initAuth(final Environment environment)
//    {
//        final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
//        environment.getApplicationContext().setSecurityHandler(securityHandler);
//        final Constraint constraint = new Constraint();
//        constraint.setAuthenticate(true);
//        
//        System.out.println("HELLO WORLD ");
//
//        KeycloakConfig config = getConfig();
//        
//        final KeycloakJettyAuthenticatorExt keycloak = new KeycloakJettyAuthenticatorExt();
////        System.out.println("ENV: " + environment.getApplicationContext());
////        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
//        final KeycloakResolver keycloakResolver = new KeycloakResolver(config);
//        keycloak.setConfigResolver(keycloakResolver);
//        keycloak.initializeKeycloak();
//        System.out.println("AUTH METHOD: ");
//    }

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