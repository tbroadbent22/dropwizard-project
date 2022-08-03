///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.example.dropwizard.helloworld.providers;
//
//import com.example.dropwizard.helloworld.keycloak.CustomAuthorizer;
//import java.util.Optional;
//import javax.ws.rs.core.Feature;
//import javax.ws.rs.core.FeatureContext;
//import javax.ws.rs.ext.Provider;
//import org.pac4j.core.client.Clients;
//import org.pac4j.core.config.Config;
//import org.pac4j.core.http.callback.NoParameterCallbackUrlResolver;
//import org.pac4j.jax.rs.features.JaxRsConfigProvider;
//import org.pac4j.jax.rs.features.Pac4JSecurityFeature;
//import org.pac4j.jax.rs.jersey.features.Pac4JValueFactoryProvider;
//import org.pac4j.jax.rs.resteasy.features.Pac4JProfileInjectorFactory;
//import org.pac4j.jax.rs.servlet.features.ServletJaxRsContextFactoryProvider;
//import org.pac4j.oidc.client.KeycloakOidcClient;
//import org.pac4j.oidc.config.KeycloakOidcConfiguration;
//import org.pac4j.oidc.credentials.authenticator.UserInfoOidcAuthenticator;
//
///**
// *
// * @author TBroadbent
// */
//
//@Provider
//public class Pac4JFeature implements Feature
//{
//    
//    @Override
//    public boolean configure(FeatureContext context) {
//        context
//            .register(new JaxRsConfigProvider(build()))
//            .register(new Pac4JSecurityFeature())
//            .register(new Pac4JValueFactoryProvider.Binder()) // only with Jersey
//            .register(new Pac4JProfileInjectorFactory()) // only with Resteasy
//            .register(new ServletJaxRsContextFactoryProvider());
//
//        return true;
//    }
//    
//        public Config build()
//    {
//        final KeycloakOidcClient oidcClient = new KeycloakOidcClient(getPac4jConfig());
//        oidcClient.setAuthorizationGenerator((ctx, session, profile) -> {
//            profile.addRole("USER");
//            return Optional.of(profile);
//        });
//        oidcClient.setAuthenticator(new UserInfoOidcAuthenticator(getPac4jConfig()));
////        oidcClient.setCallbackUrl("http://localhost:8080/callback");
//        oidcClient.setCallbackUrlResolver(new NoParameterCallbackUrlResolver());
//        oidcClient.setName("dropwizard-project");
//
//
//        final Clients clients = new Clients("http://localhost:8080/callback",oidcClient);
//        final Config config = new Config(clients);
//        //config.setAuthorizer(new RequireAnyRoleAuthorizer("user"));
//        config.addAuthorizer("custom", new CustomAuthorizer());
////        CallbackLogic cbl = new CallbackLogic()
////        config.setCallbackLogic();
//        return config;
//    }
//        
//        private KeycloakOidcConfiguration getPac4jConfig()
//    {
//        KeycloakOidcConfiguration config = new KeycloakOidcConfiguration();
//        config.setClientId("dropwizard-project");
//        config.setRealm("dev");
//        config.setBaseUri("http://localhost:8082");
//        config.setSecret("DfHOUiU75hdyEkmPLxE1eZyYvw5MAF2D");
//        config.setDiscoveryURI("http://localhost:8082/.well-known/openid-configuration");
////        config.setUseNonce(true);
//        //config.setConnectTimeout(5000000);
//        //config.setPkceMethod(CodeChallengeMethod.getDefault());
//        return config;
//    }
//}
//
//
