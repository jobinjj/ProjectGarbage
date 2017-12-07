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
import sample.com.jobin.msi.projectgarbage.R;
import sample.com.jobin.msi.projectgarbage.RegisterActivity;
import sample.com.jobin.msi.projectgarbage.Home.UserHome;

public class UserLogin extends AppCompatActivity {
    Button signup,login;
    TextView user_txt,group_txt;
    EditText email,pass;

    String str_username,str_pass,str_request,str_type;
    NetworkInfo netInfo;
    private ProgressDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_login.php?";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
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
                Intent intent = new Intent(UserLogin.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkValidation(final String str_email, String str_pass) {
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
            JsonArrayRequest request = new JsonArrayRequest(url+"email="+ str_email + "&password=" + str_pass,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            pDialog.dismiss();
                            for (int i=0;i<response.length();i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    str_request = obj.getString("login");
                                    str_type = obj.getString("type");
                                    if (str_request.equals("success")) {
                                        Toast.makeText(getApplicationContext(), "succesfully login", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UserLogin.this,UserHome.class);
                                        Intent intent2 = new Intent(UserLogin.this,KarangHome.class);
                                        editor.putBoolean("isLoggedin",true);
                                        editor.putString("email",str_email);
                                        editor.apply();
                                        if (str_type.equals("user")){
                                            startActivity(intent);
                                            finish();
                                        }else if (str_type.equals("collector")){
                                            startActivity(intent2);
                                            finish();
                                        }else if (str_type.equals("fail"))
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
