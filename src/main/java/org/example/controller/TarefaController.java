package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.model.Tarefa;
import org.example.model.Usuario;
import org.example.repository.TarefaRepository;
import org.example.repository.UsuarioRepository;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Valid;

import java.util.List;

@Path("/tarefas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TarefaController {

    @Inject
    private TarefaRepository tarefaRepository;

    @Inject
    private UsuarioRepository usuarioRepository;

    @POST
    public Response criarTarefa(@Valid Tarefa tarefa){
        try{

            Tarefa tarefaCriada = tarefaRepository.criar(tarefa);
            return Response.status(Response.Status.CREATED).entity(tarefaCriada).build();
        }catch (ConstraintDeclarationException e){
            StringBuilder mensagemErro = new StringBuilder("Erro de validação: ").append(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(mensagemErro.toString()).build();
        }
    }

    @GET
    public Response buscarTarefas(){
        try{
            List<Tarefa> tarefas = tarefaRepository.buscarTarefasPais();
            return Response.ok(tarefas).build();
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tarefas: " + e.getMessage())
                    .build();
        }

    }
}
