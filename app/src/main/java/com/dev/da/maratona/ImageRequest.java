package com.dev.da.maratona;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by ramon on 31/01/2018.
 */

public class ImageRequest {
    private String urlUnicap = "http://www.unicap.br/pergamum3/Pergamum/biblioteca_s/meu_pergamum/getImg.php?cod_pessoa=";
    private Context context;
    private Aluno aluno;
    private String url;

    public ImageRequest(Context context, Aluno aluno) {
        this.context = context;
        this.aluno = aluno;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return urlUnicap;
    }

    public void setUrlUnicap(String matricula) {
        this.urlUnicap = urlUnicap + matricula;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    private String formataMatricula(String matricula){
        String[] separa = matricula.split("-");
        return separa[0] + separa[1];
    }

    public void aplicarImagemUnicap(ImageView foto){
        Picasso.with(getContext()).load(urlUnicap + formataMatricula(getAluno().getMatricula())).error(R.drawable.usuario).into(foto);
    }

    public void aplicarImagem(ImageView foto){
        Picasso.with(getContext()).load(getUrl()).error(R.drawable.usuario).into(foto);
    }
}
