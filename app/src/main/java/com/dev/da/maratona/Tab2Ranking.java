package com.dev.da.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab2Ranking extends Fragment {

    final private ArrayList<Aluno> lista = new ArrayList<>();
    private ListView classificao;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2ranking, container, false);

        classificao = rootView.findViewById(R.id.classificacao_lista);

        contruirLista();

        classificao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Aluno aluno = (Aluno) classificao.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), IndividualActivity.class);
                intent.putExtra("aluno", aluno);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void contruirLista() {
        firebase.child("Alunos").orderByChild("pontuacao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    lista.add(postSnapshot.getValue(Aluno.class));
                }
                Collections.reverse(lista);
                AlunoAdapter alunoAdapter = new AlunoAdapter(getContext(), lista);
                classificao.setAdapter(alunoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
