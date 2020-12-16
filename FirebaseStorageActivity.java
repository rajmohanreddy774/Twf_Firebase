package in.arkapps.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageActivity extends AppCompatActivity {

    private Button upload,download;
    private FirebaseStorage storage;
    private ImageView imageView;
private StorageReference imageRef;
    private int REQUEST_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_storage);

        upload = findViewById(R.id.uploadImage);
        download = findViewById(R.id.downloadImage);
         storage = FirebaseStorage.getInstance();
         imageView = findViewById(R.id.image);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);


            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageRef!=null) {
                    downloadImage(imageRef);

                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==REQUEST_CODE && resultCode==RESULT_OK && data!=null ){

            Uri imageUri = data.getData();

            UploadImage(imageUri);

            Toast.makeText(getApplicationContext(),"Image received",Toast.LENGTH_SHORT).show();

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    private void UploadImage(Uri uri){

        StorageReference reference = storage.getReference();

        StorageReference imagesFolder = reference.child("images");

     imageRef = imagesFolder.child("myImage.jpg");

        Task uploadTask = imageRef.putFile(uri);


     imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getApplicationContext(),"Upload success",Toast.LENGTH_SHORT).show();


                downloadImage(imageRef);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("TAG", "onFailure: "+e.getMessage() );
                Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void downloadImage(StorageReference ref){
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Glide.with(getApplicationContext()).load(url).into(imageView);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }



}
