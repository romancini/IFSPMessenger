package br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.ConversaActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.PrincipalActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.UsuarioDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.ListaContatoAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaConversasFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listViewContatos;
    private List<Usuario> contatos;
    private UsuarioDao usuario;
    private ListaContatoAdapter listaContatoAdapter;
    private TextView textViewMensagem;

    public ListaConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lista_conversas, null);

        ((PrincipalActivity)getActivity())
                .getSupportActionBar()
                .setSubtitle(getString(R.string.conversas_fragment));

        listViewContatos = (ListView)view.findViewById(R.id.listView_contatos);
        usuario = new UsuarioDao(getActivity());
        contatos = usuario.obterContatosConversasAtivas();
        listaContatoAdapter = new ListaContatoAdapter(getActivity(), contatos);
        listViewContatos.setAdapter(listaContatoAdapter);
        listViewContatos.setOnItemClickListener(this);
        if (listViewContatos.getCount() > 0) {
            textViewMensagem = (TextView)view.findViewById(R.id.textViewMensagem);
            textViewMensagem.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuario usuario = contatos.get(position);
        Intent conversa = new Intent(getActivity(), ConversaActivity.class);
        conversa.putExtra("id_contato", usuario.getId());
        startActivity(conversa);
    }
}
