package com.example.jonathan.labo2;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Nouveau extends Fragment {
    EditText txtId;
    EditText txtNom;
    EditText txtPrenom;
    EditText txtTelephone;
    Button btnSave;
    DatabaseHelper mDbHelper;
    DatabaseFront dbFront;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_main, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // txtId = (EditText)view.findViewById(R.id.txtId);
        mDbHelper = new DatabaseHelper(this.getActivity());
        dbFront = new DatabaseFront(Nouveau.this.getActivity());
        txtPrenom = (EditText)view.findViewById(R.id.txtPrenom);
        txtNom = (EditText)view.findViewById(R.id.txtNom);
        txtTelephone = (EditText)view.findViewById(R.id.txtTelephone);
        btnSave = (Button)view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifier(txtTelephone.getText().toString()) == true){
                    enregistrer();
                    Toast.makeText(getActivity(), "Enregistrement Réussi", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Telephone Invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void enregistrer(){
    Membre m = new Membre(txtPrenom.getText().toString(),
            txtNom.getText().toString(), txtTelephone.getText().toString());
        dbFront.ecrireMembre2DB(m);
    }
    private boolean verifier(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
