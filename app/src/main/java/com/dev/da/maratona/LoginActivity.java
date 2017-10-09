package com.dev.da.maratona;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Alerta alerta;
    EditText login_input,senha_input;
    Button entrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_input = (EditText) findViewById(R.id.login_input);
        senha_input = (EditText) findViewById(R.id.senha_input);
        entrar = (Button) findViewById(R.id.logar);

        login_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoInputOri = login_input.getText().toString();
                if(textoInputOri.equals("Usuário")){
                    login_input.setText("");
                }
            }
        });

        senha_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String textoInputOri = senha_input.getText().toString();
                if(textoInputOri.equals("Senha")){
                    senha_input.setText("");
                }
                return false;
            }
        });

        senha_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                String textoInputOri = senha_input.getText().toString();
                if(focus){
                    if(textoInputOri.equals("Senha")){
                        senha_input.setText("");
                    }
                }
            }
        });

        /*teste*/
        FirebaseInteracao firebase = new FirebaseInteracao();
        firebase.inserirClasse("Alunos");
        /*teste*/

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = login_input.getText().toString();
                String senha = senha_input.getText().toString();

                if(login.equals("") || senha.equals("") || login.equals("Usuário") || senha.equals("Senha")){
                    Toast.makeText(LoginActivity.this,"Login ou senha não informados",Toast.LENGTH_SHORT).show();
                }
                else{
                    Alerta alerta = new Alerta("Olá,"+login,"Avalie o nosso app",LoginActivity.this);
                    alerta.exibir();
                    //TODO implementar acesso ao usuário (Firebase)
                }
            }
        });
    }
}
