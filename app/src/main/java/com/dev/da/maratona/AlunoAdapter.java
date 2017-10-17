package com.dev.da.maratona;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


import static android.R.attr.content;
import static android.R.attr.resource;

/*
 * Created by Ramon on 16/10/2017.
 */

public class AlunoAdapter extends ArrayAdapter<Aluno> {

    private Context context;
    private ArrayList<Aluno> lista;

    public AlunoAdapter(Context context, ArrayList<Aluno> lista){
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Aluno alunoPosicao = this.lista.get(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.aluno_item,null);

        TextView nome = convertView.findViewById(R.id.nome_item);
        TextView pontos = convertView.findViewById(R.id.pontos_item);
        TextView pos = convertView.findViewById(R.id.posicao_item);

        nome.setText(alunoPosicao.getPrimeiroNome()+" "+alunoPosicao.getUltimoNome());
        pontos.setText(String.valueOf(alunoPosicao.getPontuacao()));
        pos.setText(String.valueOf(position+1)+". ");


        return convertView;
    }
}
