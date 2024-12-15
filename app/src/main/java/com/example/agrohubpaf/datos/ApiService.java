package com.example.agrohubpaf.datos;



import com.example.agrohubpaf.dominio.AgricultorRequest;
import com.example.agrohubpaf.dominio.AgricultorResponse;
import com.example.agrohubpaf.dominio.CategoriaResponse;
import com.example.agrohubpaf.dominio.ConsumidorRequest;
import com.example.agrohubpaf.dominio.ConsumidorResponse;
import com.example.agrohubpaf.dominio.LoginRequest;
import com.example.agrohubpaf.dominio.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;


public interface ApiService {
    @POST("registrar_agricultor")
    Call<AgricultorResponse> registrarAgricultor(@Body AgricultorRequest agricultorRequest);

    @POST("registrar_consumidor")
    Call<ConsumidorResponse> registrarConsumidor(@Body ConsumidorRequest consumidorRequest);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("obtenerCategorias")
    Call<List<CategoriaResponse>> obtenerCategorias();

}
