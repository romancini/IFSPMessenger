package br.edu.ifspsaocarlos.sdm.ifspmessenger.models;

/**
 * Created by Romancini on 20/08/2017.
 */

public class Usuario {
    private int id;
    private String nome_completo;
    private String apelido;

    public Usuario(int id, String nome_completo, String apelido){
        this.id = id;
        this.nome_completo = nome_completo;
        this.apelido = apelido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
}
