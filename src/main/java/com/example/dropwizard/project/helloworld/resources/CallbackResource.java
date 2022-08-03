/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.pac4j.jax.rs.annotations.Pac4JCallback;

/**
 *
 * @author TBroadbent
 */
@Path("/")
public class CallbackResource
{
    @GET
    @Path("/callback")
    @Pac4JCallback(defaultUrl = "/api/hello-world", defaultClient = "KeycloakOidcClient")
    public void callbackGet() {
    }
}
