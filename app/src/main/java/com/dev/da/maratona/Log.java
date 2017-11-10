package com.dev.da.maratona;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by ramon on 05/11/2017.
 */

public class Log {
    private String matricula;
    private int pontos;
    private Context applicationContext = Tab0Administrador.getContextOfApplication();
    private Aluno alunoLogado = recuperarLogin(applicationContext);
    private String path = alunoLogado.getPrimeiroNome() + "_" + alunoLogado.getUltimoNome();
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

    public Log(String matricula,int pontos) {
        this.matricula = matricula;
        this.pontos = pontos;
    }

    public Log(Aluno aluno, int pontos){
        this.matricula = aluno.getMatricula();
        this.pontos = pontos;
    }

    public Log(){
        this.matricula = null;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getPath() {
        return path;
    }

    private String getDateTime() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        return sdt;
    }

    public void pontos(String tipo){
        String matricula = getMatricula();
        DatabaseReference ref = firebase;
        final String tipo_path;
        final String tipo_log;

        if(tipo.equals("Adicionar")){
            tipo_path = "AdicionarPontos";
            tipo_log = "Adicionou";
        }
        else {
            tipo_path = "RemoverPontos";
            tipo_log = "Removeu";
        }

        ref.child("Alunos").child(matricula).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Aluno aluno = dataSnapshot.getValue(Aluno.class);
                DatabaseReference ref = firebase.child("log").child(tipo_path).child(path).child(getDateTime());
                ref.setValue(tipo_log+" " + pontos + " pontos ao aluno "+aluno.getPrimeiroNome()+" "+aluno.getUltimoNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void faltas(String tipo){
        String matricula = getMatricula();
        DatabaseReference ref = firebase;

        final String tipo_path;
        final String tipo_log;

        if(tipo.equals("Adicionar")){
            tipo_path = "AdicionarFaltas";
            tipo_log = "Adicionou";
        }
        else {
            tipo_path = "RemoverFaltas";
            tipo_log = "Removeu";
        }

        ref.child("Alunos").child(matricula).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Aluno aluno = dataSnapshot.getValue(Aluno.class);
                DatabaseReference ref = firebase.child("log").child(tipo_path).child(path).child(getDateTime());
                ref.setValue(tipo_log + " uma falta ao usuário "+aluno.getPrimeiroNome()+" "+aluno.getUltimoNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void periodo(){
        firebase.child("AdicionarPeriodo").child(path).child(getDateTime()).setValue("Período adicionado para todos os alunos.");
    }

    public void imagem(){
        firebase.child("log").child("AdicionarImagens").child(path).child(getDateTime()).setValue("Adicionou uma uma nova imagem ao seu perfil");
    }

    private Aluno recuperarLogin(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("alunoLogado", null);
        if (json != null) {
            Gson gson = new Gson();
            Aluno aluno = gson.fromJson(json, Aluno.class);
            return aluno;
        } else {
            return null;
        }
    }
}
