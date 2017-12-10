package sample.com.jobin.msi.projectgarbage.Karang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import sample.com.jobin.msi.projectgarbage.EwasteActivity;
import sample.com.jobin.msi.projectgarbage.Home.KarangHome;
import sample.com.jobin.msi.projectgarbage.Login.UserLogin;
import sample.com.jobin.msi.projectgarbage.MetalActivity;
import sample.com.jobin.msi.projectgarbage.OrganicActivity;
import sample.com.jobin.msi.projectgarbage.PlasticActivity;
import sample.com.jobin.msi.projectgarbage.R;

public class KarangPickup extends Fragment {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button logout;
    private ImageView ewaste,organic,plastic,metal;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_karangpickup, container, false);
        ewaste = (ImageView) view.findViewById(R.id.ewaste);
        organic = (ImageView) view.findViewById(R.id.organic);
        metal = (ImageView) view.findViewById(R.id.metal);
        plastic = (ImageView) view.findViewById(R.id.plastic);
        onClick(view);
        pref = getActivity().getSharedPreferences("Mypref",0);
        editor = pref.edit();
        editor.apply();
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("isLoggedin").apply();
                Toast.makeText(getActivity(), "succesfully logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserLogin.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void onClick(View view) {
        ewaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EwasteActivity.class);
                startActivity(intent);
            }
        });
        metal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MetalActivity.class);
                startActivity(intent);
            }
        });
        plastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlasticActivity.class);
                startActivity(intent);
            }
        });
        organic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrganicActivity.class);
                startActivity(intent);
            }
        });
    }
}
