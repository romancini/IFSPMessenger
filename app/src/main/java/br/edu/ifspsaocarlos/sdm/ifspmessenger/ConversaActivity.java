package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.MensagemDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.UsuarioDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Mensagem;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.ListaMensagemAdapter;

public class ConversaActivity extends AppCompatActivity implements View.OnClickListener {
    private int idContatoConversa;
    private ListaMensagemAdapter listaMensagemAdapter;
    private ListView mensagensListView;
    private ImageButton mensagemButton;
    private EditText mensagemEditText;
    private ThreadMensagens threadMensagens;
    private MensagemDao mensagemDao;
    private List<Mensagem> mensagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        mensagemDao = new MensagemDao(getBaseContext());
        idContatoConversa = getIntent().getIntExtra("id_contato", 0);
        mensagens = mensagemDao.obterConversaContato(idContatoConversa);
        listaMensagemAdapter = new ListaMensagemAdapter(this, mensagens);
        mensagensListView = (ListView)findViewById(R.id.msgListView);
        mensagensListView.setAdapter(listaMensagemAdapter);
        mensagemButton = (ImageButton)findViewById(R.id.sendMessageButton);
        mensagemButton.setOnClickListener(this);
        // rodar processo para ficar lendo banco de dados de mensagens para o contato
        threadMensagens = new ThreadMensagens();
        threadMensagens.start();
    }

    @Override
    public void onClick(View v) {
        mensagemEditText = (EditText)findViewById(R.id.messageEditText);
        String corpo = mensagemEditText.getText().toString();
        mensagemEditText.setText("");
        // enviar mensagem API
        enviarMensagem("" + idContatoConversa, corpo);
    }

    public class ThreadMensagens extends Thread{
        @Override
        public void run() {
            try{
                mensagemDao = new MensagemDao(getBaseContext());
                while(true){
                    mensagens = mensagemDao.obterConversaContato(idContatoConversa);
                    sleep(500);
                }
            } catch (Exception e) {
                Log.e("IFSPMsg", "Erro na thread de mensagens da conversa");
            }
        }
    }

    private void enviarMensagem(String idContatoConversa, String corpo) {
        AsyncTask<String, Void, Mensagem> tarefa = new AsyncTask<String, Void, Mensagem>() {
            private MensagemDao mensagemDao;
            private Integer id;
            private int idContato;
            private String corpo;
            private UsuarioDao usuarioDao;

            @Override
            protected Mensagem doInBackground(final String... params) {
                idContato = Integer.parseInt(params[0]);
                corpo = params[1];
                mensagemDao = new MensagemDao(getBaseContext());
                usuarioDao = new UsuarioDao(getBaseContext());
                // request para a api enviando dados para cadastro
                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                String url = getString(R.string.URL_BASE) + "mensagem";
                try {
                    final JSONObject jsonBody =
                            new JSONObject(
                                    mensagemDao.obterStringCadastro(
                                            usuarioDao.obterIdUsuarioLogado(), idContato, corpo));
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject s) {
                                    try {
                                        id = s.getInt("id");
                                    } catch (JSONException e) {
                                        Log.e("IFSPMsg", "Erro ao obter id da mensagem");
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.e("IFSPMsg", "Erro ao enviar mensagem");
                                }
                            });
                    queue.add(jsonObjectRequest);
                }catch (Exception e ) {
                    Log.e("IFSPMsg", "Erro geral no envio da mensagem");
                }

                int count = 0;
                while (id == null){
                    count++;
                    if (count > 9){
                        Toast.makeText(getBaseContext(),
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

                return new Mensagem(
                        id, usuarioDao.obterIdUsuarioLogado(), idContato, "", corpo);
            }

            @Override
            protected void onPostExecute(Mensagem mensagem) {
                super.onPostExecute(mensagem);
                try {
                    mensagemDao.cadastrarDB(
                            mensagem.getId(),
                            mensagem.getOrigem_id(),
                            mensagem.getDestino_id(),
                            mensagem.getAssunto(),
                            mensagem.getCorpo());
                } catch (Exception e) {
                    Log.e("IFSPMsg", ": Erro ao cadastrar no banco a mensagem: " + e);
                }
            }
        };
        tarefa.execute(idContatoConversa, corpo);
    }
}
