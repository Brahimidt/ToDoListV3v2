package com.example.todolist_ib;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private final static int MY_REQUEST_CODE = 1;
    ListView maListe;
    ArrayAdapter<String> myarray;
    ArrayList<String> myArrayList = new ArrayList<String>();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    retrofitInterface service = retrofit.create(retrofitInterface.class);

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("items", Context.MODE_PRIVATE);
        String s1 = sharedPreferences.getString("1", "");

        service.todoList().enqueue(new Callback<List<item>>() {
            @Override
            public void onResponse(Call<List<item>> call, Response<List<item>> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    for(int i = 0; i <  response.body().size(); i++)
                    {
                        if(response.body().get(i).completed == false){
                            myArrayList.add(response.body().get(i).title + " ; pediente");
                        }else{
                            myArrayList.add(response.body().get(i).title + " ; realisado");
                        }

                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<item>> call, Throwable t) {
            }
        });

        Button button = (Button) findViewById(R.id.addButton);
        String empty = "empty";
        ajt(empty);
        myArrayList.add("");
        myarray.notifyDataSetChanged();
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
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
                myarray.notifyDataSetChanged();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                for(int i = 0; i < myArrayList.size(); i++)
                {
                    editor.putString(String.valueOf(i),myArrayList.get(i));
                }
                editor.commit();

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
                myarray.notifyDataSetChanged();
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            for(int i = 0; i < myArrayList.size(); i++)
            {
                editor.putString(String.valueOf(i),myArrayList.get(i));
            }
            editor.commit();

        }
    }


}


