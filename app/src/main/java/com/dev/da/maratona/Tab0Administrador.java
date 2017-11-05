package com.dev.da.maratona;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dev.da.maratona.LoginActivity.alunoLogado;
import static com.dev.da.maratona.SearchActivity.alunoEncontrado;

/*
 * Created by Tiago Emerenciano on 14/10/2017.
 */

public class Tab0Administrador extends Fragment {

    private EditText matricula;
    private Button addPontuacao;
    private Button remPontuacao;
    private Button addFaltas;
    private Button remFaltas;
    private Button addPeriodo;
    private EditText quantidade;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private ImageButton search;
    private AlertDialog alert;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab0admin, container, false);

        findViewById(rootView);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                matricula.setText("");
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

        addPeriodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert("Confirmação", "Você deseja aumentar um período de cada aluno?", "Sim", "Não");
            }
        });


        return rootView;
    }

    // Adiciona N pontos à um aluno definido.
    private void adicionarPontos(final String matricula, final int pontos) {
        if (pontos != 0) {
            final DatabaseReference ref = firebase.child("Alunos/" + matricula + "/pontuacao");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long qtdAtual = (long) dataSnapshot.getValue();
                    firebase.child("Alunos/" + matricula + "/pontuacao").setValue(qtdAtual + pontos);
                    Toast.makeText(getContext(), "Pontuação adicionada com sucesso.", Toast.LENGTH_SHORT).show();
                    quantidade.setText("");
                    gerarLog("AdicionarPontos", alunoLogado.getPrimeiroNome() + "_" + alunoLogado.getUltimoNome(), getDateTime(),
                            "Adicionou " + pontos + " pontos ao usuário", matricula);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getContext(), "Digite uma pontuação.", Toast.LENGTH_SHORT).show();
        }
    }

    // Remove N pontos de um aluno definido.
    private void removerPontos(final String matricula, final int pontos) {
        firebase.child("Alunos/" + matricula + "/pontuacao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long qtdAtual = (long) dataSnapshot.getValue();
                if (qtdAtual >= pontos) {
                    firebase.child("Alunos/" + matricula + "/pontuacao").setValue(qtdAtual - pontos);
                    Toast.makeText(getContext(), "Pontuação removida com sucesso.", Toast.LENGTH_SHORT).show();
                    quantidade.setText("");
                    gerarLog("RemoverPontos", alunoLogado.getPrimeiroNome() + "_" + alunoLogado.getUltimoNome(), getDateTime(),
                            "Removeu " + pontos + " pontos ao usuário", matricula);
                } else {
                    Toast.makeText(getContext(), "Aluno com menor quantidade de pontos que o solicitado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Adiciona uma falta para o aluno definido.
    private void adicionarFaltas(final String matricula) {
        firebase.child("Alunos/" + matricula + "/faltas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long qtdAtual = (long) dataSnapshot.getValue();
                firebase.child("Alunos/" + matricula + "/faltas").setValue(qtdAtual + 1);
                Toast.makeText(getContext(), "Falta adicionada com sucesso.", Toast.LENGTH_SHORT).show();
                gerarLog("AdicionarFaltas", alunoLogado.getPrimeiroNome() + "_" + alunoLogado.getUltimoNome(), getDateTime(),
                        "Adicionou uma falta ao usuário", matricula);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Remove uma falta para o aluno definido.
    private void removerFaltas(final String matricula) {
        firebase.child("Alunos/" + matricula + "/faltas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long qtdAtual = (long) dataSnapshot.getValue();
                if (qtdAtual > 0) {
                    firebase.child("Alunos/" + matricula + "/faltas").setValue(qtdAtual - 1);
                    gerarLog("RemoverFaltas", alunoLogado.getPrimeiroNome() + "_" + alunoLogado.getUltimoNome(), getDateTime(),
                            "Removeu uma falta ao usuário ", matricula);
                    Toast.makeText(getContext(), "Falta removida com sucesso.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Aluno possui 0 faltas.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Adiciona um período para todos os alunos.
    private void adicionarPeriodo() {
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    long periodoAnt = (long) ds.child("periodo").getValue();
                    ds.child("periodo").getRef().setValue(periodoAnt + 1);
                }
                gerarLog("AdicionarPeriodo", alunoLogado.getPrimeiroNome() + "_" + alunoLogado.getUltimoNome(), getDateTime(),
                        "Incrementou um período para todos os alunos ", null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        firebase.child("Alunos").addListenerForSingleValueEvent(eventListener);
        Toast.makeText(getContext(), "Período adicionado para todos os alunos.", Toast.LENGTH_SHORT).show();
    }

    // Inicia todas as variáveis
    private void findViewById(View rootView) {
        matricula = rootView.findViewById(R.id.matricula_input);
        addPontuacao = rootView.findViewById(R.id.btn_addPontuacao);
        remPontuacao = rootView.findViewById(R.id.btn_remPontuacao);
        addFaltas = rootView.findViewById(R.id.btn_addFaltas);
        remFaltas = rootView.findViewById(R.id.btn_remFaltas);
        addPeriodo = rootView.findViewById(R.id.btn_addPeriodo);
        quantidade = rootView.findViewById(R.id.qtd_input);
        search = rootView.findViewById(R.id.btn_search);
    }

    // AlertDialog para confirmação sobre add período.
    private void alert(String titulo, String mensagem, String positivo, String negativo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        builder.setNegativeButton(negativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setPositiveButton(positivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adicionarPeriodo();
            }
        });

        alert = builder.create();
        alert.show();
    }

    private String getDateTime() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        return sdt;
    }

    void gerarLog(final String tipo, final String path, final String data, final String msg, String matricula) {
        if (tipo != "AdicionarPeriodo") {
            if (alunoEncontrado == null) {
                firebase.child("Alunos").child(matricula).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        alunoEncontrado = dataSnapshot.getValue(Aluno.class);
                        String new_msg = msg + " " + alunoEncontrado.getPrimeiroNome() + " " + alunoEncontrado.getUltimoNome();
                        firebase.child("log").child(tipo).child(path).child(data).setValue(new_msg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
        else {
            firebase.child("log").child(tipo).child(path).child(data).setValue(msg);
        }

    }

    // onResume sobreposto para cada vez que o fragment for aberto, verificar se há uma matrícula a set settada no campo "matrícula".
    @Override
    public void onResume() {
        super.onResume();
        if (alunoEncontrado != null) {
            matricula.setText(alunoEncontrado.getMatricula());
        }
    }

    // onPause sobreposto para cada vez que sair do fragment, colocar a variável alunoEncontrado como nula para não carregar no campo de matrícula.
    @Override
    public void onPause() {
        super.onPause();
        alunoEncontrado = null;
    }
}
