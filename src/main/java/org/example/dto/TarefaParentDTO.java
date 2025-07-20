package org.example.dto;

import org.example.model.Tarefa;
import java.util.UUID;

public class TarefaParentDTO {
    private UUID id;
    private String nomeTarefa;

    public TarefaParentDTO() {}

    public TarefaParentDTO(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.nomeTarefa = tarefa.getNomeTarefa();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNomeTarefa() { return nomeTarefa; }
    public void setNomeTarefa(String nomeTarefa) { this.nomeTarefa = nomeTarefa; }
}