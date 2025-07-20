// Exemplo de TarefaParentDTO (ajuste conforme suas necessidades)
package org.example.dto;

import java.util.UUID;

public class TarefaParentDTO {
    private UUID id;
    private String nomeTarefa; // Adicione campos que você realmente precisa do pai

    public TarefaParentDTO() {}

    public TarefaParentDTO(org.example.model.Tarefa tarefaPai) {
        this.id = tarefaPai.getId();
        // Acessar `tarefaPai.getNomeTarefa()` aqui pode causar LI Exception
        // se `nomeTarefa` não estiver mapeado EAGERLY ou se o proxy não for inicializado
        // antes de passar para o DTO.
        // O ideal é que o `buscarPorId` ou `update` no repositório já tenha carregado isso.
        this.nomeTarefa = tarefaPai.getNomeTarefa(); // Se precisar do nome do pai
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNomeTarefa() { return nomeTarefa; }
    public void setNomeTarefa(String nomeTarefa) { this.nomeTarefa = nomeTarefa; }
}