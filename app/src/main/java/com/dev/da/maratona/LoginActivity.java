package com.dev.da.maratona;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText login_input, senha_input;
    private Button entrar;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_input = (EditText) findViewById(R.id.login_input);
        senha_input = (EditText) findViewById(R.id.senha_input);
        entrar = (Button) findViewById(R.id.btnLogar);

        login_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                senha_input.setText("");
                return false;
            }
        });

        login_input.addTextChangedListener(EditTextMask.mask(login_input, EditTextMask.MATRICULA));
        senha_input.addTextChangedListener(EditTextMask.mask(senha_input, EditTextMask.SENHA));

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String login = login_input.getText().toString();
                final String senha = senha_input.getText().toString();

                DatabaseReference ref = firebase.child("Alunos").child(login).getRef();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String nome = dataSnapshot.child("nome").getValue().toString();
                            String senha_database = dataSnapshot.child("senha").getValue().toString();
                            if (senha_database.equals(senha)) {
                                logar(login, senha);
                            } else {
                                Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
            Toast.makeText(LoginActivity.this, "Usuário logado", Toast.LENGTH_SHORT).show();
            //TODO direcionar para o menu
        }
    }

}
