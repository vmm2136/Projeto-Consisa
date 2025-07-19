package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String nome;

//
//    @OneToMany(mappedBy = "usuarioResponsavel", cascade = CascadeType.ALL, fetch = FetchType.LAZY);
//    private List<Tarefa> tarefas;

    public Usuario(){
        this.id = UUID.randomUUID();
    }

    public Usuario(String nome){
        this();
        this.nome = nome;
    }

    public UUID getId(){
        return  id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

//    public List<Tarefa> getTarefas(){
//        return tarefas;
//    }
//
//    public void setTarefas(List<Tarefa> tarefas){
//        this.tarefas = tarefas;
//    }
}
