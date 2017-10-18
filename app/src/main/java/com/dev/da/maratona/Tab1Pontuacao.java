package com.dev.da.maratona;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab1Pontuacao extends Fragment {

    private Aluno aluno;
    private TextView teste;

    public Tab1Pontuacao(Aluno aluno) {
        this.aluno = aluno;
    }
    public Tab1Pontuacao(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1pontuacao, container, false);

         //Demonstração de como se deve pegar os dados da activity do fragment (Aba do menu) --> Inicio
        String nome = aluno.getNome();
        teste = rootView.findViewById(R.id.texto);
        teste.setText(
                "Nome: " + nome + "\n" +
                        "Matricula: " + aluno.getMatricula() + "\n" +
                        "Pontuação: " + aluno.getPontuacao() + "\n" +
                        "Periodo: " + aluno.getPeriodo() + "\n" +
                        "Nº de faltas: " + aluno.getFaltas()
        );
        return rootView;
    }
}
