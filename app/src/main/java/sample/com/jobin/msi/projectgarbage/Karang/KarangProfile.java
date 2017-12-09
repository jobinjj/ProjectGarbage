package sample.com.jobin.msi.projectgarbage.Karang;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sample.com.jobin.msi.projectgarbage.AppController;
import sample.com.jobin.msi.projectgarbage.KarangEditProfile;
import sample.com.jobin.msi.projectgarbage.R;


public class KarangProfile extends Fragment {
    private String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_colsprofile.php?";
    private TextView address,name,mobile,txt_prefered_location,txt_email;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String email;
    private NetworkImageView profile_pic,id_proof;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_karangprofile, container, false);
        pref = getActivity().getSharedPreferences("Mypref",0);
        editor = pref.edit();
        editor.apply();
        email = pref.getString("email",null);
        address = view.findViewById(R.id.addresskarang);
        name = view.findViewById(R.id.namekarang);
        mobile = view.findViewById(R.id.mobilekarang);
        profile_pic = view.findViewById(R.id.profile_pic);
        id_proof = view.findViewById(R.id.id_proof);
        txt_prefered_location = view.findViewById(R.id.txt_prefered_location);
        txt_email = view.findViewById(R.id.txt_emailkarang);
        getData();

//        gotoedit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), KarangEditProfile.class);
//                startActivity(intent);
//            }
//        });
        return view;
    }

    private void getData() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {

        }else{


            JsonArrayRequest movieReqpublic = new JsonArrayRequest(url + "email=" + "karang@gmail.com",
                    new Response.Listener<JSONArray>(){
                        @Override
                        public void onResponse(JSONArray response) {



                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    profile_pic.setImageUrl(obj.getString("ppict"),imageLoader);
                                    id_proof.setImageUrl(obj.getString("idproof"),imageLoader);
//                                    String msg = obj.getString("message");
//                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    name.setText(obj.getString("name"));
//                                    Toast.makeText(getActivity(), name.getText().toString(), Toast.LENGTH_SHORT).show();
                                    address.setText(obj.getString("address"));
                                    mobile.setText(obj.getString("mobilenumber"));
                                    txt_prefered_location.setText(obj.getString("street"));
                                    txt_email.setText(obj.getString("email"));

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

}
