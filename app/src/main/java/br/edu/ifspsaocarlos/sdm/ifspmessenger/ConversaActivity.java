package br.edu.ifspsaocarlos.sdm.ifspmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

public class ConversaActivity extends AppCompatActivity implements View.OnClickListener {
    private int idContatoConversa;
    private ArrayAdapter<String> mensagensAdapter;
    private ListView mensagensListView;
    private ImageButton mensagemButton;
    private EditText mensagemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        idContatoConversa = getIntent().getIntExtra("id_contato", 0);
        mensagensAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mensagensListView = (ListView)findViewById(R.id.msgListView);
        mensagensListView.setAdapter(mensagensAdapter);
        mensagemButton = (ImageButton)findViewById(R.id.sendMessageButton);
        mensagemButton.setOnClickListener(this);
        // rodar processo para ficar lendo banco de dados
    }

    @Override
    public void onClick(View v) {
        mensagemEditText = (EditText)findViewById(R.id.messageEditText);
        String mensagem = mensagemEditText.getText().toString();
        mensagemEditText.setText("");
        // salvar mensagem no banco

        //fazer push da mensagem para a API
    }
}
