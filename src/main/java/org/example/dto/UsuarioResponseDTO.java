package org.example.dto;

import org.example.model.Usuario;
import java.util.UUID;

public class UsuarioResponseDTO {
    private UUID idUsuarioResponsavel;
    private String nome;

    public UsuarioResponseDTO() {}

    public UsuarioResponseDTO(Usuario usuario) {
        this.idUsuarioResponsavel = usuario.getId();
        this.nome = usuario.getNome();
    }

    public UUID getIdUsuarioResponsavel() { return idUsuarioResponsavel; }
    public void setIdUsuarioResponsavel(UUID idUsuarioResponsavel) { this.idUsuarioResponsavel = idUsuarioResponsavel; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
