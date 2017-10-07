package com.dev.da.maratona;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by ramon on 05/10/2017.
 */

public class Alerta {
    String titulo,mensagem;


    public Alerta(String titulo, String mensagem) {
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void exibir(final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(getTitulo());

        builder.setMessage(getMensagem());

        builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(activity, "Positivo = " +arg1, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Negativo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(activity, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
