package br.edu.ifspsaocarlos.sdm.ifspmessenger.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifspsaocarlos.sdm.ifspmessenger.R;
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Usuario;

/**
 * Created by Romancini on 27/08/2017.
 */

public class ListaContatoAdapter extends ArrayAdapter<Usuario> {
    private LayoutInflater layoutInflater;

    public ListaContatoAdapter(@NonNull Activity context, List<Usuario> resource) {
        super(context, R.layout.contato, resource);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.contato, null);
        }
        Usuario contact = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.textView_nome_completo);
        textView.setText(contact.getNome_completo());

        return convertView;
    }
}
