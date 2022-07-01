/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.helloworld.keycloak;

//import com.microfocus.apollo.idm.idmneo.KeycloakFactory;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.keycloak.adapters.spi.HttpFacade.Request;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.adapters.config.AdapterConfig;

@AllArgsConstructor
public class KeycloakResolver implements KeycloakConfigResolver
{
    private KeycloakConfig keycloakConfig;
    
    @Override 
    public KeycloakDeployment resolve(Request request){
        
        System.out.println("KEYCLOAK CONFIG...");
        AdapterConfig adapterConfig = getMyConfig();
        
        if(adapterConfig != null) {
            KeycloakDeployment deployment = KeycloakDeploymentBuilder.build(adapterConfig);
            return KeycloakDeploymentBuilder.build(adapterConfig);
        }
        
        return null;
    }
    
    private AdapterConfig getMyConfig() {
//        Keycloak keycloak = null;
        
        final KeycloakConfig config = getConfig();
        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setRealm(config.getRealm());
        adapterConfig.setAuthServerUrl(config.getAuthServerUrl());
        adapterConfig.setResource(config.getResource());
        adapterConfig.setCors(true);
        adapterConfig.setUseResourceRoleMappings(true);

//        keycloak = KeycloakFactory.getMasterRealmKeycloak(config.getAuthServerUrl(), config.getUser(), config.getPassword());

        final Map<String, Object> creds = new HashMap<>();
        creds.put("secret", config.getSecret());
        adapterConfig.setCredentials(creds);

        return adapterConfig;
    }
    
    private KeycloakConfig getConfig()
    {
        KeycloakConfig config = new KeycloakConfig();
        config.setAuthServerUrl("http://localhost:8082/auth");
        config.setSecret("eca94c43-37d0-4845-ba94-3ecdeda18901");
        config.setPassword("password");
        config.setRealm("dev");
        config.setUser("tony");
        config.setResource("dropwizard-project");
        
        return config;
    }
}
