package com.example.todolist_ib;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface retrofitInterface {

    @GET("todos")
    Call<List<item>>todoList();
}
