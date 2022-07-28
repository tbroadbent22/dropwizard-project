/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld;

import com.example.dropwizard.helloworld.keycloak.CustomAuthorizer;
import com.example.dropwizard.project.helloworld.resources.CallbackResource;
import com.example.dropwizard.project.helloworld.resources.HelloWorldResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Providers;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.security.Constraint;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.http.callback.NoParameterCallbackUrlResolver;
import org.pac4j.dropwizard.Pac4jBundle;
import org.pac4j.dropwizard.Pac4jFactory;
import org.pac4j.jax.rs.features.Pac4JSecurityFeature;
import org.pac4j.jax.rs.filters.CallbackFilter;
import org.pac4j.jax.rs.jersey.features.Pac4JValueFactoryProvider;
import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider;
import org.pac4j.jee.filter.SecurityFilter;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;
import org.pac4j.oidc.credentials.authenticator.UserInfoOidcAuthenticator;

public class HelloWorldApplication extends Application<HelloWorldConfiguration>
{
    @Context
    private Providers providers;
    
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }
    
 final Pac4jBundle<HelloWorldConfiguration> bundle = new Pac4jBundle<HelloWorldConfiguration>() {
        @Override
        public Pac4jFactory getPac4jFactory(HelloWorldConfiguration configuration) {
            return new Pac4jFactory();
//            return configuration.getPac4jFactory();
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
        
//        environment.jersey().register(new org.glassfish.hk2.utilities.binding.AbstractBinder() {
//            @Override
//            protected void configure() {
//                // so that we can inject the config in resources if needed
//                bind(build()).to(Config.class);
//            }
//        });
        final CallbackResource cbResource = new CallbackResource();
        


        System.out.println("ENV: " + environment);
        environment.jersey().register(resource);
        environment.jersey().register(cbResource);
        initCors(environment);
//        initAuth(environment);
        
        SecurityFilter authFilter = new SecurityFilter();
        authFilter.setSharedConfig(build());
        
        CallbackFilter cbFilter = new CallbackFilter(providers);
        
        
//        environment.servlets().addFilter("callbackFilter", cbFilter);
//        cbFilter.setSharedConfig(build());
        
//        System.out.println("DEFAULT URL: " + cbFilter.getDefaultUrl());

    environment.jersey().register(ServletJaxRsContextFactoryProvider.class);
    environment.jersey().register(new Pac4JSecurityFeature());
    environment.jersey().register(new Pac4JValueFactoryProvider.Binder());
        
        environment.servlets().addFilter("RequestFilterAuth", authFilter).
            addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/api/*");
    }
    
    
    public Config build()
    {
        final KeycloakOidcClient oidcClient = new KeycloakOidcClient(getPac4jConfig());
        oidcClient.setAuthorizationGenerator((ctx, session, profile) -> {
            profile.addRole("USER");
            return Optional.of(profile);
        });
        oidcClient.setAuthenticator(new UserInfoOidcAuthenticator(getPac4jConfig()));
        oidcClient.setCallbackUrl("http://localhost:8080/callback");
        oidcClient.setCallbackUrlResolver(new NoParameterCallbackUrlResolver());
        oidcClient.setName("dropwizard-project");


        final Clients clients = new Clients("http://localhost:8080/callback",oidcClient);
        final Config config = new Config(clients);
//        config.setCallbackLogic(new DefaultCallbackLogic());
        //config.setAuthorizer(new RequireAnyRoleAuthorizer("user"));
        config.addAuthorizer("custom", new CustomAuthorizer());
        return config;
    }
    
    private KeycloakOidcConfiguration getPac4jConfig()
    {
        KeycloakOidcConfiguration config = new KeycloakOidcConfiguration();
        config.setClientId("dropwizard-project");
        config.setRealm("Dev");
        config.setBaseUri("http://localhost:8082");
        config.setSecret("DfHOUiU75hdyEkmPLxE1eZyYvw5MAF2D");
        config.setDiscoveryURI("http://localhost:8082/.well-known/openid-configuration");
//        config.setUseNonce(true);
        //config.setConnectTimeout(5000000);
        //config.setPkceMethod(CodeChallengeMethod.getDefault());
        return config;
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
        
//        final ConstraintMapping constraintMappingExclusion = new ConstraintMapping();
//        constraintMappingExclusion.setPathSpec("/hello-world");
//        constraintMappingExclusion.setMethodOmissions(new String[]{"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"});
//        constraintMappingExclusion.setConstraint(constraint);
//        securityHandler.addConstraintMapping(constraintMappingExclusion);

        
//        getPac4jConfig();
//        
//        KeycloakConfig config = getConfig();
//        final KeycloakJettyAuthenticatorExt keycloak = new KeycloakJettyAuthenticatorExt();
//        environment.getApplicationContext().getSecurityHandler().setAuthenticator(keycloak);
//        final KeycloakResolver keycloakResolver = new KeycloakResolver(config);
//        keycloak.setConfigResolver(keycloakResolver);

//        final SessionHandler sessionHandler = new SessionHandler();
//        sessionHandler.getSessionCookieConfig().setSecure(false);
//        
//        environment.servlets().setSessionHandler(sessionHandler);
    }

//    private KeycloakConfig getConfig()
//    {
//        KeycloakConfig config = new KeycloakConfig();
//        config.setAuthServerUrl("http://localhost:8081");
//        config.setSecret("34f37982-733c-49ed-a840-11166f044ef8");
//        config.setPassword("password");
//        config.setRealm("dev");
//        config.setUser("tony");
//        config.setResource("dropwizard-project");
//        
//        return config;
//    }
//    
//    private OidcAuthenticator getPac4jConfig()
//    {   
//        KeycloakOidcConfiguration kcConfig = new KeycloakOidcConfiguration();
//        kcConfig.setBaseUri("http://localhost:8082/auth");
//        kcConfig.setRealm("dev");
//        kcConfig.setSecret("34f37982-733c-49ed-a840-11166f044ef8");
//        kcConfig.setClientId("dropwizard-project");
//        KeycloakOidcClient client = new KeycloakOidcClient(kcConfig);
//        client.setCallbackUrl("http://localhost:8082/auth");
//        client.init(true);
//        
//        return new OidcAuthenticator(kcConfig, client);
//        
//        
//    }
//    
//    private void setupAuthenticator(){
////        OidcAuthenticator auth = new OidcAuthenticator();
//    }
//    
//    private KeycloakOidcProfile getKcConfig() {
//        KeycloakOidcProfile kc = new KeycloakOidcProfile();
//        kc.setClientName("dropwizard-project");
//        kc.setId("dev");
//        
//        final Set<String> authRoles = new HashSet<String>();
//        authRoles.add("USER");
//        kc.setRoles(authRoles);
//        
////        Credentials c = new Credentials();
//        
//        return kc;
//    }
}
