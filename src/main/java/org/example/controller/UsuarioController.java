package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;

import java.util.UUID;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {

    @Inject
    private UsuarioRepository usuarioRepository;


    @POST
    public Response criarUsuario(Usuario usuario){

        try {
            if (usuario.getNome().equals("")){
                return Response.status(Response.Status.FORBIDDEN).entity("É necessário informar o nome do usuário!").build();
            }
            if (usuario.getNome() instanceof String){
                return Response.status(Response.Status.FORBIDDEN).entity("O nome do usuário não pode conter números!").build();
            }

            Usuario usuarioCriado = usuarioRepository.criar(usuario);
            return Response.status(Response.Status.CREATED).entity("Usuario Criado!" + usuarioCriado.getNome()).build();

        }catch (Exception e){

            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar usuario: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarUsuarioPorId(@PathParam("id") UUID id){
            Usuario usuario = usuarioRepository.buscarPorId(id);

            if(usuario != null){
                return  Response.ok(usuario).build();
            }else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuário não encontrado para o ID: " + id)
                        .build();
            }
    }



}
