package com.dev.da.maratona;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;


/**
 * Created by ramon on 27/01/2018.
 */

public class Alerta {
    private String titulo,mensagem;
    private AlertDialog alerta;

    public Alerta() {
        this.titulo = null;
        this.mensagem = null;
    }

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

    public void alertaExibir(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if(getTitulo() == null || getMensagem() == null){
            System.exit(1);
        }
        else {
            builder.setTitle(getTitulo());
            builder.setMessage(getMensagem());

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    alerta.dismiss();
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(activity,"O aplicativo foi fechado por motivos de segurança com login",Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            });
            alerta = builder.create();
            alerta.show();
        }
    }

}
