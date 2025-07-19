package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.Usuario;

import java.util.List;
import java.util.UUID;

@Stateless
public class UsuarioRepository {

    @PersistenceContext(name = "ProjetoConsisaPU")
    private EntityManager em;

    public Usuario criar(Usuario usuario){
        em.persist(usuario);
        return usuario;
    }

    public Usuario buscarPorId(UUID id){
        return em.find(Usuario.class, id);
    }

    public List<Usuario> listarUsuarios(){
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                .getResultList();
    }

    public Usuario atualizarUsuario(Usuario usuario){
        return em.merge(usuario);
    }

    public void delete(UUID id){
        Usuario usuario = buscarPorId((id));
        if(usuario != null){
            em.remove(usuario);
        }
    }
}
