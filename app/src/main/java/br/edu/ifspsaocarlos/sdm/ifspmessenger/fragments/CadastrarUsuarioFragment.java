package br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments;


import android.content.ContentValues;
import android.content.Intent;
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

import br.edu.ifspsaocarlos.sdm.ifspmessenger.LoginActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.UsuarioDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.PrincipalActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;


/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarUsuarioFragment extends Fragment {
    String nome, apelido;

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

        ((LoginActivity)getActivity())
                .getSupportActionBar()
                .setSubtitle(getString(R.string.cadastro_fragment));

        Button cadastroButton = (Button)view.findViewById(R.id.btn_cadastro_usuario);
        cadastroButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nome = ((EditText) view.findViewById(R.id.et_nome)).getText().toString();
                apelido = ((EditText) view.findViewById(R.id.et_usuario)).getText().toString();
                cadastrarUsuario(nome, apelido);
            }
        });

        return view;
    }

    private void cadastrarUsuario(String nome, String apelido) {
        AsyncTask<String, Void, Usuario> tarefa = new AsyncTask<String, Void, Usuario>() {
            UsuarioDao usuarioDao;
            private Integer id;
            private String nome, apelido;

            @Override
            protected Usuario doInBackground(final String... params) {
                nome = params[0];
                apelido = params[1];
                usuarioDao = new UsuarioDao(getActivity());
                // request para a api enviando dados para cadastro
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = getString(R.string.URL_BASE) + "contato";
                try {
                    final JSONObject jsonBody =
                            new JSONObject(usuarioDao.obterStringCadastro(nome, apelido));
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    try {
                                        id = s.getInt("id");
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

                int count = 0;
                while (id == null){
                    count++;
                    if (count > 9){
                        Toast.makeText(getActivity(),
                                "Tentativas esgotadas de conexão com o servidor!",
                                Toast.LENGTH_SHORT).show();
                        throw new RuntimeException();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.e("IFSPMsg", "Erro ao esperar");
                    }
                }

                return new Usuario(id, nome, apelido);
            }

            @Override
            protected void onPostExecute(Usuario usuarioBean) {
                super.onPostExecute(usuarioBean);
                try {
                    usuarioDao.cadastrarDB(
                            usuarioBean.getId(),
                            usuarioBean.getNome_completo(),
                            usuarioBean.getApelido());
                    usuarioDao.setarUsuarioComoLogado(apelido);
                    Toast.makeText(getActivity(), "Cadastro realizado com sucesso",
                            Toast.LENGTH_SHORT).show();
                    Intent principal = new Intent(getActivity(), PrincipalActivity.class);
                    startActivity(principal);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Erro ao cadastrar no banco o usuário",
                            Toast.LENGTH_SHORT).show();
                    Log.e("IFSPMsg", ": Erro ao cadastrar no banco o usuário: " + e);
                }
            }
        };
        tarefa.execute(nome, apelido);
    }

}
