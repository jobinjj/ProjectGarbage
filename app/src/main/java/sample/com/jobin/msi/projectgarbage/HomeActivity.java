package sample.com.jobin.msi.projectgarbage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import sample.com.jobin.msi.projectgarbage.Login.UserLogin;

public class HomeActivity extends AppCompatActivity {
    private Button logout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        initViews();
    }

    private void initViews() {
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("isLoggedin").apply();
                Toast.makeText(HomeActivity.this, "succesfully logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
