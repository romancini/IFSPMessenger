package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nome_completo FROM usuarios where logado = 1", null);
        cursor.moveToFirst();
        String nomes = "";
        for (int i = 0; i < cursor.getCount(); i++) {
            nomes = nomes + cursor.getString(0) + " - ";
            cursor.moveToNext();
        }
        cursor.close();
        TextView tv = (TextView) findViewById(R.id.helloTextView);
        tv.setText(nomes);
    }
}
