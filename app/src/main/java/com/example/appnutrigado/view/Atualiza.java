package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Atualiza extends AppCompatActivity {

    private String nomeRacas, sexo, montada, id, numeroBrinco, nomeDoAnimal, dataNacimento, pesoDoAnimal, racas;
    private EditText editNomeAnimais, editDataNasc, editPesoAnimal;
    private TextView texNumBrinco;
    private Spinner spinnerRacas;
    private RadioButton masculino, feminino, natural, artificial;
    private Button atualizar, deletar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar);

        Intent i = getIntent();
        if (i != null) {
            Bundle parms = i.getExtras();
            if (parms != null) {
                id = parms.getString("id");
                numeroBrinco = parms.getString("numerobrinco");
                System.out.println(id);
            }
        }

        inicializarComponentes();
        spinner();
        buscarDados();
        eventoClick();
    }


    private void buscarDados() {
        String email1 = user.getEmail();
        db.collection("Usuario").document(email1).collection("Fazendas").document(id)
                .collection("Animais").whereEqualTo("Numero do Brinco", numeroBrinco)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        nomeDoAnimal = (String) document.get("Nome do Animal");
                        numeroBrinco = (String) document.get("Numero do Brinco");
                        dataNacimento = (String) document.get("Data de Nascimento");
                        pesoDoAnimal = (String) document.get("Peso do Animal");
                        sexo = (String) document.get("sexo");
                        montada = (String) document.get("montada");
                        racas = (String) document.get("Raça");
                        editNomeAnimais.setText(nomeDoAnimal);
                        texNumBrinco.setText(numeroBrinco);
                        editDataNasc.setText(dataNacimento);
                        editPesoAnimal.setText(pesoDoAnimal);
                        if (sexo.equals("Macho"))
                            masculino.setChecked(true);
                        else
                            feminino.setChecked(true);
                        if (montada.equals("natural"))
                            natural.setChecked(true);
                        else
                            artificial.setChecked(true);
                        if (racas.equals("Holandesa"))
                            spinnerRacas.setSelection(0);
                        else if (racas.equals("Jersey"))
                            spinnerRacas.setSelection(1);
                        else if (racas.equals("Gir"))
                            spinnerRacas.setSelection(2);
                        else if (racas.equals("Girolando"))
                            spinnerRacas.setSelection(3);
                        else
                            spinnerRacas.setSelection(4);
                    }
                }
            }
        });
    }

    private void eventoClick() {


        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Radio_Button();
                    String email = user.getEmail();
                    Map<String, Object> animais = new HashMap<>();
                    animais.put("Numero do Brinco", texNumBrinco.getText().toString());
                    animais.put("Nome do Animal", editNomeAnimais.getText().toString());
                    animais.put("Raça", nomeRacas);
                    animais.put("sexo", sexo);
                    animais.put("montada", montada);
                    animais.put("Data de Nascimento", editDataNasc.getText().toString());
                    animais.put("Peso do Animal", editPesoAnimal.getText().toString());
                    System.out.println(id);
                    Log.i("ITALAC", "Incrição estadual: " + id);

                    db.collection("Usuario").document(email).collection("Fazendas")
                            .document(id).collection("Animais").document(numeroBrinco)
                            .set(animais)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    alert("Sucesso ao Cadastra");
                                    Intent i = new Intent(Atualiza.this, List_Animais_Cad.class);
                                    Bundle parms = new Bundle();
                                    parms.putString("id", id);
                                    i.putExtras(parms);
                                    startActivity(i);
                                }
                            });
                }
            }
        });

        deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getEmail();
                if (user != null) {
                    db.collection("Usuario").document(email).collection("Fazendas")
                            .document(id).collection("List_Animais_Cad").document(numeroBrinco).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            alert("deletado com sucesso");
                            Intent i = new Intent(Atualiza.this, List_Animais_Cad.class);
                            Bundle parms = new Bundle();
                            parms.putString("id", id);
                            i.putExtras(parms);
                            startActivity(i);
                            //
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alert("falha ao deletar");
                        }
                    });
                }
            }
        });
    }

    private void spinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.racas, android.R.layout.simple_spinner_item);
        spinnerRacas.setAdapter(adapter);
        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nomeRacas = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerRacas.setOnItemSelectedListener(escolha);
    }

    private void inicializarComponentes() {
        texNumBrinco = (TextView) findViewById(R.id.textNumBrinco);
        editNomeAnimais = (EditText) findViewById(R.id.edtNomeAnimais);
        spinnerRacas = (Spinner) findViewById(R.id.spinnerRaca);
        editDataNasc = (EditText) findViewById(R.id.edtDataNasc);
        editPesoAnimal = (EditText) findViewById(R.id.edtPesoDoAnimal);
        masculino = (RadioButton) findViewById(R.id.masculino);
        feminino = (RadioButton) findViewById(R.id.femenino);
        natural = (RadioButton) findViewById(R.id.natural);
        artificial = (RadioButton) findViewById(R.id.artificial);
        atualizar = (Button) findViewById(R.id.btnAtualizar);
        deletar = (Button) findViewById(R.id.btnDeletar);
    }

    private void alert(String msg) {
        Toast.makeText(Atualiza.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void Radio_Button() {
        if (masculino.isChecked())
            sexo = "Macho";
        if (feminino.isChecked())
            sexo = "femea";
        if (natural.isChecked())
            montada = "natural";
        if (artificial.isChecked())
            montada = "artificial";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(Atualiza.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
