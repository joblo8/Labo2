package com.example.jonathan.labo2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;


public class Membre implements Parcelable {
    private int id;
    private String nom;
    private String prenom;

    private String telephone;


    public Membre (){
        this( "", "", "");
    }
    public Membre(String nom, String prenom, String telephone) {
        this(-1, nom,  prenom,telephone);
    }

    public Membre(int id, String nom, String prenom,String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;

        this.telephone = telephone;

    }

    public int getId(){
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }



    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String fonction) {
        telephone = fonction;
    }



    public Membre(Parcel in) {
        this.id = in.readInt();
        this.nom = in.readString();
        this.prenom = in.readString();

        this.telephone = in.readString();

    }

    public ArrayList getArrayList(){
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("_id", ""+id);
        map.put("nom", prenom + " " + nom);
//        map.put("prenom", prenom);

        map.put("telephone", telephone);

        listItem.add(map);
        return listItem;
    }

    public String[] getArrayFieldsContent() {
        return new String[]{""+id, nom, prenom, telephone};
    }

    static public String[] getArrayFields(){
        return new String[] {"idContact", "nom", "prenom", "telephone"};
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nom);
        dest.writeString(prenom);

        dest.writeString(telephone);

    }

    public static final Creator<Membre> CREATOR = new Creator<Membre>()
    {
        @Override
        public Membre createFromParcel(Parcel source)
        {
            return new Membre(source);
        }

        @Override
        public Membre[] newArray(int size)
        {
            return new Membre[size];
        }
    };

    @Override
    public String toString() {
        return /*"" + id + ";\n" +*/ "Nom : " + nom + " " + prenom +"\nTel : " + telephone;
    }
}
