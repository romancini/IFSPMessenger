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
import br.edu.ifspsaocarlos.sdm.ifspmessenger.models.Mensagem;

public class ListaMensagemAdapter extends ArrayAdapter<Mensagem> {
    private LayoutInflater layoutInflater;

    public ListaMensagemAdapter(@NonNull Activity context, List<Mensagem> resource) {
        super(context, R.layout.conversa_bolha, resource);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.conversa_bolha, null);
        }
        Mensagem mensagem = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.message_text);
        textView.setText(mensagem.getCorpo());

        return convertView;
    }
}