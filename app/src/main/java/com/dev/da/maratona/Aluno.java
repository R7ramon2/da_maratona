package com.dev.da.maratona;

/*
 * Created by Ramon on 09/10/2017.
 */

import java.io.Serializable;

class Aluno implements Serializable {
    private String nome;
    private String matricula;
    private String senha;
    private int pontuacao;
    private String periodo;
    private int faltas;
    private String admin;

    String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public String getAdmin(){
        return admin;
    }

    public void setAdmin(String admin){
        this.admin = admin;
    }
}
