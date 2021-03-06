package sample.com.jobin.msi.projectgarbage.Register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sample.com.jobin.msi.projectgarbage.AppController;
import sample.com.jobin.msi.projectgarbage.Home.UserHome;
import sample.com.jobin.msi.projectgarbage.HomeActivity;
import sample.com.jobin.msi.projectgarbage.Login.KarangLogin;
import sample.com.jobin.msi.projectgarbage.Login.UserLogin;
import sample.com.jobin.msi.projectgarbage.R;

public class UserRegister extends Fragment {
    Button register,login;
    private ProgressDialog pDialog;
    EditText username,email,pass,phone;
    ImageView ic_user,ic_group;
    TextView user_txt,group_txt;
    NetworkInfo netInfo;
    String str_request,str_alreadyExist;
    SharedPreferences pref;
    String usertype;
    SharedPreferences.Editor editor;
    private static final String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_register_user.php?";
    Context context;

    public UserRegister(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_user_register, container, false);
        pref = getActivity().getSharedPreferences("Mypref",0);
        editor = pref.edit();
        editor.apply();

        getView(view);
        onClick();
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void onClick() {
//        user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ic_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_select));
//                user_txt.setTextColor(getResources().getColor(R.color.appcolor));
//                ic_group.setImageDrawable(getResources().getDrawable(R.drawable.ic_group));
//                group_txt.setTextColor(getResources().getColor(R.color.black));
//                Intent intent = new Intent(getActivity(),UserLogin.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
//        collector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ic_group.setImageDrawable(getResources().getDrawable(R.drawable.ic_group_select));
//                group_txt.setTextColor(getResources().getColor(R.color.appcolor));
//                ic_user.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
//                user_txt.setTextColor(getResources().getColor(R.color.black));
//                Intent intent = new Intent(getActivity(),KarangLogin.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),UserLogin.class);
                startActivity(intent);
            }
        });
    }
    private void getData() {
        ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Toast.makeText(getActivity(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }
        else{
            String str_username = username.getText().toString();
            final String str_email = email.getText().toString();
            final String str_pass = pass.getText().toString();
            final String str_phone = phone.getText().toString();

            pDialog = new ProgressDialog(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
            pDialog.setMessage("Registering Please wait");
            pDialog.show();
            pDialog.setCanceledOnTouchOutside(true);
            JsonArrayRequest request = new JsonArrayRequest(url+"username="+ str_username + "&mobilenumber=" + str_phone + "&email=" + str_email + "&password=" + str_pass,
                    new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    pDialog.dismiss();
                    for (int i=0;i<response.length();i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            str_request = obj.getString("register");
                            str_alreadyExist = obj.getString("register");

                            if (str_request.equals("success")) {
                                Toast.makeText(getActivity(), "succesfully registered", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),UserHome.class);
                                editor.putBoolean("isLoggedin",true);
                                editor.putString("email",str_email);
                                editor.apply();
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "failed to register", Toast.LENGTH_SHORT).show();
                                editor.putBoolean("isLoggedin",false);
                                editor.apply();
                                Toast.makeText(getActivity(), str_alreadyExist, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                }
            });
            AppController.getInstance().addToRequestQueue(request);

        }

    }
    private void getView( View view) {
        register = (Button) view.findViewById(R.id.register);
        login = (Button) view.findViewById(R.id.login);
        username = (EditText) view.findViewById(R.id.username);
        email = (EditText) view.findViewById(R.id.email);
        pass = (EditText) view.findViewById(R.id.pass);
        phone = (EditText) view.findViewById(R.id.phone);
    }
}
