package br.edu.ifspsaocarlos.sdm.ifspmessenger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Mensagem;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;

/**
 * Created by Romancini on 02/09/2017.
 */

public class MensagemDao {
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private ContentValues values;

    public MensagemDao(Context context){
        this.helper = new DatabaseHelper(context);
    }

    public List<Mensagem> obterConversaContato(int idContatoConversa) {
        List<Mensagem> mensagens = new ArrayList<>();
        Mensagem mensagem;
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery(
                "SELECT id, origem_id, destino_id, assunto, corpo" +
                "from mensagens" +
                "where (origem_id = (select id from usuarios where logado = 1) " +
                "          and destino_id = " + idContatoConversa + ") " +
                "        or " +
                "      (destino_id = (select id from usuarios where logado = 1) " +
                "          and origem_id = " + idContatoConversa + ")" +
                "order by id", null);
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    mensagem = new Mensagem(
                            c.getInt(0),
                            c.getInt(1),
                            c.getInt(2),
                            c.getString(3),
                            c.getString(4));
                    mensagens.add(mensagem);
                }while (c.moveToNext());
            }
        }
        c.close();
        db.close();

        return mensagens;
    }

    public String obterStringCadastro(int idUsuarioLogado, int idContato, String corpo) {
        String retorno =  "{\"origem_id\":\"" + idUsuarioLogado + "\"," +
                "\"destino_id\":\""+ idContato + "\"," +
                "\"assunto\":" + " " + "," +
                "\"corpo\":\"" + corpo + "\"}";
        return retorno;
    }

    public void cadastrarDB(int id, int origem_id, int destino_id, String assunto, String corpo) {
        db = helper.getWritableDatabase();
        values = new ContentValues();
        values.put("id", id);
        values.put("origem_id", origem_id);
        values.put("destino_id", destino_id);
        values.put("assunto", assunto);
        values.put("corpo", corpo);
        db.insert("mensagens", null, values);
        db.close();
    }

    public Usuario obterContatoUltimaMensagem(Usuario usuario, int ultimaMensagem) {
        Usuario contato;
        contato = usuario;
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id, nome_completo, apelido from usuarios " +
                " where id = (select id_origem from mensagens where id = " + ultimaMensagem + ")",
                null);
        if (c != null ) {
            if  (c.moveToFirst()) {
                contato = new Usuario(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2));
            }
        }
        c.close();
        db.close();

        return contato;
    }
}
