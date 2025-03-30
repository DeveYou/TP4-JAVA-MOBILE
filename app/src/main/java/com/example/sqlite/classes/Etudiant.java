package com.example.sqlite.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private String dateOfBirth;
    private byte[] image;

    public Etudiant(){
    }
    public Etudiant(String nom, String prenom, String dateOfBirth, byte[] image){
        this.nom = nom;
        this.prenom = prenom;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

}
