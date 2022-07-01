/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld.resources;

import com.codahale.metrics.annotation.Timed;
import com.example.dropwizard.helloworld.auth.AuthorizeUser;
import com.example.dropwizard.project.helloworld.api.Saying;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource
{
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@Context final HttpServletRequest request, @QueryParam("name") Optional<String> name) {
        System.out.println("TEST");
        final String value = String.format(template, name.orElse(defaultName));
//        AuthorizeUser au = new AuthorizeUser("http://localhost:8081");
//        au.authoriseUser(request.getHeader("Authorization").toLowerCase());
//        System.out.println("REQUEST: " + request.getHeader("Authorization").toLowerCase());
        return new Saying(counter.incrementAndGet(), value);
    }
}
