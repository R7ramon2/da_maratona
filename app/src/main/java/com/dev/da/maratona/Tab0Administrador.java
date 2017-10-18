package com.dev.da.maratona;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
 * Created by Tiago Emerenciano on 14/10/2017.
 */

public class Tab0Administrador extends Fragment {

    protected EditText matricula;
    protected Button addPontuacao;
    protected Button remPontuacao;
    protected Button addFaltas;
    protected Button remFaltas;
    protected Button addPeriodo;
    protected EditText quantidade;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private ImageButton search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab0admin, container, false);

        findViewById(rootView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        matricula.addTextChangedListener(EditTextMask.mask(matricula, EditTextMask.MATRICULA));

        addPontuacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mat = matricula.getText().toString();
                final String pontos = quantidade.getText().toString();
                final int pontosInt;
                if (mat.equals("")) {
                    Toast.makeText(getContext(), "Matrícula não informada.", Toast.LENGTH_SHORT).show();
                } else if (pontos.equals("")) {
                    Toast.makeText(getContext(), "Pontuação não digitada.", Toast.LENGTH_SHORT).show();
                } else {
                    pontosInt = Integer.parseInt(pontos);
                    adicionarPontos(mat, pontosInt);
                }
            }
        });

        remPontuacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mat = matricula.getText().toString();
                final String pontos = quantidade.getText().toString();
                final int pontosInt;
                if (mat.equals("")) {
                    Toast.makeText(getContext(), "Matrícula não informada.", Toast.LENGTH_SHORT).show();
                } else if (pontos.equals("")) {
                    Toast.makeText(getContext(), "Pontuação não digitada.", Toast.LENGTH_SHORT).show();
                } else {
                    pontosInt = Integer.parseInt(pontos);
                    removerPontos(mat, pontosInt);
                }
            }
        });

        addFaltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mat = matricula.getText().toString();
                if (mat.equals("")) {
                    Toast.makeText(getContext(), "Matrícula não informada.", Toast.LENGTH_SHORT).show();
                } else {
                    adicionarFaltas(mat);
                }
            }
        });

        remFaltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mat = matricula.getText().toString();
                if (mat.equals("")) {
                    Toast.makeText(getContext(), "Matrícula não informada.", Toast.LENGTH_SHORT).show();
                } else {
                    removerFaltas(mat);
                }
            }
        });
        return rootView;
    }

    public void adicionarPontos(final String matricula, final int pontos) {
        firebase.child("Alunos/" + matricula + "/pontuacao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long qtdAtual = (long) dataSnapshot.getValue();
                firebase.child("Alunos/" + matricula + "/pontuacao").setValue(qtdAtual + pontos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removerPontos(final String matricula, final int pontos) {
        firebase.child("Alunos/" + matricula + "/pontuacao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long qtdAtual = (long) dataSnapshot.getValue();
                if (qtdAtual >= pontos) {
                    firebase.child("Alunos/" + matricula + "/pontuacao").setValue(qtdAtual - pontos);
                } else {
                    Toast.makeText(getContext(), "Aluno com menor quantidade de pontos que o solicitado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void adicionarFaltas(final String matricula) {
        firebase.child("Alunos/" + matricula + "/faltas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long qtdAtual = (long) dataSnapshot.getValue();
                firebase.child("Alunos/" + matricula + "/faltas").setValue(qtdAtual + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removerFaltas(final String matricula) {
        firebase.child("Alunos/" + matricula + "/faltas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long qtdAtual = (long) dataSnapshot.getValue();
                if (qtdAtual > 0) {
                    firebase.child("Alunos/" + matricula + "/faltas").setValue(qtdAtual - 1);
                } else {
                    Toast.makeText(getContext(), "Aluno possui 0 faltas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void adicionarPeriodo() {
        //TODO: implementar "adicionar período".
    }

    public void findViewById(View rootView) {
        matricula = rootView.findViewById(R.id.matricula_input);
        addPontuacao = rootView.findViewById(R.id.btn_addPontuacao);
        remPontuacao = rootView.findViewById(R.id.btn_remPontuacao);
        addFaltas = rootView.findViewById(R.id.btn_addFaltas);
        remFaltas = rootView.findViewById(R.id.btn_remFaltas);
        addPeriodo = rootView.findViewById(R.id.btn_addPeriodo);
        quantidade = rootView.findViewById(R.id.qtd_input);
        search = rootView.findViewById(R.id.btn_search);
    }
}
