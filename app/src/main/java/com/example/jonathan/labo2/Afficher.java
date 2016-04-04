package com.example.jonathan.labo2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Afficher extends AppCompatActivity {
     protected EditText nom;
     protected EditText prenom;
     protected EditText telephone;
     protected Button cmdAppeler;
     protected Button cmdEffacer;
     protected  Button cmdModifier;
     protected int contactId;
     protected  String tel;
     protected SQLiteDatabase db;
     DatabaseFront databaseFront;
//COMMIT
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher);

        nom = (EditText)findViewById(R.id.txtNom);
        prenom = (EditText)findViewById(R.id.txtPrenom);
        telephone = (EditText)findViewById(R.id.editText);
        databaseFront = new DatabaseFront(Afficher.this);

        //Récupérer le Bundle
        getBundle();

        cmdAppeler = (Button)findViewById(R.id.btnCall);
        cmdAppeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = telephone.getText().toString();
                String uri = "tel:" + tel.trim();
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse(uri));
                startActivity(i);
            }
        });

        cmdModifier = (Button)findViewById(R.id.btnMod);
        cmdModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(verifier(telephone.getText().toString()) == true){
                modifier();
                finish();
            }
            else{
                Toast.makeText(Afficher.this, "Numero Invalide", Toast.LENGTH_SHORT).show();
            }
            }
        });
        cmdEffacer = (Button)findViewById(R.id.btnEffacer);
        cmdEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseFront.EffaceMembre(contactId);
                finish();
            }
        });
    }

    public void getBundle(){
        contactId = getIntent().getIntExtra("CONTACT_ID", 0);
         Membre m = databaseFront.getMemberById(contactId);
        nom.setText(m.getNom());
        prenom.setText(m.getPrenom());
        telephone.setText(m.getTelephone());
    }

    public void modifier(){
       Membre m = new Membre(contactId,nom.getText().toString(),
                 prenom.getText().toString(),
                telephone.getText().toString());
        databaseFront.updateMembre(m);
        Toast.makeText(Afficher.this, "Update Complete", Toast.LENGTH_SHORT).show();
    }

    private boolean verifier(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
