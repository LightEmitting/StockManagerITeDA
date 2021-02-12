package iteda.cnea.StockManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.view.WindowManager;

public class splash extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView( R.layout.activity_splash);

        int DURACION_SPLASH = 1000; //ms
        new Handler().postDelayed( new Runnable(){
            public void run(){
                Intent intent = new Intent(splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, DURACION_SPLASH );
    }

}
