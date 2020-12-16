package in.arkapps.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseFirestoreActivity extends AppCompatActivity {

    public static final String TAG = "FirebaseFirestoreActivity";
    Button addData,getData,updateData,delete;
    TextView myData;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_firestore);


        firebaseFirestore = FirebaseFirestore.getInstance();
        addData = findViewById(R.id.add_data);
        getData = findViewById(R.id.get_data);
        myData = findViewById(R.id.my_data);
        updateData = findViewById(R.id.update_data);
        delete = findViewById(R.id.delete_data);

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData();
            }
        });

        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getData();

//                getSingleData();

                queryData();
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });


    }

    private void AddData() {
        UserData data = new UserData();

        data.setName("Amit");
        data.setAge(23);
        data.setEmail("hello.arkapps@gmail.com");
        data.setFatherName("Shree Ramdulare Kesharvani");
        data.setSurname("Kesharvani");

        firebaseFirestore.collection("User").document().set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(FirebaseFirestoreActivity.this,"Success",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("TAG", "onFailure: "+e.getMessage() );
                Toast.makeText(FirebaseFirestoreActivity.this, "Failed  : Error is "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getData(){
        firebaseFirestore.collection("User").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        if (!queryDocumentSnapshots.isEmpty()){

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                                UserData userData = documentSnapshot.toObject(UserData.class);

                                String data = "Name : "+userData.getName() + " \n "+"email : "+userData.getEmail() +
                                        " \n "+"Age : "+userData.getAge() + "\n"+"Father Name"+userData.getFatherName();


                                myData.setText(data);



                            }


                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void getSingleData(){
        firebaseFirestore.collection("User").document("6uZDcCvQZGdq65yfH0mT").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserData userData = documentSnapshot.toObject(UserData.class);


                        String data = "Name : "+userData.getName() + " \n "+"email : "+userData.getEmail() +
                                " \n "+"Age : "+userData.getAge() + "\n"+"Father Name"+userData.getFatherName();


                        myData.setText(data);

                    }
                });

    }

    private void queryData(){

     Query query =  firebaseFirestore.collection("User").whereEqualTo("name","Amit").whereGreaterThan("age",20);

  query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {



          StringBuilder builder  = new StringBuilder();

          for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

              UserData userData = documentSnapshot.toObject(UserData.class);

              builder.append("Name : "+userData.getName()+" \n" );
              builder.append("Age : "+userData.getAge() + " \n");
              builder.append("Email : "+userData.getEmail() + " \n");
              builder.append("\n ------------------------------------------ \n");


          }

          myData.setText(builder);

      }
  });

    }

    private void updateData(){

        firebaseFirestore.collection("User").document("0WQgTNCzZOcJzUpwU8zb").update("name","Kisan","email","abc@gmail.com")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(FirebaseFirestoreActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteData(){
        firebaseFirestore.collection("User").document("I247jnH8lcAAjA430IDz")
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(FirebaseFirestoreActivity.this, "Doc deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
