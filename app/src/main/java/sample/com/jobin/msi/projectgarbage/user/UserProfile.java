package sample.com.jobin.msi.projectgarbage.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sample.com.jobin.msi.projectgarbage.AppController;
import sample.com.jobin.msi.projectgarbage.EditProfile;
import sample.com.jobin.msi.projectgarbage.R;
import sample.com.jobin.msi.projectgarbage.RoundedCornerNetworkImageView;


public class UserProfile extends Fragment implements OnMapReadyCallback {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    String email,latitude,longitude;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    RoundedCornerNetworkImageView profile_pic,doorimg;
    RelativeLayout body;
    double lat,lon;
    TextView address,textView8,age,sex,name,street,mobile;
    Button btn_edit;
    ImageView back;
    private String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_vsprofile.php?";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        pref = getActivity().getSharedPreferences("Mypref",0);
        editor = pref.edit();
        editor.apply();
        email = pref.getString("email",null);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViews(view);
        getData();

        return view;

    }

    private void initViews(View view) {

        profile_pic = view.findViewById(R.id.profile_pic);
        body = view.findViewById(R.id.body);

        textView8 = view.findViewById(R.id.textView8);
       address = view.findViewById(R.id.address);
        doorimg = view.findViewById(R.id.doorimg);
        btn_edit = view.findViewById(R.id.btn_edit);
        age = view.findViewById(R.id.age);
        name = view.findViewById(R.id.name);
        sex = view.findViewById(R.id.sex);
        mobile = view.findViewById(R.id.mobile);
            street = view.findViewById(R.id.street);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

    }

    private void getData() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {

        }else{


            JsonArrayRequest movieReqpublic = new JsonArrayRequest(url + "email=" + email,
                    new Response.Listener<JSONArray>(){
                        @Override
                        public void onResponse(JSONArray response) {



                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    profile_pic.setImageUrl(obj.getString("ppict"),imageLoader);
                                    doorimg.setImageUrl(obj.getString("doorimage"),imageLoader);
                                    name.setText(obj.getString("name"));
                                    age.setText(obj.getString("age") + " ");
                                    sex.setText(obj.getString("sex"));
                                    street.setText(obj.getString("street"));
                                    address.setText(obj.getString("address"));
                                    mobile.setText(obj.getString("mobilenumber"));
                                    latitude = obj.getString("latitude");
                                    longitude = obj.getString("longitude");
                                    lat=Double.parseDouble(latitude);
                                    lon=Double.parseDouble(longitude);
//                                    preferedday.setText(obj.getString("preferedday"));
//                                    preferedtime.setText(obj.getString("preferedtime"));


                                    if (address.getText().toString().isEmpty()){
                                        Toast.makeText(getActivity(), "is empty", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        body.setVisibility(View.VISIBLE);
                                        textView8.setVisibility(View.GONE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }

            });
            AppController.getInstance().addToRequestQueue(movieReqpublic);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
