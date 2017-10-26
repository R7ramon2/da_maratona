package com.dev.da.maratona;

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

import static com.dev.da.maratona.LoginActivity.alunoLogado;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab1MeusDados extends Fragment {

    private TextView dados_aluno;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference storage = storageReference.child("Fotos/" + alunoLogado.getImagem());
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private ImageView foto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1meus_dados, container, false);

        //Demonstração de como se deve pegar os dados da activity do fragment (Aba do menu) --> Inicio
        String nome = alunoLogado.getNome();

        foto = rootView.findViewById(R.id.img_aluno);

        firebase.child("Alunos/"+alunoLogado.getMatricula()+"/imagem").addValueEventListener(new ValueEventListener() {
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

        dados_aluno = rootView.findViewById(R.id.informacoes_individuais);
        dados_aluno.setText(
                nome + "\n" +
                        alunoLogado.getMatricula() + "\n" +
                        alunoLogado.getPontuacao() + " Pontos\n" +
                        alunoLogado.getPeriodo() + "º Período\n" +
                        alunoLogado.getFaltas() + " Faltas"
        );
        return rootView;
    }
}
