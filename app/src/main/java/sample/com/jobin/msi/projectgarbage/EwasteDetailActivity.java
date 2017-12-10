package sample.com.jobin.msi.projectgarbage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class EwasteDetailActivity extends AppCompatActivity {
    String img_url,etitle,priority,name,city,address,street,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewaste_detail);

        Bundle extras = getIntent().getExtras();

        img_url = extras.getString("img_url");
        etitle = extras.getString("etitle");
        priority = extras.getString("priority");
        name = extras.getString("name");
        city = extras.getString("city");
        address = extras.getString("address");
        street = extras.getString("street");
        date = extras.getString("date");

    }
}
