package sample.com.jobin.msi.projectgarbage;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String img_url,etitle,priority,str_name,str_city,str_address,str_street,str_date,bid_value,str_postid;
    TextView name,address,date,priotrity,title,city,street;
    EditText ed_bid;
    Button bid;
    private ImageView back2;
    private NetworkImageView imgurl;
    public static NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    public static String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_colinsbid.php?";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();

        Bundle extras = getIntent().getExtras();

        img_url = extras.getString("img_url");
        etitle = extras.getString("etitle");
        priority = extras.getString("priority");
        str_name = extras.getString("name");
        str_city = extras.getString("city");
        str_address = extras.getString("address");
        str_street = extras.getString("street");
        str_date = extras.getString("date");
        str_postid = extras.getString("post");

        name.setText(str_name);
        address.setText(str_address);
        date.setText(str_date);
        priotrity.setText(priority);
        title.setText(etitle);
        city.setText(str_city);
        street.setText(str_street);
        imgurl.setImageUrl(img_url, imageLoader);

        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bid_value = ed_bid.getText().toString();
                getData();
            }
        });



    }
    private void getData() {
        if (networkInfo == null){
            Toast.makeText(this, "no network connection", Toast.LENGTH_SHORT).show();
        }
        else {

            JsonArrayRequest request = new JsonArrayRequest(url + "email=" + email + "&postid=" + str_postid + "&bid=" + bid_value ,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    Toast.makeText(DetailActivity.this, obj.getString("bid"), Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            AppController.getInstance().addToRequestQueue(request);

        }

    }

    private void initViews() {
        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        email = pref.getString("email","");
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        date = findViewById(R.id.date);
        priotrity = findViewById(R.id.priority);
        title = findViewById(R.id.title);
        city = findViewById(R.id.city);
        street = findViewById(R.id.street);
        ed_bid = findViewById(R.id.ed_bid);
        bid = findViewById(R.id.bid);
        imgurl = findViewById(R.id.imgurl);
        back2 = findViewById(R.id.back2);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
