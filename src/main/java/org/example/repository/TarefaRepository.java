package org.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.model.Tarefa;
import org.example.model.Usuario;

import java.util.List;
import java.util.UUID;

@ApplicationScoped

    public class TarefaRepository {
        @PersistenceContext
    private EntityManager em;

    @Transactional
    public Tarefa criar(Tarefa tarefa) {
        if (tarefa.getTarefaPai() != null && tarefa.getTarefaPai().getId() != null) {
            Tarefa tarefaPaiGerenciada = em.find(Tarefa.class, tarefa.getTarefaPai().getId());
            if (tarefaPaiGerenciada == null) {
                throw new IllegalArgumentException("Tarefa pai não encontrada.");
            }
            tarefa.setTarefaPai(tarefaPaiGerenciada);
        }
        if (tarefa.getUsuarioResponsavel() != null && tarefa.getUsuarioResponsavel().getId() != null) {
            Usuario usuarioGerenciado = em.find(Usuario.class, tarefa.getUsuarioResponsavel().getId());
            if (usuarioGerenciado == null) {
                throw new IllegalArgumentException("Usuário responsável não encontrado.");
            }
            tarefa.setUsuarioResponsavel(usuarioGerenciado);
        }

        em.persist(tarefa);
        em.flush();

        if (tarefa.getUsuarioResponsavel() != null) {
            tarefa.getUsuarioResponsavel().getNome();
        }

        if (tarefa.getTarefaPai() != null) {
            tarefa.getTarefaPai().getNomeTarefa();
        }
        return tarefa;
    }

    public Tarefa buscarPorId(UUID id) {
        try {
            return em.createQuery(
                            "SELECT t FROM Tarefa t " +
                                    "LEFT JOIN FETCH t.usuarioResponsavel ur " +
                                    "LEFT JOIN FETCH t.tarefaPai tp " +
                                    "LEFT JOIN FETCH t.tarefasFilhas tf " +
                                    "WHERE t.id = :id", Tarefa.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Tarefa atualizar(Tarefa tarefa) {
        Tarefa mergedTarefa = em.merge(tarefa);
        em.flush();

        if (mergedTarefa.getUsuarioResponsavel() != null) {
            mergedTarefa.getUsuarioResponsavel().getNome();
        }
        if (mergedTarefa.getTarefaPai() != null) {
            mergedTarefa.getTarefaPai().getNomeTarefa();
        }
        if (mergedTarefa.getDataInicio() != null) {
            mergedTarefa.getDataInicio();
        }
        if (mergedTarefa.getDataFim() != null) {
            mergedTarefa.getDataFim();
        }
        if (mergedTarefa.getTarefasFilhas() != null) {
            mergedTarefa.getTarefasFilhas().size();
            for (Tarefa filha : mergedTarefa.getTarefasFilhas()) {
                filha.getNomeTarefa();
            }
        }
        return mergedTarefa;
    }

    public List<Tarefa> buscarTarefasPais() {
        return em.createQuery(
                        "SELECT DISTINCT t FROM Tarefa t " +
                                "LEFT JOIN FETCH t.usuarioResponsavel ur " +
                                "LEFT JOIN FETCH t.tarefasFilhas tf " +
                                "WHERE t.tarefaPai IS NULL", Tarefa.class)
                .getResultList();
    }

    @Transactional
    public void deletar(UUID id) {
        Tarefa tarefa = em.find(Tarefa.class, id);
        if (tarefa != null) {
            em.remove(tarefa);
        }
    }
}