[33mtag v0.01[m
Tagger: mayksuel2727 <mayksuel2727@gmail.com>
Date:   Sat May 2 18:15:04 2020 -0300

versão funcional de cadastramento, atualizaçõa e exclusão de animais

[33mcommit bfed72645040f3b206c4d3f55a13796cddb62b54[m[33m ([m[1;36mHEAD -> [m[1;32mmaster[m[33m, [m[1;33mtag: v0.01[m[33m, [m[1;31morigin/master[m[33m, [m[1;31morigin/HEAD[m[33m)[m
Author: mayksuel2727 <mayksuel2727@gmail.com>
Date:   Sat May 2 18:13:02 2020 -0300

    Foi refeita a tela de atualização de animais e implementada todas as sua funcionalidades.

[1mdiff --git a/app/src/main/java/com/example/appnutrigado/view/Animais.java b/app/src/main/java/com/example/appnutrigado/view/Animais.java[m
[1mindex cc767a3..9bcdde4 100644[m
[1m--- a/app/src/main/java/com/example/appnutrigado/view/Animais.java[m
[1m+++ b/app/src/main/java/com/example/appnutrigado/view/Animais.java[m
[36m@@ -76,16 +76,18 @@[m [mpublic class Animais extends AppCompatActivity {[m
                         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {[m
                             @Override[m
                             public void onComplete(@NonNull Task<QuerySnapshot> task) {[m
[31m-                                String no = null;[m
[32m+[m[32m                                String numeroBrinco = null;[m
                                 if (task.isSuccessful()) {[m
                                     for (QueryDocumentSnapshot document : task.getResult()) {[m
[31m-                                        no = (String) document.get("Numero do Brinco");[m
[32m+[m[32m                                        numeroBrinco = (String) document.get("Numero do Brinco");[m
[32m+[m[32m                                        System.out.println(numeroBrinco);[m
                                     }[m
                                 }[m
[31m-                                System.out.println(no);[m
[32m+[m[32m                                System.out.println(numeroBrinco);[m
                                 Intent i = new Intent(Animais.this, Atualiza.class);[m
                                 Bundle parms = new Bundle();[m
[31m-                                parms.putString("id", no);[m
[32m+[m[32m                                parms.putString("numerobrinco", numeroBrinco);[m
[32m+[m[32m                                parms.putString("id", id);[m
                                 i.putExtras(parms);[m
                                 startActivity(i);[m
                             }[m
[1mdiff --git a/app/src/main/java/com/example/appnutrigado/view/CadastroAnimais.java b/app/src/main/java/com/example/appnutrigado/view/CadastroAnimais.java[m
[1mindex 26f523d..2c08611 100644[m
[1m--- a/app/src/main/java/com/example/appnutrigado/view/CadastroAnimais.java[m
[1m+++ b/app/src/main/java/com/example/appnutrigado/view/CadastroAnimais.java[m
[36m@@ -26,8 +26,6 @@[m [mimport com.google.firebase.firestore.FirebaseFirestore;[m
 import java.util.HashMap;[m
 import java.util.Map;[m
 [m
[31m-import static com.example.appnutrigado.R.id.spinnerRaca;[m
[31m-[m
 [m
 public class CadastroAnimais extends AppCompatActivity {[m
     private String id, nomeRacas, sexo, montada;[m
[36m@@ -43,7 +41,7 @@[m [mpublic class CadastroAnimais extends AppCompatActivity {[m
         super.onCreate(savedInstanceState);[m
         setContentView(R.layout.activity_cadastro__animais);[m
         inicializarComponentes();[m
[31m-        Spinner();[m
[32m+[m[32m        spinner();[m
         eventoClicks();[m
     }[m
 [m
[36m@@ -109,17 +107,16 @@[m [mpublic class CadastroAnimais extends AppCompatActivity {[m
         editDataNasc = (EditText) findViewById(R.id.edtDataNasc);[m
         editNumBrinco = (EditText) findViewById(R.id.edtNumBrincu);[m
         editNomeAnimais = (EditText) findViewById(R.id.edtNomeAnimais);[m
[31m-        editValorAnimal = (EditText) findViewById(R.id.edtValorAnimal);[m
         editPesoAnimal = (EditText) findViewById(R.id.edtPesoDoAnimal);[m
         btnCadastra = (Button) findViewById(R.id.btnCadastra);[m
[31m-        racas = (Spinner) findViewById(spinnerRaca);[m
[32m+[m[32m        racas = (Spinner) findViewById(R.id.spinnerRaca);[m
         masculino = (RadioButton) findViewById(R.id.masculino);[m
         feminino = (RadioButton) findViewById(R.id.femenino);[m
         natural = (RadioButton) findViewById(R.id.natural);[m
         artificial = (RadioButton) findViewById(R.id.artificial);[m
     }[m
 [m
[31m-    public void Spinner(){[m
[32m+[m[32m    public void spinner(){[m
         ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.racas, android.R.layout.simple_spinner_item);[m
         racas.setAdapter(adapter);[m
         AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {[m
[1mdiff --git a/app/src/main/res/layout/activity_atualizar.xml b/app/src/main/res/layout/activity_atualizar.xml[m
[1mindex 988b733..257c933 100644[m
[1m--- a/app/src/main/res/layout/activity_atualizar.xml[m
[1m+++ b/app/src/main/res/layout/activity_atualizar.xml[m
[36m@@ -12,52 +12,127 @@[m
         android:background="@color/cinza"[m
         android:orientation="vertical">[m
 [m
[31m-        <EditText[m
[31m-            android:id="@+id/edtNumBrincu"[m
[32m+[m[32m        <TextView[m
[32m+[m[32m            android:layout_width="match_parent"[m
[32m+[m[32m            android:layout_height="match_parent"[m
[32m+[m[32m            android:text="@string/brinco"[m
[32m+[m[32m            android:textColor="@color/preto"[m
[32m+[m[32m            android:textSize="17dp"[m
[32m+[m[32m            />[m
[32m+[m
[32m+[m[32m        <TextView[m
[32m+[m[32m            android:id="@+id/textNumBrinco"[m
             android:layout_width="match_parent"[m
             android:layout_height="50dp"[m
[31m-            android:layout_marginTop="20dp"[m
[31m-            android:hint="@string/brinco"[m
[31m-            android:inputType="number" />[m
[32m+[m[32m            />[m
[32m+[m
[32m+[m[32m        <TextView[m
[32m+[m[32m            android:layout_width="match_parent"[m
[32m+[m[32m            android:layout_height="match_parent"[m
[32m+[m[32m            android:text="@string/nome"[m
[32m+[m[32m            android:textColor="@color/preto"[m
[32m+[m[32m            android:textSize="17dp"[m
[32m+[m[32m            />[m
 [m
         <EditText[m
             android:id="@+id/edtNomeAnimais"[m
             android:layout_width="match_parent"[m
[31m-            android:layout_height="50dp"[m
[31m-            android:layout_marginTop="20dp"[m
[31m-            android:hint="@string/nome" />[m
[32m+[m[32m            android:layout_height="50dp" />[m
 [m
[31m-        <EditText[m
[32m+[m[32m        <TextView[m
[32m+[m[32m            android:layout_width="match_parent"[m
[32m+[m[32m            android:layout_height="match_parent"[m
[32m+[m[32m            android:text="@string/raça"[m
[32m+[m[32m            android:textColor="@color/preto"[m
[32m+[m[32m            android:textSize="17dp"[m
[32m+[m[32m            />[m
[32m+[m
[32m+[m[32m        <Spinner[m
             android:id="@+id/spinnerRaca"[m
             android:layout_width="match_parent"[m
             android:layout_height="50dp"[m
[31m-            android:layout_marginTop="20dp"[m
[31m-            android:hint="@string/raça" />[m
[32m+[m[32m            android:textSize="20dp" />[m
[32m+[m
[32m+[m[32m        <TextView[m
[32m+[m[32m            android:layout_width="match_parent"[m
[32m+[m[32m            android:layout_height="match_parent"[m
[32m+[m[32m            android:text="@string/Data"[m
[32m+[m[32m            android:textColor="@color/preto"[m
[32m+[m[32m            android:textSize="17dp"/>[m
 [m
         <EditText[m
             android:id="@+id/edtDataNasc"[m
             android:layout_width="match_parent"[m
             android:layout_height="50dp"[m
[31m-            android:layout_marginTop="20dp"[m
[31m-            android:hint="@string/Data"[m
             android:inputType="date" />[m
 [m
[31m-        <EditText[m
[31m-            android:id="@+id/edtValorAnimal"[m
[32m+[m[32m        <TextView[m
             android:layout_width="match_parent"[m
[31m-            android:layout_height="50dp"[m
[31m-            android:layout_marginTop="20dp"[m
[31m-            android:hint="@string/valor"[m
[31m-            android:inputType="number" />[m
[32m+[m[32m            android:layout_height="match_parent"[m
[32m+[m[32m            android:text="@string/peso"[m
[32m+[m[32m            android:textColor="@color/preto"[m
[32m+[m[32m            android:textSize="17dp"/>[m
 [m
         <EditText[m
             android:id="@+id/edtPesoDoAnimal"[m
             android:layout_width="match_parent"[m
             android:layout_height="50dp"[m
[31m-            android:layout_marginTop="20dp"[m
[31m-            android:hint="@string/peso"[m
             android:inputType="number" />[m
 [m
[32m+[m[32m        <TextView[m
[32m+[m[32m            android:layout_width="wrap_content"[m
[32m+[m[32m            android:layout_height="wrap_content"[m
[32m+[m[32m            android:text="@string/sexo"[m
[32m+[m[32m            android:textColor="@color/preto"[m
[32m+[m[32m            android:textSize="17dp" />[m
[32m+[m
[32m+[m[32m        <RadioGroup[m
[32m+[m[32m            android:layout_width="wrap_content"[m
[32m+[m[32m            android:layout_height="wrap_content"[m
[32m+[m[32m            android:orientation="horizontal">[m
[32m+[m
[32m+[m[32m            <RadioButton[m
[32m+[m[32m                android:id="@+id/masculino"[m
[32m+[m[32m                android:layout_width="wrap_content"[m
[32m+[m[32m                android:layout_height="wrap_content"[m
[32m+[m[32m                android:text="@string/macho"[m
[32m+[m[32m                android:textSize="20dp" />[m
[32m+[m
[32m+[m[32m            <RadioButton[m
[32m+[m[32m                android:id="@+id/femenino"[m
[32m+[m[32m                android:layout_width="wrap_content"[m
[32m+[m[32m                android:layout_height="wrap_content"[m
[32m+[m[32m                android:text="@string/femia"[m
[32m+[m[32m                android:textSize="20dp" />[m
[32m+[m[32m        </RadioGroup>[m
[32m+[m
[32m+[m[32m        <TextView[m
[32m+[m[32m            android:layout_width="wrap_content"[m
[32m+[m[32m            android:layout_height="wrap_content"[m
[32m+[m[32m            android:layout_marginTop="10dp"[m
[32m+[m[32m            android:text="@string/cobertura"[m
[32m+[m[32m            android:textColor="@color/preto"[m
[32m+[m[32m            android:textSize="18dp" />[m
[32m+[m
[32m+[m[32m        <RadioGroup[m
[32m+[m[32m            android:layout_width="wrap_content"[m
[32m+[m[32m            android:layout_height="wrap_content"[m
[32m+[m[32m            android:orientation="horizontal">[m
[32m+[m
[32m+[m[32m            <RadioButton[m
[32m+[m[32m                android:id="@+id/natural"[m
[32m+[m[32m                android:layout_width="wrap_content"[m
[32m+[m[32m                android:layout_height="wrap_content"[m
[32m+[m[32m                android:text="@string/natural"[m
[32m+[m[32m                android:textSize="20dp" />[m
[32m+[m
[32m+[m[32m            <RadioButton[m
[32m+[m[32m                android:id="@+id/artificial"[m
[32m+[m[32m                android:layout_width="wrap_content"[m
[32m+[m[32m                android:layout_height="wrap_content"[m
[32m+[m[32m                android:text="@string/artificial"[m
[32m+[m[32m                android:textSize="20dp" />[m
[32m+[m[32m        </RadioGroup>[m
 [m
     </LinearLayout>[m
 [m
[36m@@ -66,11 +141,11 @@[m
         android:layout_height="wrap_content"[m
         android:layout_alignParentBottom="true"[m
         android:layout_centerHorizontal="true"[m
[31m-        android:layout_marginBottom="50dp"[m
[32m+[m[32m        android:layout_marginBottom="8dp"[m
         android:orientation="horizontal">[m
 [m
         <Button[m
[31m-            android:id="@+id/btnApaga"[m
[32m+[m[32m            android:id="@+id/btnDeletar"[m
             android:layout_width="180dp"[m
             android:layout_height="60dp"[m
             android:layout_marginRight="10dp"[m
[36m@@ -80,7 +155,7 @@[m
             android:textSize="19dp" />[m
 [m
         <Button[m
[31m-            android:id="@+id/btnAtualiza"[m
[32m+[m[32m            android:id="@+id/btnAtualizar"[m
             android:layout_width="180dp"[m
             android:layout_height="60dp"[m
             android:background="@color/verde_escuro"[m
[1mdiff --git a/app/src/main/res/layout/activity_cadastro__animais.xml b/app/src/main/res/layout/activity_cadastro__animais.xml[m
[1mindex 745ab39..9a5c1e4 100644[m
[1m--- a/app/src/main/res/layout/activity_cadastro__animais.xml[m
[1m+++ b/app/src/main/res/layout/activity_cadastro__animais.xml[m
[36m@@ -38,7 +38,8 @@[m
         <EditText[m
             android:id="@+id/edtNomeAnimais"[m
             android:layout_width="match_parent"[m
[31m-            android:layout_height="50dp" />[m
[32m+[m[32m            android:layout_height="50dp"[m
[32m+[m[32m            />[m
 [m
         <TextView[m
             android:layout_width="match_parent"[m
[36m@@ -52,7 +53,8 @@[m
            android:id="@+id/spinnerRaca"[m
            android:layout_width="match_parent"[m
            android:layout_height="50dp"[m
[31m-            />[m
[32m+[m[32m           android:textSize="20dp" />[m
[32m+[m
         <TextView[m
             android:layout_width="match_parent"[m
             android:layout_height="match_parent"[m
[1mdiff --git a/app/src/main/res/values/strings.xml b/app/src/main/res/values/strings.xml[m
[1mindex b25db8f..2e2d38f 100644[m
[1m--- a/app/src/main/res/values/strings.xml[m
[1m+++ b/app/src/main/res/values/strings.xml[m
[36m@@ -33,7 +33,7 @@[m
     <string name="cobertura">Tipo de cobertura</string>[m
     <string name="natural">Monta natural</string>[m
     <string name="artificial">Inseminação artificial</string>[m
[31m-    <string name="Atualiza">Atualiza</string>[m
[32m+[m[32m    <string name="Atualiza">Atualizar</string>[m
     <string name="deletar">Deletar</string>[m
     <string name="nome_fazenda">Nome da Fazenda</string>[m
     <string name="municipio">Municipio</string>[m
[1mdiff --git a/build.gradle b/build.gradle[m
[1mindex a04e065..644ac50 100644[m
[1m--- a/build.gradle[m
[1m+++ b/build.gradle[m
[36m@@ -8,7 +8,7 @@[m [mbuildscript {[m
         [m
     }[m
     dependencies {[m
[31m-        classpath 'com.android.tools.build:gradle:3.6.2'[m
[32m+[m[32m        classpath 'com.android.tools.build:gradle:3.6.3'[m
         classpath 'com.google.gms:google-services:4.3.3'[m
         [m
 [m
