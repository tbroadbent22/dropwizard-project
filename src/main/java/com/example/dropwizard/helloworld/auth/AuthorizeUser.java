/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.helloworld.auth;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;

/**
 *
 * @author TBroadbent
 */
@AllArgsConstructor
public class AuthorizeUser
{
    private final String keycloakUrl;
    
    public void authoriseUser(final String token) {
        System.out.println("TOKEN: " + token);
//        System.out.println("URL: " + keycloakUrl+"/auth/realms/dev/protocol/openid-connect/userinfo");
        
        UserInfo userInfo = getUserInfo(token);
//        System.out.println("USER: " + userInfo.getName());
    }
    
    private UserInfo getUserInfo(final String token) {
        Client client = ClientBuilder.newClient();
        client.register("bearer " + token);
        
        return client.target("http://localhost:8081/auth/realms/dev/protocol/openid-connect/userinfo")
            .request(MediaType.APPLICATION_JSON).get(UserInfo.class);
    }
    
}
