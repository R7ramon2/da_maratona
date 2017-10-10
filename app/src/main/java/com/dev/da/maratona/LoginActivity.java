package com.dev.da.maratona;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText login_input,senha_input;
    Button entrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_input = (EditText) findViewById(R.id.login_input);
        senha_input = (EditText) findViewById(R.id.senha_input);
        entrar = (Button) findViewById(R.id.btnLogar);

        login_input.addTextChangedListener(EditTextMask.mask(login_input, EditTextMask.MATRICULA));
        senha_input.addTextChangedListener(EditTextMask.mask(senha_input, EditTextMask.SENHA));

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = login_input.getText().toString();
                String senha = senha_input.getText().toString();

                if(login.equals("")){
                    Toast.makeText(LoginActivity.this,"Digite o login",Toast.LENGTH_SHORT).show();
                }
                else if(senha.equals("")){
                    Toast.makeText(LoginActivity.this, "Digite a senha.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Login efetuado!", Toast.LENGTH_SHORT).show();
                    //TODO implementar acesso ao usuário (Firebase)
                }
            }
        });
    }
}
