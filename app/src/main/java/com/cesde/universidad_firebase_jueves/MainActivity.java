package com.cesde.universidad_firebase_jueves;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText etcarnet, etnombre, etcarrera, etsemestre;
    CheckBox cbactivo;
    Button btadicionar, btmodificar,btanular,bteliminar;
    String carnet, nombre,carrera,semestre, coleccion="Estudiante";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ocultar barra de titulo por defecto

        getSupportActionBar().hide();
        //Asociar los objetos java con los ojetos xml

        etcarnet = findViewById(R.id.etcarnet);
        etnombre = findViewById(R.id.etnombre);
        etcarrera = findViewById(R.id.etcarrera);
        etsemestre = findViewById(R.id.etsemestre);

        cbactivo = findViewById(R.id.cbactivo);
        btadicionar = findViewById(R.id.btadicionar);
        btmodificar = findViewById(R.id.btmodificar);
        btanular = findViewById(R.id.btanular);
        bteliminar = findViewById(R.id.bteliminar);
        etcarnet.requestFocus();

    }

    public void Adicionar(View view){

        //toda la informacion fue adiccionada

        carnet = etcarnet.getText().toString();
        nombre = etnombre.getText().toString();
        carrera = etcarrera.getText().toString();
        semestre = etsemestre.getText().toString();

        if (!carnet.isEmpty() && !nombre.isEmpty() && !carrera.isEmpty() && !semestre.isEmpty()){
            // Create a new user with a first and last name
            Map<String, Object> Alumno = new HashMap<>();
            Alumno.put("Carnet", carnet);
            Alumno.put("Nombre", nombre);
            Alumno.put("Carrera", carrera);
            Alumno.put("Semestre", semestre);
            Alumno.put("Activo", "Si");

// Add a new document with a generated ID
            db.collection(coleccion)
                    .add(Alumno)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                           // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Error guardando documento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "Datos requeridos", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }

    }//fin del metodo onCrete

    public void Consultar(View view){

        Consultar_documento();

    }//fin metodo consultar

    private  void Consultar_documento(){
        carnet = etcarnet.getText().toString();
        if (!carnet.isEmpty()){

            db.collection(coleccion)
                    .whereEqualTo("Carnet",carnet)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().size() != 0){
                                    //cuando encontro almenis un documento

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        etnombre.setText(document.getString("Nombre"));
                                        etcarrera.setText(document.getString("Carrera"));
                                        etsemestre.setText(document.getString("Semestre"));

                                        if(document.getString("Activo").equals("Si"))
                                            cbactivo.setChecked(true);
                                        else
                                            cbactivo.setChecked(false);
                                    }//fin del ciclo for

                                    btanular.setEnabled(true);
                                    bteliminar.setEnabled(true);
                                    btmodificar.setEnabled(true);
                                }else {
                                    //cuando no encontro documentos
                                    Toast.makeText(MainActivity.this, "No se encontro ningun registro", Toast.LENGTH_SHORT).show();
                                    btadicionar.setEnabled(true);
                                }

                                etcarnet.setEnabled(false);
                                etnombre.setEnabled(true);
                                etcarrera.setEnabled(true);
                                etsemestre.setEnabled(true);
                                etnombre.requestFocus();

                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());

                            }

                        }
                    });

        }else{
            Toast.makeText(this, "El carnet es requerido!!", Toast.LENGTH_SHORT).show();
            etcarnet.requestFocus();
        }

    }//fin metodo consultar_documento

    private void Limpiar_campos(){
        etcarnet.setText("");
        etnombre.setText("");
        etsemestre.setText("");
        etcarrera.setText("");

        cbactivo.setEnabled(false);
        etcarnet.setEnabled(true);
        etnombre.setEnabled(false);
        etsemestre.setEnabled(false);
        etcarrera.setEnabled(false);

        bteliminar.setEnabled(false);
        btmodificar.setEnabled(false);
        btanular.setEnabled(false);
        btadicionar.setEnabled(false);
        etcarnet.requestFocus();


    }

    public void Limpiar(View view){
        Limpiar_campos();
    }



}