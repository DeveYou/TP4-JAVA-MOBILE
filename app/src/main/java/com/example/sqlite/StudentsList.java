package com.example.sqlite;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sqlite.classes.Etudiant;
import com.example.sqlite.services.EtudiantService;
import com.example.sqlite.util.Convert;

import java.util.List;

public class StudentsList extends AppCompatActivity {

    private TableLayout tableLayout;
    private EtudiantService etudiantService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_students_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tableLayout = findViewById(R.id.tableLayout);
        etudiantService = new EtudiantService(this);

        loadStudentData();

    }

    private void loadStudentData(){

        Thread thread = new Thread(){
            @Override
            public void run() {
                List<Etudiant> students = etudiantService.findAll();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Etudiant student : students){
                            TableRow row = new TableRow(StudentsList.this);
                            row.setPadding(15, 15, 15, 15);

                            TextView id = new TextView(StudentsList.this);
                            id.setText(String.valueOf(student.getId()));
                            id.setPadding(15, 15,15,15);
                            row.addView(id);

                            ImageView imageView = new ImageView(StudentsList.this);
                            Bitmap bitmap = Convert.convertToBitmap(student.getImage());
                            if(bitmap != null){
                                imageView.setImageBitmap(bitmap);
                            }else{
                                imageView.setImageResource(R.drawable.user);
                            }

                            imageView.setLayoutParams(new TableRow.LayoutParams(100, 100));
                            row.addView(imageView);

                            TextView firstName = new TextView(StudentsList.this);
                            firstName.setText(String.valueOf(student.getPrenom()));
                            firstName.setPadding(15, 15,15,15);
                            row.addView(firstName);

                            TextView lastName = new TextView(StudentsList.this);
                            lastName.setText(String.valueOf(student.getNom()));
                            lastName.setPadding(15, 15,15,15);
                            row.addView(lastName);

                            TextView dateOfBirth = new TextView(StudentsList.this);
                            dateOfBirth.setText(String.valueOf(student.getDateOfBirth()));
                            dateOfBirth.setPadding(15, 15,15,15);
                            row.addView(dateOfBirth);

                            tableLayout.addView(row);
                        }
                    }
                });

            }
        };

        thread.start();
    }
}