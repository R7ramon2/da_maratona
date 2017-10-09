package com.dev.da.maratona;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ramon on 08/10/2017.
 */

public class FirebaseInteracao {
    private DatabaseReference dadosReferencia = FirebaseDatabase.getInstance().getReference();
    private String auto_incremento;

    public String getAuto_incremento() {
        return auto_incremento;
    }

    public void setAuto_incremento(String auto_increment) {
        this.auto_incremento = auto_increment;
    }

    public void auto_increment(String tabela){

        dadosReferencia.child(tabela).limitToLast(1).addValueEventListener(new ValueEventListener() {
            int id_auto_increment;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String idString;
                if (dataSnapshot.getValue() != null) {
                    String usuario = dataSnapshot.getValue().toString();
                    String splitId[] = usuario.split("=");

                    String aux[] = splitId[0].split("\\{");
                    idString = aux[1];
                    id_auto_increment = Integer.parseInt(idString) + 1;
                    setAuto_incremento(Integer.toString(id_auto_increment));
                } else {
                    id_auto_increment = 1;
                    setAuto_incremento(Integer.toString(id_auto_increment));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void inserirClasse(String tabela){
        String id = getAuto_incremento();
        if(id == null){
            id = "1";
            setAuto_incremento(id);
        }
        auto_increment(tabela);
        dadosReferencia.child(tabela).child(id).child("nome").setValue("Ramon");
    }
}
