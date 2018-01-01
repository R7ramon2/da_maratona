package com.dev.da.maratona;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private EditText matricula_input, senha_input;
    private Button entrar;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private Aluno alunoVerifica;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        alunoVerifica = recuperarLogin();
        if (alunoVerifica != null) {
            String admin_database = alunoVerifica.getAdmin();
            if (isAdmin(admin_database)) {
                Intent intent = new Intent(LoginActivity.this, MenuAdminActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent aluno = new Intent(LoginActivity.this, MenuAlunoActivity.class);
                startActivity(aluno);
                finish();
            }
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        matricula_input = findViewById(R.id.login_input);
        senha_input = findViewById(R.id.senha_input);
        entrar = findViewById(R.id.btnLogar);

        matricula_input.addTextChangedListener(EditTextMask.mask(matricula_input, EditTextMask.MATRICULA));
        senha_input.addTextChangedListener(EditTextMask.mask(senha_input, EditTextMask.SENHA));

        entrar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final String matricula = matricula_input.getText().toString();
                final String senha = senha_input.getText().toString();
                if (matricula.equals("")) {
                    Toast.makeText(LoginActivity.this, "Informe a matricula", Toast.LENGTH_SHORT).show();
                }
                else if(matricula.length() < 11){
                    Toast.makeText(LoginActivity.this, "Matricula incompleta", Toast.LENGTH_SHORT).show();
                }
                else if (senha.equals("")) {
                    Toast.makeText(LoginActivity.this, "Informe a senha", Toast.LENGTH_SHORT).show();
                }
                else if(senha.length() < 6){
                    Toast.makeText(LoginActivity.this, "Senha incompleta", Toast.LENGTH_SHORT).show();
                }
                else {
                    unicapLogin(matricula,senha);
                }
                return false;
            }
        });
    }


    // procedimento responsável por verificar a integridade dos dados e logar usuário.
    private void logar(final String matricula) {
        DatabaseReference ref = firebase.child("Alunos").child(matricula).getRef();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    alunoVerifica = dataSnapshot.getValue(Aluno.class);
                    String admin_database = dataSnapshot.child("admin").getValue().toString();

                    salvaLogin(alunoVerifica);
                    if (isAdmin(admin_database)) {
                        Intent admin = new Intent(LoginActivity.this, MenuAdminActivity.class);
                        startActivity(admin);
                    } else {
                        Intent aluno = new Intent(LoginActivity.this, MenuAlunoActivity.class);
                        startActivity(aluno);
                    }
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() {
                            matricula_input.setText("");
                            senha_input.setText("");
                        }
                    }, 2000);

                } else {
                    Toast.makeText(LoginActivity.this, "Usuário não cadastrado na maratona.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Erro com o banco de dados. Favor contactar o desenvolvedor", Toast.LENGTH_LONG).show();
            }
        });
    }

    // procedimento responsável por validar o tipo de usuário (Admin / Aluno)
    private boolean isAdmin(String admin) {
        return !admin.equals("0");
    }

    // função responsável por criptografar a senha para validação com o Firebase
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

    // procedimento para salvar usuário no shared preferences e mantê-lo logado
    private void salvaLogin(Aluno aluno) {
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(aluno);
        prefsEditor.putString("alunoLogado", json);
        prefsEditor.apply();
    }

    // procedimento para recuperar usuário através do shared preferences
    private Aluno recuperarLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("alunoLogado", null);
        if (json != null) {
            Gson gson = new Gson();
            Aluno aluno = gson.fromJson(json, Aluno.class);
            return aluno;
        } else {
            return null;
        }
    }

    private void unicapLogin(final String matricula, final String senha) {
        String[] matr = matricula.split("-");
        String newMatricula = matr[0];
        String digito = matr[1];
        String url = "http://rmlocareceptivos.localhoost.com/da/unicap_login/login_catolica.php?matricula=" + newMatricula + "&digito=" + digito + "&senha=" + senha;

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Aguarde ...");
        dialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("1")) {
                            logar(matricula);
                        } else if (response.equals("-1")) {
                            Toast.makeText(LoginActivity.this, "Erro com a internet ou servidor", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("0")) {
                            Toast.makeText(LoginActivity.this, "Erro ao logar no app, consulte o D.A", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("2")) {
                            Toast.makeText(LoginActivity.this, "Matricula incorreta", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("3")) {
                            Toast.makeText(LoginActivity.this, "Erro do digito verificador", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("4")) {
                            Toast.makeText(LoginActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Talvez você esteja sem internet ou não está cadastrado, entre em contato com o D.A", Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });
        queue.add(stringRequest);
    }
}