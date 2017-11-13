package com.dev.da.maratona;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

public class SetSenhaActivity extends AppCompatActivity {

    private EditText senha_input;
    private Button btn_cadastrar;
    DatabaseReference firebase = FirebaseDatabase.getInstance().getReference().child("Alunos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_senha);

        senha_input = findViewById(R.id.senha_input);
        btn_cadastrar = findViewById(R.id.btnCadastrar);

        Intent intent = getIntent();
        intent.getExtras();
        final String matricula = intent.getStringExtra("matricula");

        senha_input.addTextChangedListener(EditTextMask.mask(senha_input, EditTextMask.SENHA));

        btn_cadastrar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!senha_input.equals("")){
                    String senha = criptografar(senha_input.getText().toString());
                    DatabaseReference ref = firebase.child(matricula);
                    ref.child("senha").setValue(senha);
                    ref.child("verificado").setValue(true);


                    firebase.child(matricula).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Aluno aluno = dataSnapshot.getValue(Aluno.class);
                            salvaLogin(aluno);
                            if(isAdmin(aluno.getAdmin())) {
                                startActivity(new Intent(SetSenhaActivity.this,MenuAdminActivity.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(SetSenhaActivity.this,MenuAlunoActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(SetSenhaActivity.this,"Por favor informe uma senha",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    private String criptografar(String senha) {
        int i1 = Integer.parseInt(String.valueOf(senha.charAt(0))) * 24 * 3 * 2 * 2;
        int i2 = Integer.parseInt(String.valueOf(senha.charAt(1))) * 13 * 7 * 2 * 7;
        int i3 = Integer.parseInt(String.valueOf(senha.charAt(2))) * 69 * 5 * 8 * 11;
        int i4 = Integer.parseInt(String.valueOf(senha.charAt(3))) * 21 * 7 * 5 * 9;
        int i5 = Integer.parseInt(String.valueOf(senha.charAt(4))) * 19 * 12 * 7 * 3;
        int i6 = Integer.parseInt(String.valueOf(senha.charAt(5))) * 97 * 9 * 2;
        String c1 = Integer.toString(i1);
        String c2 = Integer.toString(i2);
        String c3 = Integer.toString(i3);
        String c4 = Integer.toString(i4);
        String c5 = Integer.toString(i5);
        String c6 = Integer.toString(i6);
        return c1 + c2 + c3 + c4 + c5 + c6;
    }

    private void salvaLogin(Aluno aluno) {
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(aluno);
        prefsEditor.putString("alunoLogado", json);
        prefsEditor.apply();
    }

    private boolean isAdmin(String admin) {
        return !admin.equals("0");
    }
}
