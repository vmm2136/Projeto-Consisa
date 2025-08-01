package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@Table(name = "tarefas")
public class Tarefa implements Serializable {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "data_inicio", nullable = true)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = true)
    private LocalDate dataFim;

    @Column(name = "nome_tarefa", nullable = false)
    private String nomeTarefa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_responsavel", nullable = true)
    @JsonbTransient
    private Usuario usuarioResponsavel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_tarefa", nullable = false)
    private StatusTarefa statusTarefa = StatusTarefa.AGUARDANDO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tarefa_pai_id")
    private Tarefa tarefaPai;

    @OneToMany(mappedBy = "tarefaPai", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonbTransient
    private List<Tarefa> tarefasFilhas;

    public Tarefa(){
    }

    public UUID getId(){
        return id;
    }
    public void setId(UUID id){
        this.id = id;
    }
    public LocalDate getDataInicio(){
        return dataInicio;
    }
    public void setDataInicio(LocalDate dataInicio){
        this.dataInicio = dataInicio;
    }
    public LocalDate getDataFim(){
        return dataFim;
    }
    public void setDataFim(LocalDate dataFim){
        this.dataFim = dataFim;
    }
    public String getNomeTarefa(){
        return nomeTarefa;
    }
    public void setNomeTarefa(String nomeTarefa){
        this.nomeTarefa = nomeTarefa;
    }
    public Usuario getUsuarioResponsavel(){
        return usuarioResponsavel;
    }
    public void setUsuarioResponsavel(Usuario usuarioResponsavel){
        this.usuarioResponsavel = usuarioResponsavel;
    }
    public StatusTarefa getStatusTarefa(){
        return statusTarefa;
    }
    public void setStatusTarefa(StatusTarefa statusTarefa){
        this.statusTarefa = statusTarefa;
    }
    public Tarefa getTarefaPai(){
        return tarefaPai;
    }
    public void setTarefaPai(Tarefa tarefaPai){
        this.tarefaPai = tarefaPai;
    }
    public List<Tarefa> getTarefasFilhas(){
        return tarefasFilhas;
    }
    public void setTarefasFilhas(List<Tarefa> tarefasFilhas){
        this.tarefasFilhas = tarefasFilhas;
    }





}