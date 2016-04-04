package com.example.jonathan.labo2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Recherche extends Fragment {
    EditText txtRecherche;
    ListView listResultat;
    Button btnRechercher;
    ListAdapter adapter;
    SQLiteDatabase db;
    Cursor cursor;
    DatabaseFront databaseFront;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            return inflater.inflate(R.layout.activity_recherche, container, false);
        }
        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            txtRecherche = (EditText)view.findViewById(R.id.txtRecherche);
            listResultat = (ListView)view.findViewById(R.id.listResultat);
            btnRechercher = (Button)view.findViewById(R.id.btnRechercher);
            db = (new DatabaseHelper(this.getActivity())).getWritableDatabase();

            btnRechercher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search();
                   // listListener();
                }
            });

        }
        @SuppressWarnings("deprecation")
        public void search(){
           //List<HashMap<String, String>> liset;
            databaseFront = new DatabaseFront(Recherche.this.getActivity());
            List<HashMap<String, String>> liste = new ArrayList<>();
            liste = databaseFront.getMembersNames(txtRecherche.getText().toString());

            adapter = new SimpleAdapter(this.getActivity(),
                    liste,
                    R.layout.list_item,
                    new String[]{"fieldPrenom", "fieldNom"},
                    new int[]{R.id.firstName, R.id.lastName});
            listResultat.setAdapter(adapter);
        }

        public void listListener(){
            listResultat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Recherche.this.getActivity(), Afficher.class);
                    Cursor cursor = (Cursor) adapter.getItem(position);
                    intent.putExtra("CONTACT_ID", cursor.getInt(cursor.getColumnIndex("_id")));
                    Toast.makeText(Recherche.this.getActivity(), "" + cursor.getInt(cursor.getColumnIndex("_id")), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });
        }



}
