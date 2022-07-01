/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakRequest
{
    private String baseUrl;
    private String username;
    private String password;
}
