/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.helloworld.auth;

import java.util.Collections;
import java.util.List;
import org.pac4j.core.client.DirectClient;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.engine.SecurityGrantedAccessAdapter;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.http.adapter.HttpActionAdapter;
import org.pac4j.core.profile.UserProfile;
import static org.pac4j.core.util.CommonHelper.assertNotNull;
import static org.pac4j.core.util.CommonHelper.isEmpty;
import static org.pac4j.core.util.CommonHelper.isNotEmpty;

/**
 *
 * @author TBroadbent
 */
public class CustomSecurityLogic extends DefaultSecurityLogic
{
     @Override
    public Object perform(final WebContext context, final SessionStore sessionStore, final Config config,
                          final SecurityGrantedAccessAdapter securityGrantedAccessAdapter, final HttpActionAdapter httpActionAdapter,
                          final String clients, final String authorizers, final String matchers, final Object... parameters) {

        //LOGGER.debug("=== SECURITY ===");

        System.out.println("OVERRIDE");
        HttpAction action = null;
        try {
            final var configClients = config.getClients();

            // logic
            //LOGGER.debug("url: {}", context.getFullRequestURL());
            //LOGGER.debug("clients: {} | matchers: {}", clients, matchers);
//            final var currentClients = clientFinder.find(configClients, context, clients);
            //LOGGER.debug("currentClients: {}", currentClients);

//            if (matchingChecker.matches(context, sessionStore, matchers, config.getMatchers(), currentClients)) {
//
//                final var manager = getProfileManager(context, sessionStore);
//                manager.setConfig(config);
//                var profiles = this.loadProfilesFromSession
//                    ? loadProfiles(manager, context, sessionStore, currentClients)
//                    : List.<UserProfile>of();
//                //LOGGER.debug("Loaded profiles (from session: {}): {} ", this.loadProfilesFromSession, profiles);
//
//                // no profile and some current clients
//                if (isEmpty(profiles) && isNotEmpty(currentClients)) {
//                    var updated = false;
//                    // loop on all clients searching direct ones to perform authentication
//                    for (final var currentClient : currentClients) {
//                        if (currentClient instanceof DirectClient) {
//                            //LOGGER.debug("Performing authentication for direct client: {}", currentClient);
//
//                            final var credentials = currentClient.getCredentials(context, sessionStore);
//                            //LOGGER.debug("credentials: {}", credentials);
//                            if (credentials.isPresent()) {
//                                final var optProfile =
//                                    currentClient.getUserProfile(credentials.get(), context, sessionStore);
//                                //LOGGER.debug("profile: {}", optProfile);
//                                if (optProfile.isPresent()) {
//                                    final var profile = optProfile.get();
//                                    final var directClient = (DirectClient) currentClient;
//                                    final boolean saveProfileInSession = directClient.getSaveProfileInSession(context, profile);
//                                    final var multiProfile = directClient.isMultiProfile(context, profile);
//                                    //LOGGER.debug("saveProfileInSession: {} / multiProfile: {}", saveProfileInSession, multiProfile);
//                                    manager.save(saveProfileInSession, profile, multiProfile);
//                                    updated = true;
//                                    if (!multiProfile) {
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (updated) {
//                        profiles = loadProfiles(manager, context, sessionStore, currentClients);
//                        //LOGGER.debug("Reloaded profiles: {}", profiles);
//                    }
//                }
//
//                // we have profile(s) -> check authorizations; otherwise, redirect to identity provider or 401
//                if (isNotEmpty(profiles)) {
//                    //LOGGER.debug("authorizers: {}", authorizers);
//                    if (authorizationChecker.isAuthorized(context, sessionStore, profiles,
//                                                          authorizers, config.getAuthorizers(), currentClients)) {
//                        //LOGGER.debug("authenticated and authorized -> grant access");
//                        return securityGrantedAccessAdapter.adapt(context, sessionStore, profiles, parameters);
//                    } else {
//                        //LOGGER.debug("forbidden");
//                        action = forbidden(context, sessionStore, currentClients, profiles, authorizers);
//                    }
//                } else {
//                    if (startAuthentication(context, sessionStore, currentClients)) {
//                        //LOGGER.debug("Starting authentication");
//                        saveRequestedUrl(context, sessionStore, currentClients, config.getClients().getAjaxRequestResolver());
//                        action = redirectToIdentityProvider(context, sessionStore, currentClients);
//                    } else {
//                        //LOGGER.debug("unauthorized");
//                        action = unauthorized(context, sessionStore, currentClients);
//                    }
//                }
//
//            } else {
//
//                //LOGGER.debug("no matching for this request -> grant access");
//                return securityGrantedAccessAdapter.adapt(context, sessionStore, Collections.emptyList(), parameters);
//            }

        } catch (final Exception e) {
            return handleException(e, httpActionAdapter, context);
        }

        return httpActionAdapter.adapt(action, context);
    }
}
