package com.example.sqlite.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sqlite.classes.Etudiant;
import com.example.sqlite.util.Convert;
import com.example.sqlite.util.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class EtudiantService {
    private static final String TABLE_NAME = "etudiant";
    private static final String KEY_ID = "id";
    private static final String KEY_NOM = "nom";
    private static final String KEY_PRENOM = "prenom";
    private static final String KEY_DATE_OF_BIRTH = "dateOfBirth";
    private static final String KEY_IMAGE = "image";

    private static String[] COLUMNS = {KEY_ID, KEY_NOM, KEY_PRENOM, KEY_DATE_OF_BIRTH, KEY_IMAGE};

    private MySQLiteHelper helper;

    public EtudiantService(Context context){
        this.helper = new MySQLiteHelper(context);
    }

    public void create(Etudiant etudiant){
        SQLiteDatabase db = this.helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOM, etudiant.getNom());
        values.put(KEY_PRENOM, etudiant.getPrenom());
        values.put(KEY_DATE_OF_BIRTH, etudiant.getDateOfBirth());
        values.put(KEY_IMAGE, etudiant.getImage());

        db.insert(
                TABLE_NAME,
                null,
                values
                );

        Log.d("insert", etudiant.getNom());
        db.close();
    }

    public Etudiant findById(int id){
        Etudiant etudiant = null;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_NAME,
                COLUMNS,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
                );
        if(cursor.moveToFirst()){
            etudiant = new Etudiant();
            etudiant.setId(cursor.getInt(0));
            etudiant.setNom(cursor.getString(1));
            etudiant.setPrenom(cursor.getString(2));
            etudiant.setDateOfBirth(cursor.getString(3));
            etudiant.setImage(cursor.getBlob(4));
        }

        db.close();
        return etudiant;
    }

    public void delete(Etudiant etudiant){
        SQLiteDatabase db = this.helper.getWritableDatabase();
        db.delete(
                TABLE_NAME,
                "id = ?",
                new String[]{String.valueOf(etudiant.getId())}
        );
        db.close();
    }

    public List<Etudiant> findAll(){
        List<Etudiant> etudiants = new ArrayList<>();
        String query = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Etudiant etudiant = null;
        if(cursor.moveToFirst()){
            do{
                etudiant = new Etudiant();
                etudiant.setId(cursor.getInt(0));
                etudiant.setNom(cursor.getString(1));
                etudiant.setPrenom(cursor.getString(2));
                etudiant.setDateOfBirth(cursor.getString(3));
                etudiant.setImage(cursor.getBlob(4));
                etudiants.add(etudiant);
                Log.d("id = ", etudiant.getId() + "");
            }while (cursor.moveToNext());
        }
        return etudiants;
    }

}
