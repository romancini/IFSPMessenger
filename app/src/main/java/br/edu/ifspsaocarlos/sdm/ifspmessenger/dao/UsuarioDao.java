package br.edu.ifspsaocarlos.sdm.ifspmessenger.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        values = new ContentValues();
        values.put("id", id);
        values.put("nome_completo", nome_completo);
        values.put("apelido", apelido);
        values.put("logado", 0);
        db.insert("usuarios", null, values);
    }

    public int obterNumeroUsuarios(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(id) FROM usuarios", null);
        cursor.moveToFirst();
        int totalUsuarios = cursor.getInt(0);
        cursor.close();

        return totalUsuarios;
    }

    public boolean verificarUsuarioJaLogado(){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(id) FROM usuarios where logado = 1", null);
        cursor.moveToFirst();
        int usuarioLogado = cursor.getInt(0);
        cursor.close();
        if (usuarioLogado == 1)
            return true;
        else
            return false;
    }

    public boolean validarUsuarioExistente(String apelido){
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(1) FROM usuarios where apelido = '"
                + apelido + "'", null);
        cursor.moveToFirst();
        int retorno = cursor.getInt(0);
        cursor.close();

        if (retorno == 1)
            return true;
        else
            return false;
    }

    public void setarUsuarioComoLogado(String apelido){
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, nome_completo, logado FROM usuarios where apelido = '"
                + apelido + "'", null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        String nome = cursor.getString(1);
        int logado = cursor.getInt(2);
        cursor.close();
        values = new ContentValues();
        values.put("id", id);
        values.put("nome_completo", nome);
        values.put("apelido", apelido);
        values.put("logado", 1);
        db.update("usuarios", values, "id=" + id, null);
    }
}
