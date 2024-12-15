package com.example.agrohubpaf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.agrohubpaf.R;
import com.example.agrohubpaf.datos.ApiClient;
import com.example.agrohubpaf.datos.ApiService;
import com.example.agrohubpaf.dominio.CategoriaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CliConsumidorVistaFragment extends Fragment {
    private LinearLayout contenedorCategorias;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cli_consumidor_vista, container, false);
        contenedorCategorias = view.findViewById(R.id.contenedor_categorias);
        cargarCategorias();
        return view;
    }

    private void cargarCategorias() {
        ApiClient.getClient().create(ApiService.class).obtenerCategorias().enqueue(new Callback<List<CategoriaResponse>>() {
            @Override
            public void onResponse(Call<List<CategoriaResponse>> call, Response<List<CategoriaResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (CategoriaResponse categoria : response.body()) {
                        agregarLayoutCategoria(categoria);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoriaResponse>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error al cargar categorías: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void agregarLayoutCategoria(CategoriaResponse categoria) {
        if (getContext() == null) return;

        // Crear CardView para la categoría
        CardView cardView = new CardView(getContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 16, 0, 16);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(8);
        cardView.setCardElevation(4);

        // Layout interior del CardView
        LinearLayout layoutCategoria = new LinearLayout(getContext());
        layoutCategoria.setOrientation(LinearLayout.VERTICAL);
        layoutCategoria.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutCategoria.setPadding(16, 16, 16, 16);

        // Título de la categoría
        TextView tituloCategoria = new TextView(getContext());
        tituloCategoria.setText("Categoría: " + categoria.getNombreCategoria());
        tituloCategoria.setTextSize(18);
        tituloCategoria.setPadding(0, 0, 0, 16);

        // Agregar el título al layout
        layoutCategoria.addView(tituloCategoria);

        // Aquí puedes agregar más elementos como imágenes de productos
        // Por ejemplo:
        ImageView imagenProducto = new ImageView(getContext());
        imagenProducto.setImageResource(R.drawable.arandanos); // Asegúrate de tener esta imagen
        imagenProducto.setAdjustViewBounds(true);
        imagenProducto.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutCategoria.addView(imagenProducto);

        // Texto del precio
        TextView textoPrecio = new TextView(getContext());
        textoPrecio.setText("Precio por kilo: $50");
        textoPrecio.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textoPrecio.setPadding(0, 16, 0, 0);
        layoutCategoria.addView(textoPrecio);

        // Agregar el layout al CardView
        cardView.addView(layoutCategoria);

        // Agregar el CardView al contenedor principal
        contenedorCategorias.addView(cardView);
    }
}