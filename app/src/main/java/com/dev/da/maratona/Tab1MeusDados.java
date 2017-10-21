package com.dev.da.maratona;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.dev.da.maratona.LoginActivity.alunoLogado;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab1MeusDados extends Fragment {

    private Aluno aluno;
    private TextView teste;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private StorageReference storage = storageReference.child("Fotos/" + alunoLogado.getMatricula());
    private ImageView foto;

    public Tab1MeusDados(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1meus_dados, container, false);

        //Demonstração de como se deve pegar os dados da activity do fragment (Aba do menu) --> Inicio
        String nome = aluno.getNome();

        foto = rootView.findViewById(R.id.img_aluno);
        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(storage)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .error(R.drawable.usuario)
                .into(foto);

        teste = rootView.findViewById(R.id.texto);
        teste.setText(
                nome + "\n" +
                        aluno.getMatricula() + "\n" +
                        aluno.getPontuacao() + " Pontos\n" +
                        aluno.getPeriodo() + "º Período\n" +
                        aluno.getFaltas() + " Faltas"
        );
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        foto = getActivity().findViewById(R.id.img_aluno);
        Glide.with(getContext())
                .using(new FirebaseImageLoader())
                .load(storage)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .error(R.drawable.usuario)
                .into(foto);
    }
}
