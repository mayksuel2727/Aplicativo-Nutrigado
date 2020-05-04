package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class List_Animais_Cad extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ListView listViewAnimais;
    private List<String> listAnimais = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterAnimais;
    private String animalSelecionado;
    private Button btnCad;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_animais_cad);
        inicializarComponentes();
        Intent i = getIntent();
        if (i!= null){
            Bundle parms = i.getExtras();
            if (parms != null){
                id  = parms.getString("id");
            }
        }
            eventoBusca();
            eventoClick();
    }

    private void eventoClick() {
        final String email1 = user.getEmail();
        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(id);
                Intent i = new Intent(List_Animais_Cad.this, CadastroAnimais.class);
                Bundle parms = new Bundle();
                parms.putString("id", id);
                i.putExtras(parms);
                startActivity(i);
            }
        });
        listViewAnimais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                animalSelecionado = (String) adapterView.getItemAtPosition(position);

                db.collection("Usuario").document(email1).collection("Fazendas")
                        .document(id).collection("Animais").whereEqualTo("Nome do Animal", animalSelecionado)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String numeroBrinco = null;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        numeroBrinco = (String) document.get("Numero do Brinco");
                                        System.out.println(numeroBrinco);
                                    }
                                }
                                System.out.println(numeroBrinco);
                                Intent i = new Intent(List_Animais_Cad.this, Atualiza.class);
                                Bundle parms = new Bundle();
                                parms.putString("numerobrinco", numeroBrinco);
                                parms.putString("id", id);
                                i.putExtras(parms);
                                startActivity(i);
                            }

                        });
            }
        });
    }

    private void eventoBusca() {
        String email1 = user.getEmail();
        System.out.println(id);
        db.collection("Usuario").document(email1).collection("Fazendas")
                .document(id).collection("Animais").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String nome = (String) documentSnapshot.get("Nome do Animal");
                        listAnimais.add(nome);
                    }
                }
                arrayAdapterAnimais = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_expandable_list_item_1, listAnimais);
                listViewAnimais.setAdapter(arrayAdapterAnimais);
            }
        });
    }

    private void inicializarComponentes() {
        listViewAnimais = (ListView) findViewById(R.id.listViewAnimais);
        btnCad = (Button) findViewById(R.id.btnCadastra);
    }

    private void alert(String msg) {
        Toast.makeText(List_Animais_Cad.this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(List_Animais_Cad.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
