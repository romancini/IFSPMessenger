package br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.LoginActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.PrincipalActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.UsuarioDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntrarFragment extends Fragment {
    private EditText etApelido;
    private Button btnEntrar;
    private Button btnCadastrar;
    private UsuarioDao usuario;
    FragmentTransaction fragmentTransaction;

    public EntrarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_entrar, null);
        usuario = new UsuarioDao(getActivity());

        ((LoginActivity)getActivity())
                .getSupportActionBar()
                .setSubtitle(getString(R.string.login));

        etApelido = (EditText)view.findViewById(R.id.apelidoUsuarioEditText);
        btnEntrar = (Button)view.findViewById(R.id.entrarButton);
        btnEntrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (usuario.validarUsuarioExistente(etApelido.getText().toString())) {
                    usuario.setarUsuarioComoLogado(etApelido.getText().toString());
                    Toast.makeText(getActivity(), R.string.smile_sorriso,
                            Toast.LENGTH_LONG).show();
                    Intent principal = new Intent(getActivity(), PrincipalActivity.class);
                    startActivity(principal);
                }
                else
                    Toast.makeText(getActivity(), "Usuário não encontrado",
                            Toast.LENGTH_LONG).show();
            }
        });

        btnCadastrar = (Button)view.findViewById(R.id.cadastrarButton);
        btnCadastrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CadastrarUsuarioFragment cadastroFragment = new CadastrarUsuarioFragment();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, cadastroFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
