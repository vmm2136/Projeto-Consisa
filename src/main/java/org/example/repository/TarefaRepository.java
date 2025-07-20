package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.StatusTarefa;
import org.example.model.Tarefa;
import java.util.List;
import java.util.UUID;

@Stateless
public class TarefaRepository {

    @PersistenceContext(unitName = "ProjetoConsisaPU")
    private EntityManager em;

    public Tarefa criar(Tarefa tarefa){
        em.persist(tarefa);
        return tarefa;
    }

    public List<Tarefa> buscarTarefasPais(){
        return em.createQuery("SELECT t FROM Tarefa t WHERE t.tarefaPai IS NULL", Tarefa.class)
                .getResultList();
    }
}
