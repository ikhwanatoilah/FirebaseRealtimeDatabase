package com.example.firebaserealtimedatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;
    DatabaseReference databaseArtist;
    ListView listViewArtist;
    List<Artist> artistList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtist = FirebaseDatabase.getInstance().getReference("Artist");


        editTextName = (EditText)findViewById(R.id.editTextName);
        buttonAdd = (Button)findViewById(R.id.buttonAddArtist);
        spinnerGenres = (Spinner)findViewById(R.id.spinnerGenres);

        listViewArtist = (ListView)findViewById(R.id.listViewArtists);
        artistList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistList.clear();

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);

                    artistList.add(artist);
                }
                ArtistList adapter = new ArtistList(MainActivity.this, artistList);
                listViewArtist.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addArtist(){
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString().trim();

        if (!TextUtils.isEmpty(name)){


            String id = databaseArtist.push().getKey();
            Artist artist = new Artist(id,name,genre);
            databaseArtist.child(id).setValue(artist);

            Toast.makeText(this, "Artist Added",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"U Should Enter a name", Toast.LENGTH_LONG).show();

        }
    }
}
