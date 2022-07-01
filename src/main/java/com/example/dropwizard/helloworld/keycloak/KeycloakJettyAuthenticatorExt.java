/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.helloworld.keycloak;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.security.ServerAuthException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Request;
import org.keycloak.adapters.jetty.KeycloakJettyAuthenticator;

/**
 *
 * @author TBroadbent
 */
public class KeycloakJettyAuthenticatorExt extends KeycloakJettyAuthenticator
{
    @Override
    public Authentication validateRequest(ServletRequest req, ServletResponse res, boolean mandatory) throws ServerAuthException
    {
        final Authentication authentication = super.validateRequest(req, res, true);

        return authentication;
    }

    @Override
    protected Request resolveRequest(final ServletRequest request)
    {
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest httpRequest = (HttpServletRequest) request;
        }
        return super.resolveRequest(request);
    }
}
