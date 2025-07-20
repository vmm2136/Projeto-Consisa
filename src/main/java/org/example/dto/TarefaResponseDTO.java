package org.example.dto;

import org.example.model.StatusTarefa;
import org.example.model.Tarefa;
import org.example.model.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TarefaResponseDTO {
    private UUID id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String nomeTarefa;
    private UsuarioResponseDTO usuarioResponsavel;
    private StatusTarefa statusTarefa;
    private TarefaParentDTO tarefaPai;
    private List<TarefaResponseDTO> tarefasFilhas;

    public TarefaResponseDTO() {}

    public TarefaResponseDTO(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.dataInicio = tarefa.getDataInicio();
        this.dataFim = tarefa.getDataFim();
        this.nomeTarefa = tarefa.getNomeTarefa();
        this.statusTarefa = tarefa.getStatusTarefa();

        if (tarefa.getUsuarioResponsavel() != null) {
            try {
                this.usuarioResponsavel = new UsuarioResponseDTO(tarefa.getUsuarioResponsavel());
            } catch (Exception e) {
                System.err.println("WARN: Não foi possível inicializar UsuarioResponsavel para serialização: " + e.getMessage());
            }
        }

        if (tarefa.getTarefaPai() != null) {
            try {
                this.tarefaPai = new TarefaParentDTO(tarefa.getTarefaPai());
            } catch (Exception e) {
                System.err.println("WARN: Não foi possível inicializar TarefaPai para serialização: " + e.getMessage());
            }
        }

        if (tarefa.getTarefasFilhas() != null) {
            try {
                tarefa.getTarefasFilhas().size();
                this.tarefasFilhas = tarefa.getTarefasFilhas().stream()
                        .map(TarefaResponseDTO::new)
                        .collect(java.util.stream.Collectors.toList());
            } catch (Exception e) {
                System.err.println("WARN: Não foi possível inicializar TarefasFilhas para serialização: " + e.getMessage());
            }
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public String getNomeTarefa() { return nomeTarefa; }
    public void setNomeTarefa(String nomeTarefa) { this.nomeTarefa = nomeTarefa; }
    public UsuarioResponseDTO getUsuarioResponsavel() { return usuarioResponsavel; }
    public void setUsuarioResponsavel(UsuarioResponseDTO usuarioResponsavel) { this.usuarioResponsavel = usuarioResponsavel; }
    public StatusTarefa getStatusTarefa() { return statusTarefa; }
    public void setStatusTarefa(StatusTarefa statusTarefa) { this.statusTarefa = statusTarefa; }
    public TarefaParentDTO getTarefaPai() { return tarefaPai; }
    public void setTarefaPai(TarefaParentDTO tarefaPai) { this.tarefaPai = tarefaPai; }
    public List<TarefaResponseDTO> getTarefasFilhas() { return tarefasFilhas; }
    public void setTarefasFilhas(List<TarefaResponseDTO> tarefasFilhas) { this.tarefasFilhas = tarefasFilhas; }
}

