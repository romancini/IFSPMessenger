package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.UsuarioDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.DatabaseHelper;

public class CarregandoActivity extends Activity {
    private final int ABRIR_ACTIVITY_LOGIN = 0;
    private final int ABRIR_ACTIVITY_PRINCIPAL = 1;
    private final int TEMPO_CARREGANDO = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carregando);
        verificarPrimeiraExecucao();
    }

    private void verificarPrimeiraExecucao() {
        AsyncTask<Void, Void, Void> tarefa = new AsyncTask<Void, Void, Void>() {
            UsuarioDao usuario;

            @Override
            protected Void doInBackground(Void... params) {
                usuario = new UsuarioDao(getBaseContext());

                if (usuario.obterNumeroUsuarios() < 1)
                {
                    RequestQueue queue = Volley.newRequestQueue(CarregandoActivity.this);
                    String url = getString(R.string.URL_BASE) + "contato";
                    try {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    JSONArray jsonArray;
                                    UsuarioDao usuario = new UsuarioDao(getBaseContext());
                                    try {
                                        jsonArray = s.getJSONArray("contatos");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jO = jsonArray.getJSONObject(i);
                                            usuario.cadastrarDB(
                                                    jO.getInt("id"),
                                                    jO.getString("nome_completo"),
                                                    jO.getString("apelido"));
                                        }
                                    }
                                    catch (JSONException je) {
                                        Toast.makeText(
                                                CarregandoActivity.this,
                                                "Erro na conversão de objeto JSON!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    catch (SQLException e) {
                                        Toast.makeText(
                                                CarregandoActivity.this,
                                                "Erro ao inserir dados no banco",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(
                                            CarregandoActivity.this,
                                            "Erro na recuperação das mensagens!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        );
                        queue.add(jsonObjectRequest);
                    } catch (Exception e){
                        Log.e("IFSPMsg", "Erro na obtencao dos usuarios: " + e.toString());
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                usuario = new UsuarioDao(getBaseContext());
                Toast.makeText(CarregandoActivity.this, R.string.carregando,
                        Toast.LENGTH_LONG).show();
                Message mensagem = new Message();
                if (usuario.verificarUsuarioJaLogado())
                    mensagem.what = ABRIR_ACTIVITY_PRINCIPAL;
                else
                    mensagem.what = ABRIR_ACTIVITY_LOGIN;
                handler.sendMessageDelayed(mensagem, TEMPO_CARREGANDO);
            }
        };
        tarefa.execute();
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message m) {
            super.handleMessage(m);
            Intent intent;
            switch (m.what){
                case ABRIR_ACTIVITY_LOGIN:
                    intent = new Intent(CarregandoActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case ABRIR_ACTIVITY_PRINCIPAL:
                    intent = new Intent(CarregandoActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}