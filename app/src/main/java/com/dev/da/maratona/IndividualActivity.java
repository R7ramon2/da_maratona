package com.dev.da.maratona;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class IndividualActivity extends AppCompatActivity {

    private TextView informacoes;
    private ImageView foto;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private String url = "http://www.unicap.br/pergamum3/Pergamum/biblioteca_s/meu_pergamum/getImg.php?cod_pessoa=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Aluno aluno = (Aluno) getIntent().getSerializableExtra("aluno");

        informacoes = findViewById(R.id.informacoes_individuais);
        foto = findViewById(R.id.img_aluno);

        carregarInformacoes(aluno);
    }

    private String formataMatricula(String matricula){
        String[] separa = matricula.split("-");
        return separa[0] + separa[1];
    }

    private void carregarInformacoes(Aluno aluno) {
        StorageReference storage = storageReference.child("Fotos/" + aluno.getImagem());

        if(aluno.getImagem().equals("0")){
            Picasso.with(getApplicationContext()).load(url + formataMatricula(aluno.getMatricula())).error(R.drawable.usuario).into(foto);
        }
        else {
            Glide.with(getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(storage)
                    .into(foto);
        }
        informacoes.setText(
                aluno.getPrimeiroNome() + " " + aluno.getUltimoNome() + "\n\n" +
                        aluno.getNick() + "\n" +
                        aluno.getPontuacao() + " Pontos" + "\n" +
                        aluno.getPeriodo() + "º Período"
        );
    }
}
