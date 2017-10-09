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


    public void inserirClasse(String tabela, String matricula, Object obj) {
        dadosReferencia.child(tabela).child(matricula).setValue(obj);
    }
    
}
