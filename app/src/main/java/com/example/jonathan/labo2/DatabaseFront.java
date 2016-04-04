package com.example.jonathan.labo2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
private class PostTask extends AsyncTask<String, String, String> {
    public static final String strURL = "http://192.168.140.105/Labo3/gestionContacts.php";
    @Override
    protected String doInBackground(String... data){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(strURL);
    }
}
*/


public class DatabaseFront {

    static SQLiteDatabase db = null;
    protected Cursor cursor; //voir diapositive suivant
    public static final String strURL = "http://10.0.2.2/Labo3/gestionContacts.php"; // localhost pour emulateur
   // public static final String strURL = "http://192.168.140.105/Labo3/gestionContacts.php";
    static final String NODE_CONTACT = "contact";
    static final String NODE_ID = "idContact";
    static final String NODE_NOM = "nom";
    static final String NODE_PRENOM = "prenom";
    static final String NODE_AGE = "age";
    static final String NODE_SEXE = "sexe";
    static final String NODE_TEL = "telephone";
    static final String NODE_COURRIEL = "courriel";
    private List<NameValuePair> paramRequete;
    private XMLDOMParser parser;
    private String ligneResult = null;
    public volatile boolean parsingComplete = true;

    public DatabaseFront(Context cntx) {
        parser = new XMLDOMParser();
    }

    public String getLigneResult(){
        return ligneResult;
    }

    public void clearParsingComplete(){
        parsingComplete = true;
    }

    private void sendRequest(List<NameValuePair> pairs){
        paramRequete = pairs;
        ligneResult = null;
        parsingComplete = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(strURL);
                    httppost.setEntity(new UrlEncodedFormEntity(paramRequete));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity2 = response.getEntity();
                    ligneResult = EntityUtils.toString(entity2);
                    parsingComplete = true;
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void sendRequestNoResponse(List<NameValuePair> pairs){
        paramRequete = pairs;
        ligneResult = null;
        parsingComplete = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(strURL);
                    httppost.setEntity(new UrlEncodedFormEntity(paramRequete));
                    HttpResponse response = httpclient.execute(httppost);
                    parsingComplete = true;
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public List<HashMap<String, String>> getMembersNames(String searchKey) {
        List<HashMap<String, String>> nameArray = null;
       // Document doc = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "lister"));
        pairs.add(new BasicNameValuePair("params", searchKey));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            nameArray = new ArrayList<>();
            System.out.print("Databasefront getMembersNames ligneResult = " + ligneResult + "\n\n");
            System.out.flush();
            Document doc = parser.getDocument(ligneResult);
             System.out.print("Databasefront getMembersNames doc = " + doc + "\n\n");
            System.out.flush();
            NodeList nodeList = doc.getElementsByTagName(NODE_CONTACT);
            for(int i = 0; i< nodeList.getLength(); i++){
                Element e = (Element) nodeList.item(i);
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("membreId", parser.getValue(e, NODE_ID));
                map.put("fieldNom", parser.getValue(e, NODE_NOM));
                map.put("fieldPrenom", parser.getValue(e, NODE_PRENOM));
                nameArray.add(map);
            }
        }
        return nameArray;
    }


    public Membre getMemberById(int id){
        Membre membre = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "detail"));
        pairs.add(new BasicNameValuePair("idContact", "" + id));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            Document doc =parser.getDocument(ligneResult);
            NodeList nodeList = doc.getElementsByTagName(NODE_CONTACT);

            if (nodeList != null) {
                Element e = (Element) nodeList.item(0);
                membre = new Membre(
                        id,
                        parser.getValue(e, NODE_NOM),
                        parser.getValue(e, NODE_PRENOM),
                        parser.getValue(e, NODE_TEL));
            }
        }
        return membre;
    }

    public boolean EffaceMembre(int id){
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "effacer"));
        pairs.add(new BasicNameValuePair("idContact", ""+id));
        sendRequestNoResponse(pairs);
        while(parsingComplete == false); // attendre la fin de la transaction avant de retourner à la liste
        return true;
    }

    void ecrireMembre2DB(Membre m){

        String[] params = Membre.getArrayFields();
        String[] values = m.getArrayFieldsContent();
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "enregistrer"));
        // || is the concatenation operation in SQLite
        int i;
        for(i=1; i<params.length ; i++)
            pairs.add(new BasicNameValuePair(params[i], values[i]));
        sendRequestNoResponse(pairs);

    }

    void updateMembre(Membre m){
        String[] params = Membre.getArrayFields();
        String[] values = m.getArrayFieldsContent();
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "update"));
        // || is the concatenation operation in SQLite
        int i;
        for(i=0; i<params.length ; i++)
            pairs.add(new BasicNameValuePair( params[i], values[i]));
        sendRequestNoResponse(pairs);
    }
}
