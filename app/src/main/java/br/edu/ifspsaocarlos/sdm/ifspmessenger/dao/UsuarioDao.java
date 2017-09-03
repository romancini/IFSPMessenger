package br.edu.ifspsaocarlos.sdm.ifspmessenger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;

/**
 * Created by Romancini on 22/08/2017.
 */

public class UsuarioDao {
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private ContentValues values;

    public UsuarioDao(Context context){
        this.helper = new DatabaseHelper(context);
    }

    public String obterStringCadastro(String nome_completo, String apelido){
        String retorno = "{\"nome_completo\":\"" + nome_completo +
                "\",\"apelido\":\"" + apelido + "\"}";
        return retorno;
    }

    public void cadastrarDB(int id, String nome_completo, String apelido){
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(id) FROM usuarios where id = " + id, null);
        cursor.moveToFirst();
        int usuarioExistente = cursor.getInt(0);
        if (usuarioExistente == 0) {
            values = new ContentValues();
            values.put("id", id);
            values.put("nome_completo", nome_completo);
            values.put("apelido", apelido);
            values.put("logado", 0);
            values.put("contato", 0);
            db.insert("usuarios", null, values);
        }
        db.close();
    }

    public int obterNumeroUsuarios(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(id) FROM usuarios", null);
        cursor.moveToFirst();
        int totalUsuarios = cursor.getInt(0);
        cursor.close();
        db.close();

        return totalUsuarios;
    }

    public boolean verificarUsuarioJaLogado(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(id) FROM usuarios where logado = 1", null);
        cursor.moveToFirst();
        int usuarioLogado = cursor.getInt(0);
        cursor.close();
        db.close();
        if (usuarioLogado == 1)
            return true;
        else
            return false;
    }

    public int obterIdUsuarioLogado(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM usuarios where logado = 1", null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        db.close();

        return id;
    }

    public boolean validarUsuarioExistente(String apelido){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(1) FROM usuarios where apelido = '"
                + apelido + "'", null);
        cursor.moveToFirst();
        int retorno = cursor.getInt(0);
        cursor.close();
        db.close();

        if (retorno == 1)
            return true;
        else
            return false;
    }

    public void setarUsuarioComoLogado(String apelido){
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, nome_completo FROM usuarios where apelido = '"
                + apelido + "'", null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String nome = cursor.getString(1);
        cursor.close();
        values = new ContentValues();
        values.put("id", id);
        values.put("nome_completo", nome);
        values.put("apelido", apelido);
        values.put("logado", 1);
        values.put("contato", 0);
        db.update("usuarios", values, "id=" + id, null);
        db.close();
    }

    public List<Usuario> obterContatos() {
        List<Usuario> contatos = new ArrayList<>();
        Usuario contato;
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id, nome_completo, apelido from usuarios " +
                " where logado = 0 and contato = 0", null);
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    contato = new Usuario(
                            c.getInt(0),
                            c.getString(1),
                            c.getString(2));
                    contatos.add(contato);
                }while (c.moveToNext());
            }
        }
        c.close();
        db.close();

        return contatos;
    }

    public List<Usuario> obterContatosConversasAtivas() {
        List<Usuario> contatos = new ArrayList<>();
        Usuario contato;
        db = helper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id, nome_completo, apelido from usuarios " +
                " where logado = 0 and contato = 1", null);
        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    contato = new Usuario(
                            c.getInt(0),
                            c.getString(1),
                            c.getString(2));
                    contatos.add(contato);
                }while (c.moveToNext());
            }
        }
        c.close();
        db.close();

        return contatos;
    }

    public void setarUsuarioComoContato(int id) {
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome_completo, apelido FROM usuarios where id = '"
                + id + "'", null);
        cursor.moveToFirst();
        String nome = cursor.getString(0);
        String apelido = cursor.getString(1);
        cursor.close();
        values = new ContentValues();
        values.put("id", id);
        values.put("nome_completo", nome);
        values.put("apelido", apelido);
        values.put("logado", 0);
        values.put("contato", 1);
        db.update("usuarios", values, "id=" + id, null);
        db.close();
    }
}
