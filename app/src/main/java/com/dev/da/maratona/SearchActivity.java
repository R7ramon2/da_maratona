package com.dev.da.maratona;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    static Aluno alunoEncontrado;
    private EditText editPalavra;
    private ListView listVPesquisa;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editPalavra = (EditText) findViewById(R.id.editPalavra);
        listVPesquisa = (ListView) findViewById(R.id.listView);

        buscar();

        listVPesquisa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                alunoEncontrado = (Aluno) listVPesquisa.getItemAtPosition(position);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        pesquisarNome("");
    }

    private void pesquisarNome(String palavra) {
        Query query;
        final ArrayList<Aluno> lista = new ArrayList<Aluno>();

        if (palavra.equals("")) {
            query = databaseReference.child("Alunos").orderByChild("nome");
        } else {
            query = databaseReference.child("Alunos").orderByChild("nome").startAt(palavra).endAt(palavra + "\uf8ff");
        }
        lista.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Aluno p = objSnapshot.getValue(Aluno.class);
                    lista.add(p);
                }
                AlunoAdapterSearch alunoAdapter = new AlunoAdapterSearch(SearchActivity.this, lista);
                listVPesquisa.setAdapter(alunoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void buscar() {
        editPalavra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String nome = editPalavra.getText().toString().trim();
                pesquisarNome(nome);
            }
        });
    }


}
