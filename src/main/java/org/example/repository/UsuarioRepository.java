package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.Usuario;
import java.util.UUID;

@Stateless
public class UsuarioRepository {

    @PersistenceContext(name = "ProjetoConsisaPU")
    private EntityManager em;

    public Usuario criar(Usuario usuario){

        em.persist(usuario);
        return usuario;
    }
}
