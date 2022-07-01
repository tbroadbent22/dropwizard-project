package com.example.dropwizard.helloworld.keycloak;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KeycloakConfig
{
    private String realm;
    private String resource;
    private String authServerUrl;
    private String secret;
    private Credentials credentials;
    private Integer minTimeBetweenJwksRequests;
    private Integer publicCacheTtl;
    private String user;
    private String password;
    
    public String returnSecret() {
        return credentials.getSecret();
    }
    
    @Getter
    @Setter
    private class Credentials{
        String secret;
    }
}
