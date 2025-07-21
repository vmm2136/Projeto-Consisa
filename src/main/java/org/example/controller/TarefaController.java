package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.dto.TarefaResponseDTO;
import org.example.dto.UsuarioResponseDTO;
import org.example.model.Tarefa;
import org.example.model.Usuario;
import org.example.repository.TarefaRepository;
import org.example.repository.UsuarioRepository;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
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

            if (responseDTOs != null && !responseDTOs.isEmpty()) {
                return Response.ok(responseDTOs).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Não existem tarefas principais cadastradas!")
                        .build();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tarefas: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{tarefaPaiId}/filhas")
    public Response buscarTarefasFilhas(@PathParam("tarefaPaiId") UUID tarefaPaiId) {
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
    @Path("/{id}/nome")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarNomeTarefa(@PathParam("id") UUID idTarefa, TarefaResponseDTO tarefaResponseDTO) {

        if (tarefaResponseDTO == null || tarefaResponseDTO.getNomeTarefa() == null || tarefaResponseDTO.getNomeTarefa().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O nome da tarefa não pode ser nulo ou vazio.")
                    .build();
        }

        Tarefa tarefaOld = tarefaRepository.buscarPorId(idTarefa);
        tarefaOld.setNomeTarefa(tarefaResponseDTO.getNomeTarefa());

        try {
            tarefaRepository.atualizar(tarefaOld);
            return Response.ok(tarefaOld).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar o nome da tarefa: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/data-inicio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarDataInicioTarefa(@PathParam("id") UUID idTarefa,TarefaResponseDTO tarefaResponseDTO) {

        if (tarefaResponseDTO == null || tarefaResponseDTO.getDataInicio() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A data de início não pode ser nula.")
                    .build();
        }

        Tarefa tarefaOld = tarefaRepository.buscarPorId(idTarefa);
        LocalDate novaDataInicio = tarefaResponseDTO.getDataInicio();
        tarefaOld.setDataInicio(novaDataInicio);


        try {
            tarefaRepository.atualizar(tarefaOld);
            return Response.ok(tarefaOld).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar a data de início da tarefa: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/data-fim")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarDataFimTarefa(@PathParam("id") UUID idTarefa,TarefaResponseDTO tarefaResponseDTO) {

        if (tarefaResponseDTO == null || tarefaResponseDTO.getDataFim() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A data de fim não pode ser nula.")
                    .build();
        }

        Tarefa tarefaOld = tarefaRepository.buscarPorId(idTarefa);
        LocalDate novaDataFim = tarefaResponseDTO.getDataFim();
        tarefaOld.setDataFim(novaDataFim);

        try {
            tarefaRepository.atualizar(tarefaOld);
            return Response.ok(tarefaOld).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar a data de fim da tarefa: " + e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/{id}/usuario-responsavel")
    public Response atualizarUsuarioResponsavel(@PathParam("id") UUID idTarefa, UsuarioResponseDTO usuarioResponsavelPatch) {
        if (usuarioResponsavelPatch == null || usuarioResponsavelPatch.getIdUsuarioResponsavel() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O ID do usuário responsável é obrigatório no corpo da requisição.")
                    .build();
        }

        Tarefa tarefaOld = tarefaRepository.buscarPorId(idTarefa);
        Usuario usuario = usuarioRepository.buscarPorId(usuarioResponsavelPatch.getIdUsuarioResponsavel());
        if(usuario == null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário selecionado não existe na base de dados!")
                    .build();
        }
        tarefaOld.setUsuarioResponsavel(usuario);

        try {
            tarefaRepository.atualizar(tarefaOld);
            return Response.ok(tarefaOld).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno ao atualizar o usuário responsável da tarefa.")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletarTarefa(@PathParam("id") UUID id) {
        try {
            Tarefa tarefaExistente = tarefaRepository.buscarPorId(id);
            if (tarefaExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tarefa com ID " + id + " não encontrada para exclusão.")
                        .build();
            }

            tarefaRepository.deletar(id);
            return Response.noContent().build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("ID de tarefa inválido.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar tarefa: " + e.getMessage())
                    .build();
        }
    }
}