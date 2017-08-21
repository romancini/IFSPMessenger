package br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.PrincipalActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;


/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarUsuarioFragment extends Fragment {
    private SQLiteDatabase db;
    private DatabaseHelper helper;
    private String nome, apelido;

    public CadastrarUsuarioFragment() {
        // Required empty public constructor
    }

    public static CadastrarUsuarioFragment newInstance() {
        CadastrarUsuarioFragment fragment = new CadastrarUsuarioFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cadastrar_usuario, null);

        ((PrincipalActivity)getActivity())
                .getSupportActionBar()
                .setSubtitle(getString(R.string.cadastro_fragment));

        Button cadastroButton = (Button)view.findViewById(R.id.btn_cadastro_usuario);
        cadastroButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nome = ((EditText) view.findViewById(R.id.et_nome)).getText().toString();
                apelido = ((EditText) view.findViewById(R.id.et_usuario)).getText().toString();
                Usuario u = new Usuario(nome, apelido);
                cadastrarUsuario(u);
            }
        });

        return view;
    }

    private void cadastrarUsuario(Usuario usuario) {
        AsyncTask<Usuario, Void, Usuario> tarefa = new AsyncTask<Usuario, Void, Usuario>() {
            private Usuario usuarioAT;

            @Override
            protected Usuario doInBackground(Usuario... params) {
                usuarioAT = params[0];
                // request para a api enviando dados para cadastro
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = getString(R.string.URL_BASE) + "/usuario";
                String string = params[0].cadastroString();
                try {
                    final JSONObject jsonBody = new JSONObject(string);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    try {
                                        usuarioAT.setId(s.getInt("id"));
                                    } catch (JSONException e) {
                                        Log.e("IFSPMsg", "Erro ao obter id do Usuário");
                                    }
                                    Toast.makeText(getActivity(), "Usuário Cadastrado!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getActivity(),
                                            "Erro no request no cadastro do Usuário!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    queue.add(jsonObjectRequest);
                }catch (Exception e ) {
                    Log.e("IFSPMsg", "Erro geral no cadastro do Usuário");
                }

                return usuarioAT;
            }

            @Override
            protected void onPostExecute(Usuario usuario) {
                super.onPostExecute(usuario);
                // cadastro na base local
                try {
                    helper = new DatabaseHelper(getActivity());
                    db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("id", usuario.getId());
                    values.put("nome_completo", usuario.getNome_completo());
                    values.put("apelido", usuario.getApelido());
                    db.insert("usuarios", null, values);
                    Toast.makeText(getActivity(), "Cadastro realizado com sucesso",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                    System.out.println("erro: " + e);
                }
            }
        };
        tarefa.execute(usuario);
    }

}
