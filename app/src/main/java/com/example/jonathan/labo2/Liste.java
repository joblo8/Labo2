package com.example.jonathan.labo2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Liste extends Fragment {
    protected ListView listContacts;
    protected ListAdapter adapter;
    protected Button refresh;
    DatabaseFront databaseFront;
    List<HashMap<String, String>> liste;

    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_liste, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listContacts = (ListView) view.findViewById(R.id.listView);
        refresh = (Button)view.findViewById(R.id.refresh);

        recup();

        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                liste.get(position);
                Intent intent = new Intent(Liste.this.getActivity(), Afficher.class);
;
                int idChoisi = Integer.parseInt
                        (((TextView) view.findViewById(R.id.idContact)).getText().toString());
               intent.putExtra("CONTACT_ID",idChoisi);
                startActivity(intent);
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recup();
            }
        });
    }

    public void recup() {
      databaseFront = new DatabaseFront(Liste.this.getActivity());
        /*<HashMap<String, String>>*/ liste = new ArrayList<>();
        liste = databaseFront.getMembersNames("");

        adapter = new SimpleAdapter(this.getActivity(),
                liste,
                R.layout.list_item,
                new String[]{"membreId","fieldPrenom", "fieldNom"},
                new int[]{R.id.idContact, R.id.firstName, R.id.lastName});
        listContacts.setAdapter(adapter);
    }
}
