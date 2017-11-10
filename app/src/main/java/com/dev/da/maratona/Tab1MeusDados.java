package com.dev.da.maratona;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab1MeusDados extends Fragment {

    private TextView dados_aluno;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private ImageView foto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1meus_dados, container, false);
        final Aluno alunoLogado = recuperarLogin();
        final StorageReference storage = storageReference.child("Fotos/" + alunoLogado.getImagem());

        foto = rootView.findViewById(R.id.img_aluno);
        dados_aluno = rootView.findViewById(R.id.informacoes_individuais);
        firebase.child("Alunos/" + alunoLogado.getMatricula() + "/imagem").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storage)
                        .into(foto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        firebase.child("Alunos/" + alunoLogado.getMatricula()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Aluno aluno = dataSnapshot.getValue(Aluno.class);

                dados_aluno.setText(
                        aluno.getNome() + "\n" +
                                aluno.getMatricula() + "\n" +
                                aluno.getPontuacao() + " Pontos\n" +
                                aluno.getPeriodo() + "º Período\n" +
                                aluno.getFaltas() + " Faltas");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

    private Aluno recuperarLogin() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
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
