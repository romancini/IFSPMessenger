package br.edu.ifspsaocarlos.sdm.ifspmessenger.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
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

import java.util.List;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.ConversaActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.MensagemDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.UsuarioDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;

public class MensagensService extends Service implements Runnable{
    private boolean appAberta;
    private boolean primeiraBusca;
    private static int ultimaMensagem;
    private static int ultimaMensagemLocal;
    private Usuario contatoUltimaMensagem;
    private MensagemDao mensagemDao;
    private UsuarioDao usuarioDao;

    public MensagensService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        appAberta = true;
        primeiraBusca = true;
        ultimaMensagem = 1;
        ultimaMensagemLocal = 1;
        new Thread(this).start();
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    public void run() {
        while (appAberta) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                buscaNovaMensagem();
                UsuarioDao usuarioDao = new UsuarioDao(getBaseContext());
                if (!primeiraBusca && ultimaMensagem != ultimaMensagemLocal) {
                    NotificationManager nm = (NotificationManager)
                            getSystemService(NOTIFICATION_SERVICE);
                    Intent intent = new Intent(this, ConversaActivity.class);
                    contatoUltimaMensagem = mensagemDao.obterContatoUltimaMensagem(
                            contatoUltimaMensagem,
                            ultimaMensagem);
                    intent.putExtra("id_contato", contatoUltimaMensagem.getId());
                    PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setSmallIcon(R.drawable.ic_contato);
                    builder.setTicker(getString(R.string.nova_mensagem_de) +
                                    contatoUltimaMensagem.getApelido());
                    builder.setContentTitle(getString(R.string.nova_mensagem));
                    builder.setContentText(getString(R.string.clique_aqui));
                    builder.setWhen(System.currentTimeMillis());
                    builder.setContentIntent(p);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.logo));
                    Notification notification = builder.build();
                    notification.vibrate = new long[] {100, 250};
                    nm.notify(R.mipmap.ic_launcher, notification);
                }
                ultimaMensagemLocal = ultimaMensagem;
                primeiraBusca = false;
            }
            catch (InterruptedException ie) {
                Log.e("SDM", "Erro na thread de recuperação de contato");
            }
        }
    }
    private void buscaNovaMensagem() {
        RequestQueue queue = Volley.newRequestQueue(MensagensService.this);
        try {
            JsonObjectRequest jsonObjectRequest;
            String url;
            Usuario contato;
            // procurar nos contatos adicionados (contato = 1) se
            // há mensagem com id maior que a ultima mensagem
            List<Usuario> contatos = usuarioDao.obterContatosConversasAtivas();
            for (int i = 0; i < contatos.size(); i++){
                url = R.string.URL_BASE + "mensagem/" +
                        usuarioDao.obterIdUsuarioLogado() + "/" +
                        contatos.get(i).getId() + "/" +
                        ultimaMensagem;
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject s) {
                            JSONArray jsonArray;
                            try {
                                jsonArray = s.getJSONArray("mensagens");
                                if (jsonArray.length() > 1) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jO = jsonArray.getJSONObject(i);
                                        ultimaMensagem = jO.getInt("id");
                                        mensagemDao.cadastrarDB(
                                                jO.getInt("id"),
                                                jO.getInt("origem_id"),
                                                jO.getInt("destino_id"),
                                                jO.getString("assunto"),
                                                jO.getString("corpo"));
                                    }
                                }
                            }
                            catch (JSONException je) {
                                Toast.makeText(MensagensService.this,
                                        "Erro na conversão de objeto JSON da nova mensagem!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MensagensService.this,
                            "Erro na recuperação da nova mensagem!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObjectRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        appAberta = false;
        stopSelf();
    }
}
