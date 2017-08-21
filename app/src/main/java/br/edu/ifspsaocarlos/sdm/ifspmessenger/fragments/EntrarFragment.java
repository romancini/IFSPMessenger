package br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.LoginActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntrarFragment extends Fragment {


    public EntrarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entrar, container, false);

        ((LoginActivity)getActivity())
                .getSupportActionBar()
                .setSubtitle(getString(R.string.login));

        etApelido = (EditText)findViewById(R.id.apelidoUsuarioEditText);
        btnEntrar = (Button)findViewById(R.id.entrarButton);
        btnEntrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // validar usuário na base local
                helper = new DatabaseHelper(LoginActivity.this);
                db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT count(1) FROM usuarios where apelido = '"
                        + etApelido.getText() + "'", null);
                cursor.moveToFirst();
                int retorno = cursor.getInt(0);
                cursor.close();

                if (retorno == 1)
                    Toast.makeText(LoginActivity.this, R.string.smile_sorriso,
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(LoginActivity.this, "Usuário não encontrado",
                            Toast.LENGTH_LONG).show();
            }
        });

        btnCadastrar = (Button)findViewById(R.id.cadastrarButton);
        btnCadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }

}
