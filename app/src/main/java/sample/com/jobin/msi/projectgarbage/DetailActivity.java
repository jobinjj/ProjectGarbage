package sample.com.jobin.msi.projectgarbage;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String img_url,etitle,priority,str_name,str_city,str_address,str_street,str_date,bid_value,str_postid;
    TextView name,address,date,priotrity,title,city,street;
    EditText ed_bid;
    Button bid;
    private NetworkImageView imgurl;
    public static NetworkInfo networkInfo;
    ConnectivityManager connectivityManager;
    public static String url = "http://searchdeal.online/webtemplate/garbagecollector/garbage_colinsbid.php?";
    private SharedPreferences pref;
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
        bid_value = ed_bid.getText().toString();


    }

    private void initViews() {
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

    }
}
