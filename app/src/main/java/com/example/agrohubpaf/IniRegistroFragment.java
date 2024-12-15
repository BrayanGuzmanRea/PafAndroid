package com.example.agrohubpaf;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agrohubpaf.datos.ApiClient;
import com.example.agrohubpaf.datos.ApiService;
import com.example.agrohubpaf.dominio.AgricultorRequest;
import com.example.agrohubpaf.dominio.AgricultorResponse;
import com.example.agrohubpaf.dominio.ConsumidorRequest;
import com.example.agrohubpaf.dominio.ConsumidorResponse;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniRegistroFragment extends Fragment {
    private TextInputEditText etNombre, etEmail, etDireccion, etTelefono, etNombreUsuario, etContrasenia, etPreferencias;
    private Spinner spinnerUserType;
    private Button btnRegistrar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ini_registro, container, false);

        // Inicializar los elementos de la interfaz
        etNombre = view.findViewById(R.id.etNombre);
        etEmail = view.findViewById(R.id.etEmail);
        etDireccion = view.findViewById(R.id.etDireccion);
        etTelefono = view.findViewById(R.id.etTelefono);
        etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        etContrasenia = view.findViewById(R.id.etContrasenia);
        etPreferencias = view.findViewById(R.id.etPreferencias);
        spinnerUserType = view.findViewById(R.id.spinnerUserType);
        btnRegistrar = view.findViewById(R.id.buttonEnviar);

        // Configurar el Spinner de tipo de usuario
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.user_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);

        // Agregar listener al botón de registro
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndRegister();
            }
        });

        // Mostrar/ocultar el campo de preferencias según el tipo de usuario seleccionado
        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) { // Consumidor
                    etPreferencias.setVisibility(View.VISIBLE);
                } else {
                    etPreferencias.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TextView ytCuentaTextView = view.findViewById(R.id.YTCuenta);
        ytCuentaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(IniRegistroFragment.this)
                        .navigate(R.id.action_iniRegistroFragment_to_iniLoginFragment);
            }
        });

        adjustKeyboardVisibility(view);


        return view;
    }

    private void validateAndRegister() {
        // Validar que todos los campos obligatorios estén llenos
        if (TextUtils.isEmpty(etNombre.getText()) || TextUtils.isEmpty(etEmail.getText()) ||
                TextUtils.isEmpty(etDireccion.getText()) || TextUtils.isEmpty(etTelefono.getText()) ||
                TextUtils.isEmpty(etNombreUsuario.getText()) || TextUtils.isEmpty(etContrasenia.getText()) ||
                (spinnerUserType.getSelectedItemPosition() == 0)) {
            // Mostrar un mensaje de error indicando los campos faltantes
            showErrorMessage("Por favor, complete todos los campos obligatorios.");
            return;
        }

        // Validar el formato del nombre
        if (!isValidName(etNombre.getText().toString())) {
            showErrorMessage("El nombre solo puede contener letras y espacios.");
            return;
        }

        // Validar el formato del correo electrónico
        if (!isValidEmail(etEmail.getText().toString())) {
            showErrorMessage("Por favor, ingrese un correo electrónico válido.");
            return;
        }

        // Validar el formato del número de teléfono
        if (!isValidPhoneNumber(etTelefono.getText().toString())) {
            showErrorMessage("El número de teléfono debe tener 9 dígitos.");
            return;
        }

        // Validar el tipo de usuario seleccionado
        int selectedUserType = spinnerUserType.getSelectedItemPosition();
        if (selectedUserType == 0) {
            showErrorMessage("Debe seleccionar un tipo de usuario.");
            return;
        }

        registerUser();

    }

    private void adjustKeyboardVisibility(View view) {
        // Ajustar el teclado para que no cubra los campos de texto
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 15% of screen
                    // Teclado visible
                    view.setPadding(0, 0, 0, keypadHeight);
                } else {
                    // Teclado oculto
                    view.setPadding(0, 0, 0, 0);
                }
            }
        });
    }

    private void registerUser() {
        // Validaciones previas (que ya tienes en el método validateAndRegister())
        int selectedUserType = spinnerUserType.getSelectedItemPosition();

        if (selectedUserType == 2) { // Agricultor
            // Crear la instancia de ApiService
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            // Crear el objeto de solicitud
            AgricultorRequest agricultor = new AgricultorRequest(
                    etNombre.getText().toString(),
                    etEmail.getText().toString(),
                    etDireccion.getText().toString(),
                    etTelefono.getText().toString(),
                    etNombreUsuario.getText().toString(), // Cambio de nombreUsuario a nombre_usuario
                    etContrasenia.getText().toString(),
                    "Verificado por AgroHub"  // Certificación predefinida
            );

            // Realizar la llamada a la API
            Call<AgricultorResponse> call = apiService.registrarAgricultor(agricultor);
            call.enqueue(new Callback<AgricultorResponse>() {
                @Override
                public void onResponse(Call<AgricultorResponse> call, Response<AgricultorResponse> response) {
                    if (response.isSuccessful()) {
                        // Registro exitoso
                        if (response.body() != null) {
                            // Puedes loguear o mostrar el mensaje de respuesta
                            Log.d("Registro", "Respuesta: " + response.body().getMessage());
                            Toast.makeText(requireContext(),
                                    response.body().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            // Navegar al login
                            new Handler().postDelayed(() -> {
                                NavHostFragment.findNavController(IniRegistroFragment.this)
                                        .navigate(R.id.action_iniRegistroFragment_to_iniLoginFragment);
                            }, 2000);
                        }
                    } else {
                        // Manejar errores de la API
                        try {
                            String errorBody = response.errorBody() != null
                                    ? response.errorBody().string()
                                    : "Error desconocido";

                            Log.e("Registro Error", "Código: " + response.code() + ", Mensaje: " + errorBody);

                            Toast.makeText(requireContext(),
                                    "Error: " + errorBody,
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("Registro Error", "Error al procesar respuesta", e);
                            Toast.makeText(requireContext(),
                                    "Error en el registro",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<AgricultorResponse> call, Throwable t) {
                    // Error de conexión
                    Log.e("Registro Error", "Fallo en la conexión", t);
                    Toast.makeText(requireContext(),
                            "Error de conexión: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else if (selectedUserType == 1) { // Consumidor
            // Crear la instancia de ApiService
            ApiService apiService = ApiClient.getClient().create(ApiService.class);

            // Crear el objeto de solicitud
            ConsumidorRequest consumidor = new ConsumidorRequest(
                    etNombre.getText().toString(),
                    etEmail.getText().toString(),
                    etDireccion.getText().toString(),
                    etTelefono.getText().toString(),
                    etNombreUsuario.getText().toString(),
                    etContrasenia.getText().toString(),
                    etPreferencias.getText().toString() // Obtener las preferencias del campo de texto
            );

            // Realizar la llamada a la API
            Call<ConsumidorResponse> call = apiService.registrarConsumidor(consumidor);
            call.enqueue(new Callback<ConsumidorResponse>() {
                @Override
                public void onResponse(Call<ConsumidorResponse> call, Response<ConsumidorResponse> response) {
                    if (response.isSuccessful()) {
                        // Registro exitoso
                        if (response.body() != null) {
                            // Puedes loguear o mostrar el mensaje de respuesta
                            Log.d("Registro", "Respuesta: " + response.body().getMessage());
                            Toast.makeText(requireContext(),
                                    response.body().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                            // Navegar al login
                            new Handler().postDelayed(() -> {
                                NavHostFragment.findNavController(IniRegistroFragment.this)
                                        .navigate(R.id.action_iniRegistroFragment_to_iniLoginFragment);
                            }, 2000);
                        }
                    } else {
                        // Manejar errores de la API
                        try {
                            String errorBody = response.errorBody() != null
                                    ? response.errorBody().string()
                                    : "Error desconocido";

                            Log.e("Registro Error", "Código: " + response.code() + ", Mensaje: " + errorBody);
                            Toast.makeText(requireContext(),
                                    "Error: " + errorBody,
                                    Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("Registro Error", "Error al procesar respuesta", e);
                            Toast.makeText(requireContext(),
                                    "Error en el registro",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ConsumidorResponse> call, Throwable t) {
                    // Error de conexión
                    Log.e("Registro Error", "Fallo en la conexión", t);
                    Toast.makeText(requireContext(),
                            "Error de conexión: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToLogin() {
        new Handler().postDelayed(() -> {
            NavHostFragment.findNavController(IniRegistroFragment.this)
                    .navigate(R.id.action_iniRegistroFragment_to_iniLoginFragment);
        }, 1500); // Mostrar el mensaje por 1.5 segundos antes de navegar
    }

    private void handleRegistrationError(Response<AgricultorResponse> response) {
        try {
            String errorMessage = "Error en el registro";
            if (response.errorBody() != null) {
                JSONObject errorBody = new JSONObject(response.errorBody().string());
                errorMessage = errorBody.optString("message1", errorMessage);
            }
            showErrorMessage(errorMessage);
        } catch (Exception e) {
            showErrorMessage("Error desconocido durante el registro");
        }
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+");
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{9}");
    }

    private void showErrorMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}