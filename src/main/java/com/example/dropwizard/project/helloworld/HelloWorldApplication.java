/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld;

import com.codahale.metrics.health.HealthCheck;
import com.example.dropwizard.project.helloworld.resources.CallbackResource;
import com.example.dropwizard.project.helloworld.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.config.Config;
import org.pac4j.core.http.ajax.DefaultAjaxRequestResolver;
import org.pac4j.dropwizard.Pac4jBundle;
import org.pac4j.dropwizard.Pac4jFactory;
import org.pac4j.jee.filter.SecurityFilter;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;

public class HelloWorldApplication extends Application<HelloWorldConfiguration>
{
    
    public static void main(String[] args) throws Exception
    {
        new HelloWorldApplication().run(args);
    }

    private final Pac4jBundle<HelloWorldConfiguration> pac4j = new Pac4jBundle<HelloWorldConfiguration>()
    {
        @Override
        public Pac4jFactory getPac4jFactory(
            HelloWorldConfiguration configuration)
        {
            return configuration.getPac4jFactory();
        }
    };

    @Override
    public String getName()
    {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap)
    {
        bootstrap.addBundle(pac4j);
    }

    @Override
    public void run(HelloWorldConfiguration conf, Environment env)
        throws Exception
    {

        env.jersey().register(new AbstractBinder()
        {
            @Override
            protected void configure()
            {
                // so that we can inject the config in resources if needed
                bind(pac4j.getConfig()).to(Config.class);
            }
        });

        final HelloWorldResource resource = new HelloWorldResource(
            conf.getTemplate(),
            conf.getDefaultName()
        );

        final CallbackResource cb = new CallbackResource();
        env.jersey().register(resource);
        env.jersey().register(cb);

        env.healthChecks().register(getName(), new HealthCheck()
                                {
                                    @Override
                                    protected HealthCheck.Result check() throws Exception
                                    {
                                        return HealthCheck.Result.healthy();
                                    }
                                });

        final KeycloakOidcClient oidcClient = new KeycloakOidcClient(getPac4jConfig());
        oidcClient.setCallbackUrl("http://localhost:8080/callback");
        oidcClient.setAjaxRequestResolver(new DefaultAjaxRequestResolver());

        pac4j.getConfig().getClients().getClients().add(oidcClient);

        Map<String, Authorizer> authorizers = new HashMap();
        authorizers.put("role", new RequireAnyRoleAuthorizer("USER"));
        pac4j.getConfig().setAuthorizers(authorizers);

        initCors(env);

        SecurityFilter authFilter
            = new SecurityFilter(pac4j.getConfig(), "KeycloakOidcClient", "role");
        env.servlets().addFilter("RequestFilterAuth", authFilter).
            addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/api/*");
    }

    private void initCors(final Environment environment)
    {
        final FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,PUT,PATCH,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,If-Modified-Since,Cache-Control,Tenant,Csrf-Token,X-TENANT-ID,CA-AGENT-SERVICE,CAF-Correlation-Id");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    }

    private KeycloakOidcConfiguration getPac4jConfig()
    {
        KeycloakOidcConfiguration config = new KeycloakOidcConfiguration();
        config.setClientId("dropwizard-project");
        config.setRealm("dev");
        config.setBaseUri("http://localhost:8082/auth");
        config.setDiscoveryURI("http://localhost:8082/auth");
//        config.setSecret("c4771e44-7c30-41f1-b50d-d658a64ecc1c");
        
        return config;
    }
    
//    private void initAuth(final Environment environment) {
//         final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
//        environment.getApplicationContext().setSecurityHandler(securityHandler);
//        final Constraint constraint = new Constraint();
//        constraint.setAuthenticate(true);
//
//        final Set<String> authRoles = new HashSet<String>();
//        authRoles.add("USER");
//        securityHandler.setRoles(authRoles);
//        constraint.setRoles(authRoles.toArray(new String[0]));
//
//        final ConstraintMapping constraintMapping = new ConstraintMapping();
//        constraintMapping.setPathSpec("/*");
//        constraintMapping.setConstraint(constraint);
//        securityHandler.addConstraintMapping(constraintMapping);
//        
////        final ConstraintMapping constraintMappingExclusion = new ConstraintMapping();
////        constraintMappingExclusion.setPathSpec("/hello-world");
////        constraintMappingExclusion.setMethodOmissions(new String[]{"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"});
////        constraintMappingExclusion.setConstraint(constraint);
////        securityHandler.addConstraintMapping(constraintMappingExclusion);
//
//        
////        getPac4jConfig();
////        
////        KeycloakConfig config = getConfig();
////        final KeycloakJettyAuthenticatorExt keycloak = new KeycloakJettyAuthenticatorExt();
////        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
////        final KeycloakResolver keycloakResolver = new KeycloakResolver(config);
////        keycloak.setConfigResolver(keycloakResolver);
//
////        final SessionHandler sessionHandler = new SessionHandler();
////        sessionHandler.getSessionCookieConfig().setSecure(false);
////        
////        environment.servlets().setSessionHandler(sessionHandler);
//    }

}
