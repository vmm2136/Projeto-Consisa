package org.example.model;

public enum StatusTarefa {
    AGUARDANDO("Aguardando"),
    INICIADA("Iniciada"),
    ENCERRADA("Encerrada"),
    ATRASADA("Atrasada");

    private String descricao;

    StatusTarefa(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
