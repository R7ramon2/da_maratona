package com.dev.da.maratona;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/*
 * Created by ramon on 05/10/2017.
 */

public class Alerta {
    private String titulo, mensagem;
    private Activity activity;
    private boolean retorno;

    public Alerta(String titulo, String mensagem, Activity activity) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.activity = activity;
    }

    private String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    private String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    private Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private boolean getRetorno() {
        return retorno;
    }

    private void setRetorno(boolean retorno) {
        this.retorno = retorno;
    }

    private void defineAlerta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getTitulo());

        builder.setMessage(getMensagem());

        builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), "Positivo = " + arg1, Toast.LENGTH_SHORT).show();
                setRetorno(true);
            }
        });

        builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getActivity(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
                setRetorno(false);
            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }

    public boolean exibir() {
        defineAlerta();
        return getRetorno();
    }
}
