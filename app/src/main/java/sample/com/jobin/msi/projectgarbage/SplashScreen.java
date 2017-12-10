package sample.com.jobin.msi.projectgarbage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sample.com.jobin.msi.projectgarbage.Home.KarangHome;
import sample.com.jobin.msi.projectgarbage.Home.UserHome;
import sample.com.jobin.msi.projectgarbage.Login.UserLogin;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        pref = getApplicationContext().getSharedPreferences("Mypref", MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (String.valueOf(pref.getBoolean("isLoggedin", false)).equals("false")) {
                    Intent intent = new Intent(SplashScreen.this, WelcomeScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, UserHome.class);
                    Intent intent2 = new Intent(SplashScreen.this, KarangHome.class);
                    String type = pref.getString("type", "collector");
                    if (type.equals("user")) {
                        startActivity(intent);
                        finish();
                    } else if (type.equals("collector")) {
                        startActivity(intent2);
                        finish();

                    }
                }
            }
        }, 2000);

    }
}
