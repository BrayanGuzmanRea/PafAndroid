package com.example.agrohubpaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrohubpaf.datos.ApiClient;
import com.example.agrohubpaf.datos.ApiService;
import com.example.agrohubpaf.dominio.LoginRequest;
import com.example.agrohubpaf.dominio.LoginResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniLoginFragment extends Fragment {
    private TextInputEditText editTextUser, editTextPassword; // Campos de usuario y contraseña
    private Button buttonLogin;
    private ApiService apiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ini_login, container, false);

        // Inicializar Retrofit y ApiService
        apiService = ApiClient.getClient().create(ApiService.class);

        editTextUser = view.findViewById(R.id.editText_user);       // ID correcto
        editTextPassword = view.findViewById(R.id.editText_password); // ID correcto
        buttonLogin = view.findViewById(R.id.button_login);         // ID correcto

        //.................................Para iniciar sesion......................................

        // Encuentra el Button con id button_login
        Button buttonLogin = view.findViewById(R.id.button_login);

        // Agrega un OnClickListener al Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
        //..........................................................................................

        //...............Para crear la cuenta de nuevo usuario......................................
        // Encuentra el TextView con id textView_create_account
        TextView textViewCreateAccount = view.findViewById(R.id.textView_create_account);

        // Agrega un OnClickListener al TextView
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método para navegar al fragmento IniRegistroFragment
                navigateToIniRegistro();
            }
        });

        //..........................................................................................


        // Encuentra el TextView con id textView_recover_account
        TextView textViewRecoverAccount = view.findViewById(R.id.textView_recover_account);

        // Agrega un OnClickListener al TextView
        textViewRecoverAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes agregar la lógica de recuperación de cuenta
                // ...
            }
        });

        return view;
    }

    private void performLogin() {
        String user = editTextUser.getText().toString();
        String password = editTextPassword.getText().toString();

        // Validar que los campos no estén vacíos
        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto LoginRequest
        LoginRequest loginRequest = new LoginRequest(user, password);

        // Llamar a la API
        Call<LoginResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        String rol = loginResponse.getRol();

                        if ("Agricultor".equals(rol)) {
                            NavDirections action = IniLoginFragmentDirections.actionIniLoginFragmentToAgPerfilAgricultorFragment();
                            NavHostFragment.findNavController(IniLoginFragment.this).navigate(action);
                        } else if ("Consumidor".equals(rol)) {
                            NavDirections action = IniLoginFragmentDirections.actionIniLoginFragmentToCliConsumidorVistaFragment();
                            NavHostFragment.findNavController(IniLoginFragment.this).navigate(action);
                        }
                    } else {
                        Toast.makeText(getContext(), loginResponse.getMensaje(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta del servidor", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_LONG).show();
            }

        });
    }



    private void navigateToIniRegistro() {
        // Usa la acción definida en el nav_graph.xml
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_iniLoginFragment_to_iniRegistroFragment);
    }
}