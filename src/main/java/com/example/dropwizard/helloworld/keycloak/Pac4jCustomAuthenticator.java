///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.example.dropwizard.helloworld.keycloak;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import org.eclipse.jetty.security.Authenticator;
//import org.eclipse.jetty.security.ServerAuthException;
//import org.eclipse.jetty.security.authentication.LoginAuthenticator;
//import org.eclipse.jetty.server.Authentication;
//import org.eclipse.jetty.server.Request;
//import org.pac4j.core.context.WebContext;
//import org.pac4j.core.credentials.Credentials;
//
///**
// *
// * @author TBroadbent
// */
//public class Pac4jCustomAuthenticator extends LoginAuthenticator
//{
//    @Override
//    public boolean secureResponse(ServletRequest req, ServletResponse res, boolean mandatory, Authentication.User validatedUser) throws ServerAuthException {
//        return true;
//    }
//    
//    @Override
//    public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory) throws ServerAuthException
//    {
//        final Authentication authentication = super.validateRequest(req, res, true);
//
//        return authentication;
//    }
//
//    @Override
//    protected Request resolveRequest(final ServletRequest request)
//    {
//        if (request instanceof HttpServletRequest) {
//            final HttpServletRequest httpRequest = (HttpServletRequest) request;
//        }
//        return super.resolveRequest(request);
//    }
//}
