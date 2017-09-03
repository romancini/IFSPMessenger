package br.edu.ifspsaocarlos.sdm.ifspmessenger.models;

/**
 * Created by Romancini on 02/09/2017.
 */

public class Mensagem {
    private int id;
    private int origem_id;
    private int destino_id;
    private String assunto;
    private String corpo;

    public Mensagem(int id, int origem_id, int destino_id, String assunto, String corpo) {
        this.id = id;
        this.origem_id = origem_id;
        this.destino_id = destino_id;
        this.assunto = assunto;
        this.corpo = corpo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrigem_id() {
        return origem_id;
    }

    public void setOrigem_id(int origem_id) {
        this.origem_id = origem_id;
    }

    public int getDestino_id() {
        return destino_id;
    }

    public void setDestino_id(int destino_id) {
        this.destino_id = destino_id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }
}
