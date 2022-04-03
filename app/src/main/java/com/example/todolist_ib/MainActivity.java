package com.example.todolist_ib;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private final static int MY_REQUEST_CODE = 1;
    ListView maListe;
    ArrayAdapter<String> myarray;

    String daily[] = { "Sacar al perro ; pediente", "comprar el pan ; pediente",
            "revisar el correo de la salle ; pediente", "preparar reuniones del día ; pediente",
            "hacer ejercicio ; pediente" };

    ArrayList<String> myArrayList = new ArrayList<String>(Arrays.asList(daily));

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor = sharedPreferences.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("Users", Context.MODE_PRIVATE);
        Button button = (Button) findViewById(R.id.addButton);
        String empty = "empty";
        ajt(empty);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
    }

    public void ajt(String test){
        maListe = findViewById(R.id.list);
        myarray = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, myArrayList);
        for(int i = 0; i < myArrayList.size(); i++)
        {
            editor.putString(String.valueOf(i),String.valueOf(myArrayList.indexOf(i)));
        }
        editor.commit();
        maListe.setAdapter(myarray);
        maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                String test = item.toString();
                String[] parties = test.split(";");
                String itBug = parties[1];
                if(itBug.equals(" realisado")){
                    myArrayList.set(position,parties[0]+"; pediente");
                }else{
                    myArrayList.set(position,parties[0]+"; realisado");
                }
                editor.clear();
                editor.commit();
                for(int i = 0; i < myArrayList.size(); i++)
                {
                    editor.putString(String.valueOf(i),String.valueOf(myArrayList.indexOf(i)));
                }
                editor.commit();
                myarray.notifyDataSetChanged();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
               String test = data.getStringExtra("value");
                myArrayList.add(test+" ; pediente");
                editor.clear();
                editor.commit();
                for(int i = 0; i < myArrayList.size(); i++)
                {
                    editor.putString(String.valueOf(i),String.valueOf(myArrayList.indexOf(i)));
                }
                editor.commit();
                myarray.notifyDataSetChanged();
            }
        }
    }


}


