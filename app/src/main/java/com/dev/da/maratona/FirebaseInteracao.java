package com.dev.da.maratona;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ramon on 08/10/2017.
 */

public class FirebaseInteracao {
    private DatabaseReference dadosReferencia = FirebaseDatabase.getInstance().getReference();
    private boolean result;


    public void inserirClasse(String tabela, String matricula, Object obj) {
        dadosReferencia.child(tabela).child(matricula).setValue(obj);
    }

    public void inserirColuna(String tabela, String matricula, String coluna, String valor) {
        dadosReferencia.child(tabela).child(matricula).child(coluna).setValue(valor);
    }

    public boolean atualizarClasse(final String tabela, final String matricula, final Object obj) {
        DatabaseReference ref = dadosReferencia.child(tabela).child(matricula);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    result = true;
                    dadosReferencia.child(tabela).child(matricula).setValue(obj);
                }
                else {
                    result = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Firebase","Error");
            }
        });
        return result;
    }

    public boolean atualizarCampo(final String tabela,final String matricula,final String campo,final String valor){
        DatabaseReference ref = dadosReferencia.child(tabela).child(matricula).child(campo);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dadosReferencia.child(tabela).child(matricula).child(campo).setValue(valor);
                    result = true;
                }
                else {
                    result = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Firebase","Error");
            }
        });
        return result;
    }

}
