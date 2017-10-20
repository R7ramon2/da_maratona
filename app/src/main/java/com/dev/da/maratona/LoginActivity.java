package com.dev.da.maratona;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText matricula_input, senha_input;
    private Button entrar;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        matricula_input = (EditText) findViewById(R.id.login_input);
        senha_input = (EditText) findViewById(R.id.senha_input);
        entrar = (Button) findViewById(R.id.btnLogar);

        matricula_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                senha_input.setText("");
                return false;
            }
        });

        matricula_input.addTextChangedListener(EditTextMask.mask(matricula_input, EditTextMask.MATRICULA));
        senha_input.addTextChangedListener(EditTextMask.mask(senha_input, EditTextMask.SENHA));

        entrar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final String matricula = matricula_input.getText().toString();
                final String senha = senha_input.getText().toString();
                logar(matricula, senha);
                return false;
            }
        });
    }

    // procedimento responsável por verificar a integridade dos dados e logar usuário.
    private void logar(final String matricula, final String senha) {
        if (matricula.equals("")) {
            Toast.makeText(LoginActivity.this, "Digite a matrícula.", Toast.LENGTH_SHORT).show();
        } else if (matricula.length() < 11) {
            Toast.makeText(LoginActivity.this, "Matrícula incompleta.", Toast.LENGTH_SHORT).show();
        } else if (senha.equals("")) {
            Toast.makeText(LoginActivity.this, "Digite a senha.", Toast.LENGTH_SHORT).show();
        } else if (senha.length() < 6) {
            Toast.makeText(LoginActivity.this, "Senha incompleta.", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference ref = firebase.child("Alunos").child(matricula).getRef();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Aluno usuario_objeto = dataSnapshot.getValue(Aluno.class);

                        String admin_database = dataSnapshot.child("admin").getValue().toString();
                        String senha_database = dataSnapshot.child("senha").getValue().toString();
                        if (senha_database.equals(senha)) {
                            if (isAdmin(admin_database)) {
                                Intent intent = new Intent(LoginActivity.this, MenuAdminActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("usuario_objeto",usuario_objeto);
//                                bundle.putSerializable("search_objeto",usuario_objeto);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                Intent aluno = new Intent(LoginActivity.this, MenuAlunoActivity.class);
                                aluno.putExtra("usuario_objeto", usuario_objeto);
                                startActivity(aluno);
                            }
                            new android.os.Handler().postDelayed(new Runnable() {
                                public void run() {
                                    matricula_input.setText("");
                                    senha_input.setText("");
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(LoginActivity.this, "Senha incorreta.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Usuário incorreto.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "Erro com o banco de dados. Favor contactar o desenvolvedor", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // procedimento responsável por validar o tipo de usuário (Admin / Aluno)
    protected boolean isAdmin(String admin) {
        return !admin.equals("0");
    }
}