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
            if (tarefa.getUsuarioResponsavel() != null && tarefa.getUsuarioResponsavel().getId() != null) {
                if (usuarioRepository.buscarPorId(tarefa.getUsuarioResponsavel().getId()) == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Usuário responsável inválido ou não encontrado.")
                            .build();
                }
            }
            if (tarefa.getTarefaPai() != null && tarefa.getTarefaPai().getId() != null) {
                if (tarefaRepository.buscarPorId(tarefa.getTarefaPai().getId()) == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Tarefa pai inválida ou não encontrada.")
                            .build();
                }
            }

            Tarefa tarefaCriada = tarefaRepository.criar(tarefa);
            TarefaResponseDTO responseDTO = new TarefaResponseDTO(tarefaCriada);
            return Response.status(Response.Status.CREATED).entity(responseDTO).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        catch (ConstraintDeclarationException e){
            StringBuilder mensagemErro = new StringBuilder("Erro de validação: ").append(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(mensagemErro.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar tarefa: " + e.getMessage())
                    .build();
        }
    }

    @GET
    public Response buscarTarefas(){
        try{
            List<Tarefa> tarefas = tarefaRepository.buscarTarefasPais();
            List<TarefaResponseDTO> responseDTOs = tarefas.stream()
                    .map(TarefaResponseDTO::new)
                    .collect(Collectors.toList());
            return Response.ok(responseDTOs).build();
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
            Tarefa tarefaExistente = tarefaRepository.buscarPorId(id); // Já com FETCH JOIN

            if (tarefaExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tarefa com ID " + id + " não encontrada.")
                        .build();
            }

            if (tarefaAtualizacao.getNomeTarefa() != null) {
                if (tarefaAtualizacao.getNomeTarefa().trim().isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("O nome da tarefa não pode ser vazio.")
                            .build();
                }
                tarefaExistente.setNomeTarefa(tarefaAtualizacao.getNomeTarefa());
            }

            if (tarefaAtualizacao.getDataInicio() != null) {
                tarefaExistente.setDataInicio(tarefaAtualizacao.getDataInicio());
            }

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

            if (tarefaAtualizacao.getUsuarioResponsavel() != null) {
                UUID usuarioId = tarefaAtualizacao.getUsuarioResponsavel().getId();
                if (usuarioId == null) {
                    tarefaExistente.setUsuarioResponsavel(null);
                } else {
                    Usuario usuarioGerenciado = usuarioRepository.buscarPorId(usuarioId);
                    if (usuarioGerenciado == null) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Usuário responsável inválido ou não encontrado.")
                                .build();
                    }
                    tarefaExistente.setUsuarioResponsavel(usuarioGerenciado);
                }
            }
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
            if (tarefaAtualizacao.getTarefaPai() != null) {
                UUID newTarefaPaiId = tarefaAtualizacao.getTarefaPai().getId();

                if (newTarefaPaiId == null) {
                    tarefaExistente.setTarefaPai(null);
                } else {
                    if (Objects.equals(newTarefaPaiId, id)) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Uma tarefa não pode ser sua própria tarefa pai.")
                                .build();
                    }
                    Tarefa novaTarefaPai = tarefaRepository.buscarPorId(newTarefaPaiId);
                    if (novaTarefaPai == null) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Tarefa pai inválida ou não encontrada.")
                                .build();
                    }
                    tarefaExistente.setTarefaPai(novaTarefaPai);
                }
            }

            Tarefa tarefaAtualizada = tarefaRepository.atualizar(tarefaExistente);

            TarefaResponseDTO responseDTO = new TarefaResponseDTO(tarefaAtualizada);
            return Response.ok(responseDTO).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar a tarefa: " + e.getMessage())
                    .build();
        }
    }

//    @DELETE
//    @Path("/{id}")
//    public Response deletarTarefa(@PathParam("id") UUID id) {
//        try {
//            tarefaRepository.(id);
//            return Response.noContent().build();
//        } catch (Exception e) {
//            e.printStackTrace(); // Adicione e.printStackTrace() para depuração
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Erro ao deletar a tarefa: " + e.getMessage())
//                    .build();
//        }
//    }
}