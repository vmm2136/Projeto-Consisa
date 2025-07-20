package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.TarefaResponseDTO;
import org.example.model.StatusTarefa;
import org.example.model.Tarefa;
import org.example.model.Usuario;
import org.example.repository.TarefaRepository;
import org.example.repository.UsuarioRepository;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @GET
    @Path("/{tarefaPaiId}/filhas")
    public Response getTarefasFilhas(@PathParam("tarefaPaiId") UUID tarefaPaiId) {
        try {
            List<Tarefa> tarefasFilhas = tarefaRepository.buscarTarefasFilhas(tarefaPaiId);
            List<TarefaResponseDTO> responseDTOs = tarefasFilhas.stream()
                    .map(TarefaResponseDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(responseDTOs).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tarefas filhas: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/{id}")
    public Response atualizarTarefa(@PathParam("id") UUID id, Tarefa tarefaAtualizacao) {
        try {
            Tarefa tarefaExistente = tarefaRepository.buscarPorId(id);

            if (tarefaExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tarefa com ID " + id + " não encontrada.")
                        .build();
            }

            // Atualizar o nome da tarefa
            if (tarefaAtualizacao.getNomeTarefa() != null) {
                if (tarefaAtualizacao.getNomeTarefa().trim().isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("O nome da tarefa não pode ser vazio.")
                            .build();
                }
                tarefaExistente.setNomeTarefa(tarefaAtualizacao.getNomeTarefa());
            }

            // Atualizar a data de inicio (quando definido o status iniciada)
            if (tarefaAtualizacao.getDataInicio() != null) {
                tarefaExistente.setDataInicio(tarefaAtualizacao.getDataInicio());
            }

            // Atualizar a data de fim
            if (tarefaAtualizacao.getDataFim() != null) {
                tarefaExistente.setDataFim(tarefaAtualizacao.getDataFim());
            }

            if (tarefaExistente.getDataInicio() != null && tarefaExistente.getDataFim() != null) {
                if (tarefaExistente.getDataFim().isBefore(tarefaExistente.getDataInicio())) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("A data de fim não pode ser anterior à data de início.")
                            .build();
                }
            }

            // Atualizar o usuário responsável
            if (tarefaAtualizacao.getUsuarioResponsavel() != null) {

                 if (tarefaAtualizacao.getUsuarioResponsavel().getId() == null ||
                     usuarioRepository.buscarPorId(tarefaAtualizacao.getUsuarioResponsavel().getId()) == null) {
                     return Response.status(Response.Status.BAD_REQUEST)
                             .entity("Usuário responsável inválido ou não encontrado.")
                             .build();
                 }
                tarefaExistente.setUsuarioResponsavel(tarefaAtualizacao.getUsuarioResponsavel());
            } else if (tarefaAtualizacao.getUsuarioResponsavel() == null && tarefaAtualizacao.getUsuarioResponsavel() != null) {
                tarefaExistente.setUsuarioResponsavel(null);
            }

            // Atualizar o status
            if (tarefaAtualizacao.getStatusTarefa() != null) {
                try {
                    StatusTarefa.valueOf(tarefaAtualizacao.getStatusTarefa().name());
                    tarefaExistente.setStatusTarefa(tarefaAtualizacao.getStatusTarefa());
                } catch (IllegalArgumentException e) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Status da tarefa inválido: " + tarefaAtualizacao.getStatusTarefa())
                            .build();
                }
            }

            // Atualizar o id_tarefa_pai
            if (tarefaAtualizacao.getTarefaPai() != null) {
                if (tarefaAtualizacao.getTarefaPai().getId() == null ||
                        tarefaRepository.buscarPorId(tarefaAtualizacao.getTarefaPai().getId()) == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Tarefa pai inválida ou não encontrada.")
                            .build();
                }
                if (Objects.equals(tarefaAtualizacao.getTarefaPai().getId(), id)) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Uma tarefa não pode ser sua própria tarefa pai.")
                            .build();
                }
                tarefaExistente.setTarefaPai(tarefaAtualizacao.getTarefaPai());
            } else if (tarefaAtualizacao.getTarefaPai() == null && tarefaAtualizacao.getTarefaPai() != null) {
                tarefaExistente.setTarefaPai(null);
            }

            Tarefa tarefaAtualizada = tarefaRepository.atualizar(tarefaExistente);
            return Response.ok(tarefaAtualizada).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar a tarefa: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarTarefa(@PathParam("id") UUID id) {
        try {
            tarefaRepository.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar a tarefa: " + e.getMessage())
                    .build();
        }
    }

}
