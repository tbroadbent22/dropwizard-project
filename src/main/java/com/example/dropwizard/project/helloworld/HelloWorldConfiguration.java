/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dropwizard.project.helloworld;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.pac4j.dropwizard.Pac4jFactory;

@Getter
@Setter
public class HelloWorldConfiguration extends Configuration
{
    
    public HelloWorldConfiguration()
    {
        super();
    }
   
    
    @JsonProperty
    @NotEmpty
    private String template;

    @JsonProperty
    @NotEmpty
    private String defaultName = "Stranger";
    
    @NotNull
    @Nullable
    Pac4jFactory pac4j = new Pac4jFactory();

    @JsonProperty("pac4j")
    public Pac4jFactory getPac4jFactory() {
        return pac4j;
    }

    @JsonProperty("pac4j")
    public void setPac4jFactory(Pac4jFactory pac4jFactory) {
        this.pac4j = pac4jFactory;
    }
}
