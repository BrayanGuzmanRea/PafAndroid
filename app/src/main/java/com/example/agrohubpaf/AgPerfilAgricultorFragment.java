package com.example.agrohubpaf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AgPerfilAgricultorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AgPerfilAgricultorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AgPerfilAgricultorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AgPerfilAgricultorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AgPerfilAgricultorFragment newInstance(String param1, String param2) {
        AgPerfilAgricultorFragment fragment = new AgPerfilAgricultorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_ag_perfil_agricultor, container, false);

        // Encontrar el botón de editar perfil
        Button btnEditProfile = view.findViewById(R.id.btn_edit_profile);

        // Encontrar el botón de cerrar sesión
        Button btnReturn = view.findViewById(R.id.btn_return);

        // Establecer el listener de clic para navegar al fragmento de edición
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar al fragmento de edición de agricultor
                Navigation.findNavController(v).navigate(R.id.action_agPerfilAgricultorFragment_to_agEditarAgricultorFragment);
            }
        });

        // Establecer el listener de clic para cerrar sesión
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar de vuelta a la pantalla de login
                Navigation.findNavController(v).navigate(R.id.action_agPerfilAgricultorFragment_to_iniLoginFragment);
            }
        });

        return view;
    }
}