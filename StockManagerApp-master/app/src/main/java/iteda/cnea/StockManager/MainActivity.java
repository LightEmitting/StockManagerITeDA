package iteda.cnea.StockManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class MainActivity extends AppCompatActivity {
//boton "cree una cuenta"
    EditText et_usuario, et_contraseña;
    TextView tv_registrar, tv_newpass;
    Button btn_log;

    public static final String MyPREFERENCES = "MyPrefs" ; //user id global
    public static final String userID = "userID";//user id global
    public static final String User = "user";
    SharedPreferences sharedPreferences;//user id global

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        tv_registrar = findViewById(R.id.tv_registrar);
        tv_newpass = findViewById( R.id.btn_NewPass );
        et_usuario = findViewById(R.id.tv_usuari);
        et_contraseña = findViewById(R.id.tv_contraseñ);
        btn_log = findViewById(R.id.btn_iniciar);

        new AlertDialog.Builder(MainActivity.this)  //AlertDialog momentario
                .setTitle("Atención")
                .setMessage("Por el momento la app solo funciona con la red wifi ITeDA2, disculpe las molestias")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


        et_usuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard( v );
                } else {
                    InputMethodManager imm;
                    imm = (InputMethodManager) MainActivity.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( et_usuario, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        et_contraseña.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm;
                    imm = (InputMethodManager) MainActivity.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( et_contraseña, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        tv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(MainActivity.this, Registration.class); //desde el activitymain llamo al activity registro
                MainActivity.this.startActivity(intentReg);
            }
        });

        tv_newpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentNewPass = new Intent(MainActivity.this, NewPass.class); //desde el activitymain llamo al activity registro
                MainActivity.this.startActivity(intentNewPass);
            }
        });

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);//user id global

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //para debudebuguear
                final String user = "prueba";
                final String pass ="prueba";
                Toast.makeText( MainActivity.this, "Debug Mode", Toast.LENGTH_LONG ).show();
/*
                final String user = et_usuario.getText().toString();
                final String pass = et_contraseña.getText().toString();
*/
                String password = null;
                String key = "92AE31A79FEEB2A3"; //llave
                String iv = "0123456789ABCDEF"; // vector de inicialización



                try {
                    password = StringEncrypt.encrypt(key, iv, pass);
                    password = password.substring( 0, 20 ); //reduzco el valor para que coincida con el de la database
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //si no funca el boton verificar ip
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (user.equals( "" )) {
                                Toast.makeText( MainActivity.this, "Complete el campo Usuario", Toast.LENGTH_LONG ).show();
                            } else if (pass.equals( "" )) {
                                Toast.makeText( MainActivity.this, "Complete el campo Contraseña", Toast.LENGTH_LONG ).show();
                            } else if (success) {
                                String id = jsonResponse.getString("userID");//user id global
                                SharedPreferences.Editor editor = sharedPreferences.edit();//user id global
                                editor.putString(userID, id);//user id global
                                editor.putString(User, user);
                                editor.apply();//user id global

                                Intent intent = new Intent(MainActivity.this, AccessToComp.class);
                                MainActivity.this.startActivity(intent);

                            } else {
                                Toast.makeText(MainActivity.this, "Error en el logueo", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText( MainActivity.this, "error", Toast.LENGTH_LONG ).show();
                            e.printStackTrace();
                        }
                    }
                    };
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText( MainActivity.this, "error:"+String.valueOf( error ), Toast.LENGTH_SHORT ).show();
                    }
                };
                //Toast.makeText( MainActivity.this, "prueba2", Toast.LENGTH_LONG ).show();

                LoginRequest loginRequest = new LoginRequest(user, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(loginRequest);
                loginRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
            }
        });
    }
    public void hideKeyboard(View view) {//hide soft keyboard after clicking outside EditText
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

class LoginRequest extends StringRequest {

    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String LOGIN_REQUEST_URL = "http://"+ip.ifconfig+"/Login.php"; //si no funca el boton verificar ip
    private Map<String, String> params;

    LoginRequest(String user, String password, Response.Listener<String> listener) {
        super( Request.Method.POST, LOGIN_REQUEST_URL, listener, null );
        params = new HashMap<>();
        params.put( "user", user );
        params.put( "password", password );
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}

class StringEncrypt {
    // Definición del tipo de algoritmo a utilizar (AES, DES, RSA)
    private final static String alg = "AES";
    // Definición del modo de cifrado a utilizar
    private final static String cI = "AES/CBC/PKCS5Padding";

    static String encrypt(String key, String iv, String cleartext) throws Exception {
        Cipher cipher = Cipher.getInstance( cI );
        SecretKeySpec skeySpec = new SecretKeySpec( key.getBytes(), alg );
        IvParameterSpec ivParameterSpec = new IvParameterSpec( iv.getBytes() );
        cipher.init( Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec );
        byte[] encrypted = cipher.doFinal( cleartext.getBytes() );
        return Base64.encodeToString( encrypted, 0 );
        //return new String(encodeBase64(encrypted));
    }
/*
    public static String decrypt(String key, String iv, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance( cI );
        SecretKeySpec skeySpec = new SecretKeySpec( key.getBytes(), alg );
        IvParameterSpec ivParameterSpec = new IvParameterSpec( iv.getBytes() );
        //byte[] enc = decodeBase64(encrypted);
        byte[] enc = Base64.decode( encrypted, 0 );
        cipher.init( Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec );
        byte[] decrypted = cipher.doFinal( enc );
        return new String( decrypted );
    }
*/
}