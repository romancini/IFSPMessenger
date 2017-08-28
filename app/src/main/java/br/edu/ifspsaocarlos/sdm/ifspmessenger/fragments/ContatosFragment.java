package br.edu.ifspsaocarlos.sdm.ifspmessenger.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.PrincipalActivity;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.dao.UsuarioDao;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.utils.ListaContatoAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listViewContatos;
    private List<Usuario> contatos;
    private UsuarioDao usuario;
    private ListaContatoAdapter listaContatoAdapter;
    private FragmentTransaction ft;

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contatos, null);

        ((PrincipalActivity)getActivity())
                .getSupportActionBar()
                .setSubtitle(getString(R.string.contatos_fragment));

        listViewContatos = (ListView)view.findViewById(R.id.listView_contatos);
        usuario = new UsuarioDao(getActivity());
        contatos = usuario.obterContatos();
        listaContatoAdapter = new ListaContatoAdapter(getActivity(), contatos);
        listViewContatos.setAdapter(listaContatoAdapter);
        listViewContatos.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuario contato = listaContatoAdapter.getItem(position);
        salvarUsuarioComoContato(contato.getId());
    }

    private void salvarUsuarioComoContato(int id) {
        AsyncTask<Integer, Void, Void> tarefa = new AsyncTask<Integer, Void, Void>() {
            private ListaConversasFragment listaConversasFragment;
            UsuarioDao usuario;

            @Override
            protected Void doInBackground(Integer... params) {
                int id = params[0];
                usuario = new UsuarioDao(getActivity());
                usuario.setarUsuarioComoContato(id);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listaConversasFragment = new ListaConversasFragment();
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, listaConversasFragment);
                ft.commit();
            }
        };
        tarefa.execute(id);
    }
}
