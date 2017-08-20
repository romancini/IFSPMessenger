package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT count(id) FROM usuarios", null);
        cursor.moveToFirst();
        int totalUsuarios = cursor.getInt(0);
        cursor.close();
        TextView tv = (TextView) findViewById(R.id.helloTextView);
        tv.setText("" + totalUsuarios);
    }
}
