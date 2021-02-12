package iteda.cnea.StockManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class Registration extends AppCompatActivity implements View.OnClickListener {

    EditText etnombre, etapellido, etusuario, etcontraseña1, etcontraseña2, etemail, etcode;
    Button btn_pre_registrar, btn_registrar;
    int RandomCodeSend = (int) (Math.random() * 99999) + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registration );
/*
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );    //enables the up buttom in action bar (minSdkVersion is below)
        }
*/
        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled( true );//enables the up buttom in action bar (minSdkVersion is 11 or higher)
        }

        etnombre = findViewById( R.id.EditT_nombre );
        etapellido = findViewById( R.id.EditT_apellido );
        etusuario = findViewById( R.id.EditT_usuario );
        etcontraseña1 = findViewById( R.id.EditT_contraseña1 );
        etcontraseña2 = findViewById( R.id.EditT_contraseña2 );
        etemail = findViewById( R.id.EditT_email );
        etcode = findViewById( R.id.EditT_code );
        btn_pre_registrar = findViewById( R.id.Btn_PreRegistro );
        btn_registrar = findViewById( R.id.Btn_registrar );

        btn_pre_registrar.setOnClickListener( this );
        btn_registrar.setOnClickListener( this );

        etnombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) Registration.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etnombre, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        etapellido.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) Registration.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etapellido, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        etusuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) Registration.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etusuario, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        etcontraseña1.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) Registration.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etcontraseña1, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        etcontraseña2.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) Registration.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etcontraseña2, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        etemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) Registration.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etemail, InputMethodManager.SHOW_IMPLICIT );
                }
            }

        });

        etcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {  //hide soft keyboard after clicking outside EditText
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                } else {
                    InputMethodManager imm = (InputMethodManager) Registration.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etcode, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        final String name = etnombre.getText().toString();
        final String familyname = etapellido.getText().toString();
        final String user = etusuario.getText().toString();
        final String email = etemail.getText().toString();
        final String auxPas1 = etcontraseña1.getText().toString();
        final String auxPas2 = etcontraseña2.getText().toString();
        String password;
        String encrypt = null;
        final String RandomCodeReceive = etcode.getText().toString();   //string del codigo enviado por el usuario
        //gereno valor random en el rango [0 - 99999]:

        if (auxPas1.equals( auxPas2 )) {    //si las contraseñas son correctas, encripto la contraseña y la guardo en passencrip
            password = etcontraseña1.getText().toString();

            String key = "92AE31A79FEEB2A3"; //llave
            String iv = "0123456789ABCDEF"; // vector de inicialización
            try {
                encrypt = StringEncrypt.encrypt(key, iv, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        switch (v.getId()){
            case R.id.Btn_PreRegistro:
                final String finalPassword = encrypt;
                Response.Listener<String> respoListener2 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject( response );
                            boolean success = jsonResponse.getBoolean( "success" );

                            //chequeo: campos completos
                            if (name.equals( "" )) {
                                Toast.makeText( Registration.this, "Complete el campo Nombre", Toast.LENGTH_LONG ).show();
                            } else if (familyname.equals( "" )) {
                                Toast.makeText( Registration.this, "Complete el campo Apellido", Toast.LENGTH_LONG ).show();
                            } else if (user.equals( "" )) {
                                Toast.makeText( Registration.this, "Complete el campo Usuario", Toast.LENGTH_LONG ).show();
                            } else if (finalPassword == null) {
                                Toast.makeText( Registration.this, "No hay coincidencia en las Contraseñas", Toast.LENGTH_LONG ).show();
                            } else if (finalPassword.equals( "" )) {
                                Toast.makeText( Registration.this, "Complete el campo Contraseña", Toast.LENGTH_LONG ).show();
                            } else if (email.equals( "" )) {
                                Toast.makeText( Registration.this, "Complete el campo Email", Toast.LENGTH_LONG ).show();
                            } else if (success) {  //si el mail ya existe en la base de datos
                                Toast.makeText( Registration.this, "El mail ingresado ya existe en nuestra base de datos", Toast.LENGTH_LONG ).show();
                            } else {
                                //envio email codigo de verificacion
                                SendMail sendm = new SendMail( Registration.this , email, "Confirmar Email", "Tu clave de confirmación es: "+RandomCodeSend);
                                sendm.execute();

                                Toast.makeText( Registration.this, "Por favor chequeá tu email o spam para completar el regitro", Toast.LENGTH_LONG ).show();
                                etcode.setVisibility( View.VISIBLE );
                                btn_registrar.setVisibility( View.VISIBLE );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                CompRequest compRequest = new CompRequest( email, respoListener2 );
                RequestQueue qu = Volley.newRequestQueue( Registration.this );
                qu.add( compRequest );
                compRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //obliga a que envie un solo email

                break;

            case R.id.Btn_registrar:
                if(RandomCodeReceive.equals( "" ) || !RandomCodeReceive.matches( "\\d+" )){
                    Toast.makeText( this, "Complete el código que se encuentra en su casilla de mail, revise en spam", Toast.LENGTH_LONG ).show();
                }
                else if(RandomCodeSend == Integer.valueOf(RandomCodeReceive)){
                    Response.Listener<String> respoListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject( response );
                                boolean success = jsonResponse.getBoolean( "success" );

                                if(success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder( Registration.this );
                                    builder.setMessage( "Te registraste exitosamente" ).setPositiveButton( "ok", null ).create().show();
                                    Intent intent = new Intent( Registration.this, MainActivity.class );
                                    Registration.this.startActivity( intent );
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder( Registration.this );
                                    builder.setMessage( "Error en el registro" ).setPositiveButton( "Retry", null ).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegistryRequest registryRequest = new RegistryRequest( name, familyname, user, encrypt, email, respoListener );
                    RequestQueue queue = Volley.newRequestQueue( Registration.this );
                    queue.add( registryRequest );
                    registryRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //obliga a que envie un solo email
                    //envio email "cuenta creada correctamente"
                    SendMail sm = new SendMail( Registration.this , email, "Cuenta creada correctamente", "Hola "+ user +": Tu cuenta en Control de Stock de iteda ha sido creada exitosamente. En ella podrás acceder al stock, editar sus cantidades, descripción y ubicación; informar sobre bugs o posibles mejoras, entre otras cosas. Esperamos que sea de tu agrado. ");
                    sm.execute(  );
                }
                else{
                    Toast.makeText( this, "Código incorrecto", Toast.LENGTH_LONG ).show();
                }
                break;
            default:
                Toast.makeText( this, "Error", Toast.LENGTH_LONG ).show();
                break;
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
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

    class RegistryRequest extends StringRequest {
        final private static AccessToComp.IP ip = new AccessToComp.IP();
        private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/Register.php";
    private Map<String, String> params;
    RegistryRequest(String name, String familyname, String user, String password, String email, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("familyname", familyname);
        params.put("user", user);
        params.put("password", password);
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
