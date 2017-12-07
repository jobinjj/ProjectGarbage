package sample.com.jobin.msi.projectgarbage.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sample.com.jobin.msi.projectgarbage.AppController;
import sample.com.jobin.msi.projectgarbage.Home.KarangHome;
import sample.com.jobin.msi.projectgarbage.Register.KarangRegister;
import sample.com.jobin.msi.projectgarbage.R;

public class KarangLogin extends AppCompatActivity {
    Button signup,login;
    TextView user_txt,group_txt;
    SharedPreferences pref;
    EditText email,pass;
    private ProgressDialog pDialog;
    NetworkInfo netInfo;
    String str_username,str_pass,str_request;
    private static final String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_collection_collectorlogin.php?";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karang_login);
        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        getView();
        onClick();
    }
    private void getView() {
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);

    }
    public  void onClick(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_username = email.getText().toString();
                str_pass = pass.getText().toString();
                checkValidation(str_username,str_pass);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KarangLogin.this,KarangRegister.class);
                startActivity(intent);
            }
        });
    }
    private void checkValidation(String str_email, String str_pass) {
        ConnectivityManager conMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();

        }
        else{
            pDialog = new ProgressDialog(new ContextThemeWrapper(this, R.style.AppTheme));
            pDialog.setMessage("Logging in Please wait");
            pDialog.show();
            pDialog.setCanceledOnTouchOutside(true);
            JsonArrayRequest request = new JsonArrayRequest(url+"email="+ str_username + "&password=" + str_pass,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            pDialog.dismiss();
                            for (int i=0;i<response.length();i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    str_request = obj.getString("login");
                                    if (str_request.equals("success")) {
                                        Toast.makeText(getApplicationContext(), "succesfully login", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(KarangLogin.this,KarangHome.class);
                                        editor.putBoolean("isLoggedin",true);
                                        editor.apply();
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        editor.putBoolean("isLoggedin",false);
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), "failed to login", Toast.LENGTH_SHORT).show();
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

}
