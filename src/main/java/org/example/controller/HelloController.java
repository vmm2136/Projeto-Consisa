package org.example.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class HelloController {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String digaOla(){
        return  "Olá WildFly!";
    }

}
