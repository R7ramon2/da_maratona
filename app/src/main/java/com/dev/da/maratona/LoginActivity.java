package com.dev.da.maratona;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

                login_input.addTextChangedListener(EditTextMask.mask(login_input, EditTextMask.MATRICULA));
                senha_input.addTextChangedListener(EditTextMask.mask(senha_input, EditTextMask.SENHA));

                Aluno a = new Aluno();
                a.setMatricula("201520243-1");
                a.setSenha("123456");
                a.setFaltas(5);
                a.setNome("Tiago Emerenciano");
                a.setPeriodo("5ª");

                firebase.child("Alunos").child(a.getMatricula()).setValue(a);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = login_input.getText().toString();
                String senha = senha_input.getText().toString();

                // Logar usuário, caso as informações estejam corretas
                logar(login,senha);
            }
        });
    }

    // procedimento responsável por verificar a integridade dos dados e logar usuário.
    private void logar(final String matricula, final String senha){
        if (matricula.equals("")) {
            Toast.makeText(LoginActivity.this, "Digite a matrícula.", Toast.LENGTH_SHORT).show();
        } 
		else if (matricula.length() < 11) {
            Toast.makeText(LoginActivity.this, "Matrícula incompleta.", Toast.LENGTH_SHORT).show();
        } 
		else if (senha.equals("")) {
            Toast.makeText(LoginActivity.this, "Digite a senha.", Toast.LENGTH_SHORT).show();
        } 
		else if(senha.length() < 6){
            Toast.makeText(LoginActivity.this, "Senha incompleta.", Toast.LENGTH_SHORT).show();
        } 
		else {
            DatabaseReference ref = firebase.child("Alunos").child(matricula).getRef();
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String nome = dataSnapshot.child("nome").getValue().toString();
                        String senha_database = dataSnapshot.child("senha").getValue().toString();
                        if (senha_database.equals(senha)) {
                            Alerta alerta = new Alerta("Olá, " + nome, "Avalie nosso app", LoginActivity.this);
                            alerta.exibir();
                        } else {
                            Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
