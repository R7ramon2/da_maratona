package com.dev.da.maratona;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import static android.app.Activity.RESULT_OK;
import static com.dev.da.maratona.LoginActivity.alunoLogado;

/*
 * Created by Tiago Emerenciano on 11/10/2017.
 */

public class Tab3Foto extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_CAMERA = 2;
    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
    final String nomeArquivo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/image.png";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button selecionarImagem;
    private Button uploadImagem;
    private Button abrirCamera;
    private Uri filePath;
    private ImageView foto;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3foto, container, false);

        selecionarImagem = rootView.findViewById(R.id.btn_selecionarImagem);
        uploadImagem = rootView.findViewById(R.id.btn_uploadImagem);
        foto = rootView.findViewById(R.id.imageView);
        abrirCamera = rootView.findViewById(R.id.btn_camera);

        abrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File newDir = new File(dir);
                newDir.mkdirs();
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
                if (resultCode == RESULT_OK && data != null) {
//                    InputStream stream = null;
//                    try {
//                        if (bitmap != null) {
//                            bitmap.recycle();
//                        }
//                        stream = getContext().getContentResolver().openInputStream(data.getData());
//                        bitmap = BitmapFactory.decodeStream(stream);
//                        foto.setImageBitmap(resizeImage(getContext(), bitmap, 200, 200));
//                    } catch (FileNotFoundException e){
//                        e.printStackTrace();
//                    } finally {
//                        if(stream != null){
//                            try {
//                                stream.close();
//                            } catch (IOException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    }
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    filePath = escreveImagens(bitmap);
                    foto.setImageBitmap(bitmap);
                }
                break;
        }
    }

    private void selecionarImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE_REQUEST);
    }

    public void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_IMAGE_CAMERA);
    }

    private void uploadImagem() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Enviando...");
            progressDialog.show();
            StorageReference reference = storageReference.child("Fotos/" + alunoLogado.getMatricula());

            reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
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
            Toast.makeText(getContext(), "Ocorreu um erro. Tente novamente!", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri escreveImagens(Bitmap bmp) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] bytes = stream.toByteArray();

            FileOutputStream pos = new FileOutputStream(nomeArquivo);
            pos.write(bytes);
            pos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(nomeArquivo);
    }
}
