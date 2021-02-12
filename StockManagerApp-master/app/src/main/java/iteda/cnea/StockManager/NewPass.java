package iteda.cnea.StockManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPass extends AppCompatActivity implements View.OnClickListener {

    Button ResPass1, ResPass2, ResPass3;
    EditText NewMail, code2, Pass1, Pass2;
    int RandomCodeSend1 = (int) (Math.random() * 99999) + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_pass );

        NewMail = findViewById( R.id.et_Mail );
        ResPass1 = findViewById( R.id.btn_ResPass1 );
        ResPass2 = findViewById( R.id.btn_ResPass2 );
        ResPass3 = findViewById( R.id.btn_ResPass3 );
        code2 = findViewById( R.id.EditT_code2 );
        Pass1 = findViewById( R.id.EditT_Pass1 );
        Pass2 = findViewById( R.id.EditT_Pass2 );

        ResPass1.setOnClickListener( this );
        ResPass2.setOnClickListener( this );
        ResPass3.setOnClickListener( this );

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled( true );//enables the up buttom in action bar (minSdkVersion is 11 or higher)
        }

        NewMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) NewPass.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( NewMail, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        code2.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) NewPass.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( code2, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        Pass1.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) NewPass.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( Pass1, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        Pass2.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) NewPass.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( Pass2, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        final String newwmail = NewMail.getText().toString();

        switch (v.getId()) {
            case R.id.btn_ResPass1:
                Response.Listener<String> respoListener2 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject( response );
                            boolean success = jsonResponse.getBoolean( "success" );

                            if (success && !(newwmail.equals( "" ))) {   //si el string es distinto de "" y si esta en la base de datos
                                String newwmail = NewMail.getText().toString();
                                //Creating SendMail object
                                SendMail sm = new SendMail( NewPass.this, newwmail, "Cambio de contraseña", "Tu código para el cambio de contraseña es: " + RandomCodeSend1 );
                                //Executing sendmail to send email
                                sm.execute();
                                Toast.makeText( NewPass.this, "Verificá tu casilla de correo", Toast.LENGTH_LONG ).show();
                                ResPass1.setEnabled( false );
                                code2.setVisibility( View.VISIBLE );
                                ResPass2.setVisibility( View.VISIBLE );
                            } else if (!success) {   //si esta en la base de datos
                                Toast.makeText( NewPass.this, "El email ingresado no corresponde a un usuario registrado", Toast.LENGTH_LONG ).show();
                                Toast.makeText( NewPass.this, "El email ingresado no corresponde a un usuario registrado", Toast.LENGTH_LONG ).show();
                            } else {
                                Toast.makeText( NewPass.this, "Ingresa tu mail", Toast.LENGTH_LONG ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                CompRequest compRequest = new CompRequest( newwmail, respoListener2 );
                RequestQueue queue = Volley.newRequestQueue( NewPass.this );
                queue.add( compRequest );
                compRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
                break;

            case R.id.btn_ResPass2:
                if (RandomCodeSend1 == Integer.valueOf( code2.getText().toString() )) {
                    ResPass2.setEnabled( false );
                    Pass1.setVisibility( View.VISIBLE );
                    Pass2.setVisibility( View.VISIBLE );
                    ResPass3.setVisibility( View.VISIBLE );

                } else {
                    Toast.makeText( NewPass.this, "Código incorrecto", Toast.LENGTH_LONG ).show();
                }

                break;

            case R.id.btn_ResPass3:
                String pass1 = Pass1.getText().toString();
                String encrypt = null;

                if (pass1.equals( Pass2.getText().toString() )) {

                    //encripto contraseña
                    String key = "92AE31A79FEEB2A3"; //llave
                    String iv = "0123456789ABCDEF"; // vector de inicialización
                    try {
                        encrypt = StringEncrypt.encrypt(key, iv,pass1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //update en base de datos
                    Response.Listener<String> respoListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                new JSONObject( response );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Toast.makeText( NewPass.this, "Tu contraseña se ha cambiado exitosamente", Toast.LENGTH_LONG ).show();

                    EmailRequest emailRequest = new EmailRequest( encrypt, newwmail, respoListener );
                    RequestQueue quee = Volley.newRequestQueue( NewPass.this );
                    quee.add( emailRequest );
                    emailRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request

                    Intent intent = new Intent( NewPass.this, MainActivity.class );
                    NewPass.this.startActivity( intent );
                    ResPass3.setEnabled( false );
                } else {
                    Toast.makeText( NewPass.this, "No hay coincidencia en las contraseñas", Toast.LENGTH_LONG ).show();
                }

                break;
        }
    }

    static class EmailRequest extends StringRequest {

        final private static AccessToComp.IP ip = new AccessToComp.IP();
        private static final String REGISTER_REQUEST_URL = "http://"+ip.ifconfig+"/UpdatePass.php";
        private Map<String, String> params;

        EmailRequest(String newPass, String email, Response.Listener<String> listener) {

            super( Method.POST, REGISTER_REQUEST_URL, listener, null );
            params = new HashMap<>();
            params.put( "newPass", newPass );
            params.put( "email", email );
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }


    static class CompRequest extends StringRequest {

        final private static AccessToComp.IP ip = new AccessToComp.IP();
        private static final String REGISTER_REQUEST_URL = "http://"+ip.ifconfig+"/UserFinder.php";
        private Map<String, String> params;

        CompRequest(String email, Response.Listener<String> listener) {
            super( Method.POST, REGISTER_REQUEST_URL, listener, null );
            params = new HashMap<>();
            params.put( "email", email );
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }
    public void hideKeyboard(View view) {//hide soft keyboard after clicking outside EditText
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService( Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}