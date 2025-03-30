package com.example.sqlite;

import android.app.ComponentCaller;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlite.classes.Etudiant;
import com.example.sqlite.services.EtudiantService;
import com.example.sqlite.util.Convert;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText nom;
    private EditText prenom;
    private Button save;
    private EditText id;
    private Button search;
    private Button delete;
    private Button showTable;
    private Button loadImage;
    private DatePicker date_of_birth;
    private ImageView studentImage;
    private Bitmap selectedImage;

    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EtudiantService etudiantService = new EtudiantService(this);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        save = findViewById(R.id.save);
        date_of_birth = findViewById(R.id.date_of_birth);
        loadImage = findViewById(R.id.loadImage);
        studentImage = findViewById(R.id.studentImage);

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateOfBirth = date_of_birth.getDayOfMonth() + "/" + date_of_birth.getMonth() + "/" + date_of_birth.getYear();
                if(selectedImage == null){
                    Toast.makeText(MainActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                etudiantService.create(new Etudiant(nom.getText().toString(), prenom.getText().toString(), dateOfBirth, Convert.convertToBytes(selectedImage)));
                Toast.makeText(MainActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();
                clear();
            }
        });

        id = findViewById(R.id.id);
        search = findViewById(R.id.search);
        delete = findViewById(R.id.delete);
        showTable = findViewById(R.id.getInfo);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etudiant etudiant = etudiantService.findById(Integer.parseInt(id.getText().toString().trim()));

                if(etudiant != null){
                    View popup = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_popup, null, false);

                    final ImageView image = popup.findViewById(R.id.image);
                    final TextView firstName = popup.findViewById(R.id.firstName);
                    final TextView lastName = popup.findViewById(R.id.lastName);
                    final TextView dateOfBirth = popup.findViewById(R.id.dateOfBirth);

                    if(etudiant.getImage() != null){
                        image.setImageBitmap(Convert.convertToBitmap(etudiant.getImage()));
                    }else{
                        image.setImageResource(R.drawable.user);
                    }


                    firstName.setText(etudiant.getPrenom());
                    lastName.setText(etudiant.getNom());
                    dateOfBirth.setText(etudiant.getDateOfBirth());
                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setView(popup).setNegativeButton("Exit", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                    dialog.show();
                }else{
                    Toast.makeText(MainActivity.this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etudiant etudiant = etudiantService.findById(Integer.parseInt(id.getText().toString()));
                if(etudiant != null){
                    etudiantService.delete(etudiant);
                    Toast.makeText(MainActivity.this,"etudiant deleted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"etudiant not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentsList.class);
                startActivity(intent);
            }
        });

    }

    private void openImageChooser(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            if(imageUri != null){
                try {
                    if(Build.VERSION.SDK_INT >= 28){
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imageUri);
                        selectedImage = ImageDecoder.decodeBitmap(source);
                    }else{
                        selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    }
                    studentImage.setImageBitmap(selectedImage);
                }catch (IOException e){
                    Toast.makeText(MainActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(MainActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void clear(){
        nom.setText("");
        prenom.setText("");
        studentImage.setImageResource(R.drawable.user);
    }


}