package com.dev.da.maratona;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.dev.da.maratona.LoginActivity.alunoLogado;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab3Foto extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_CAMERA = 2;
    private Button selecionarImagem;
    private Button uploadImagem;
    private Button abrirCamera;
    private Uri filePath;
    private ImageView foto;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3foto, container, false);

        selecionarImagem = rootView.findViewById(R.id.btn_selecionarImagem);
        uploadImagem = rootView.findViewById(R.id.btn_uploadImagem);
        foto = rootView.findViewById(R.id.imageView);
        abrirCamera = rootView.findViewById(R.id.btn_camera);
        abrirCamera.setVisibility(View.VISIBLE);

        abrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamera();
            }
        });

        selecionarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarImagem();
            }
        });

        uploadImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImagem();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                        foto.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PICK_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    filePath = data.getData();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    foto.setImageBitmap(bitmap);
                }
                break;
        }
    }

    private void selecionarImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE_REQUEST);
    }

    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivityForResult(intent, PICK_IMAGE_CAMERA);
    }

    private void uploadImagem() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Enviando...");
            progressDialog.show();
            final StorageReference reference = storageReference.child("Fotos/" + alunoLogado.getImagem());

            if (!alunoLogado.getImagem().equals("0")) {
                reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Erro ao enviar a imagem.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            alunoLogado.setImagem(gerarMD5() + "_" + alunoLogado.getPrimeiroNome() + "_" + alunoLogado.getUltimoNome());

            StorageReference reference_novo = storageReference.child("Fotos/" + alunoLogado.getImagem());

            reference_novo.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    firebase.child("Alunos/" + alunoLogado.getMatricula() + "/imagem").setValue(alunoLogado.getImagem());
                    Toast.makeText(getContext(), "Foto enviada!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage((((int) progress) + "% enviados..."));
                }
            });
        } else {
            Toast.makeText(getContext(), "Selecione uma imagem!", Toast.LENGTH_SHORT).show();
        }
    }

    private String gerarMD5() {
        Random random = new Random(System.currentTimeMillis());
        String s = String.valueOf(random);
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException a) {
            return "0.png";
        }
    }
}
