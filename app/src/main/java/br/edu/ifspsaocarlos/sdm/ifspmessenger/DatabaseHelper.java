package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Romancini on 12/08/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String BANCO_DADOS = "IFSPMessengerDB";
    private static int VERSAO = 1;

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE usuarios (" +
                "id INTEGER," +
                "nome_completo TEXT NOT NULL," +
                "apelido TEXT NOT NULL," +
                "logado INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE mensagens (" +
                "id INTEGER," +
                "origem_id INTEGER," +
                "destino_id INTEGER," +
                "assunto TEXT, " +
                "corpo TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
