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

    public List<Tarefa> buscarTarefasFilhas(UUID tarefaPaiId){
        List<Tarefa> filhas = em.createQuery("SELECT t FROM Tarefa t WHERE t.tarefaPai.id = :tarefaPaiId", Tarefa.class)
                .setParameter("tarefaPaiId", tarefaPaiId)
                .getResultList();

        for (Tarefa filha : filhas) {
            if (filha.getUsuarioResponsavel() != null) {
                filha.getUsuarioResponsavel().getNome();
            }
            if (filha.getTarefasFilhas() != null) {
                filha.getTarefasFilhas().size();

            }
            if (filha.getTarefaPai() != null) {
                filha.getTarefaPai().getNomeTarefa();
            }
        }
        return filhas;
    }

    public Tarefa atualizar(Tarefa tarefa) {
        return em.merge(tarefa);
    }

    public void delete(UUID id) {
        Tarefa tarefaParaRemover = em.find(Tarefa.class, id);

        if (tarefaParaRemover != null) {
            em.remove(tarefaParaRemover);
        }
    }

    public Tarefa buscarPorId(UUID id) {
        return em.find(Tarefa.class, id);
    }
}
