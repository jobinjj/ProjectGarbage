package sample.com.jobin.msi.projectgarbage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sample.com.jobin.msi.projectgarbage.Login.UserLogin;
import sample.com.jobin.msi.projectgarbage.Register.UserRegister;

public class WelcomeScreen extends AppCompatActivity {
    private Button signin,signup;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        pref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
        initViews();
    }

    private void initViews() {
        signin = (Button) findViewById(R.id.signin);
        signup = (Button) findViewById(R.id.signup);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, UserLogin.class);
                startActivity(intent);
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeScreen.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
