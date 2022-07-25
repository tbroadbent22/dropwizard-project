/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld.resources;

import com.codahale.metrics.annotation.Timed;
import com.example.dropwizard.project.helloworld.api.Saying;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.pac4j.core.http.callback.NoParameterCallbackUrlResolver;

/**
 *
 * @author TBroadbent
 */
@Path("/callback")
public class CallbackResource 
{
    @GET
    public void sayHello(@Context final HttpServletRequest request, @QueryParam("name") Optional<String> name) {
        System.out.println("TEST");
    }
}
