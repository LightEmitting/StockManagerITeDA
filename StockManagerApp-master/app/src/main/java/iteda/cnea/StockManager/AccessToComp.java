package iteda.cnea.StockManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("Registered")

public class AccessToComp extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SurfaceView cameraPreview;
    //TextView TxtResult;
    TextView et_barcode;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    Button IngresoManual;
    boolean received = false;
    /*
    //example
        String authorization = "C9HE145HvA0I0CiepuWac1EtvIVE";  //access_token
        String client_id = "d5772660-5ab8-4b99-a453-b2f369a3c088";
        String URL = "https://api.digikey.com/services/barcode/v1/productbarcode/";
        String accept = "application/json";
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission( this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start( cameraPreview.getHolder() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.stop();
    }

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_access_comp );

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled( true );//enables the up buttom in action bar (minSdkVersion is 11 or higher)
        }



        //begin sidebar navigator
        mDrawerLayout = (DrawerLayout) findViewById( R.id.drawerLayout );
        mToggle = new ActionBarDrawerToggle( this, mDrawerLayout, R.string.Open, R.string.Close );

        mDrawerLayout.addDrawerListener( mToggle );
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        NavigationView navigationView = (NavigationView) findViewById( R.id.navigation_view );
        navigationView.setNavigationItemSelectedListener( this );

        //end sidebar navigator

        cameraPreview = (SurfaceView) findViewById( R.id.cameraPreview );
        //  txtResult = (TextView) findViewById( R.id.txtResult );  //info del componente

        //linkeo el boton "ingreso manual"
        et_barcode = (EditText) findViewById( R.id.EditT_barcode );
        IngresoManual = (Button) findViewById( R.id.btn_IngMan );

        et_barcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
//        txtResult.setText( barcode );

        IngresoManual.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String barcode = et_barcode.getText().toString(); //transformo a string
                if (barcode.equals( "" )) {//INGRESO: manualmente sin codigo
                    Intent aux1 = new Intent( AccessToComp.this, CompInfo.class ); //desde el activitymain llamo al activity registro
                    aux1.putExtra( "flag", "flag" );
                    AccessToComp.this.startActivity( aux1 );
                } else {   //INGRESO: manualmente con codigo
                    final DKvars dkvars = new DKvars();
                    com.android.volley.Response.Listener<String> respListener = new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String accept = "application/json";
                                JSONObject jsonResponse = new JSONObject( response );
                                dkvars.state = jsonResponse.getBoolean( "success" );

                                if (dkvars.state) {
                                    dkvars.url = jsonResponse.getString( "url" );
                                    dkvars.client_id = jsonResponse.getString( "client_id" );
                                    dkvars.access_token = jsonResponse.getString( "access_token" );
                                    dkvars.refresh_token = jsonResponse.getString( "refresh_token" );
                                    dkvars.client_secret = jsonResponse.getString( "client_secret" );
                                } else {
                                    Toast.makeText( AccessToComp.this, "error en request: dkvars.state = false", Toast.LENGTH_SHORT ).show();
                                }
                                //request get JSON component info
                                String jsonbarcodeURL = "https://api.digikey.com/services/barcode/v1/productbarcode/";
                                Request request = new Request.Builder()
                                        .url( jsonbarcodeURL + barcode )
                                        .get()
                                        .addHeader( "x-ibm-client-id", dkvars.client_id )
                                        .addHeader( "authorization", dkvars.access_token )
                                        .addHeader( "accept", accept )
                                        .build();

                                OkHttpClient client = new OkHttpClient();
                                Call call = client.newCall( request );
                                call.enqueue( new Callback() {

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Toast.makeText( AccessToComp.this, getResources().getString( R.string.networkFailed ), Toast.LENGTH_SHORT ).show();
                                    }

                                    @Override
                                    public void onResponse(Call call, final Response response) throws IOException {
                                        final String json = response.body().string();
                                        if(response.code() == 200){
                                            AccessToComp.this.runOnUiThread( new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intentA = new Intent( AccessToComp.this, CompInfo.class );
                                                    intentA.putExtra( "json", json ); //envio el json al CompInfo
                                                    AccessToComp.this.startActivity( intentA );
                                                }
                                            } );
                                        } else if(response.code() == 401){
                                            Toast.makeText( AccessToComp.this, "Error: 401", Toast.LENGTH_SHORT ).show();
                                        } else if(response.code() == 403) {
                                            Toast.makeText( AccessToComp.this, "Error: 403", Toast.LENGTH_SHORT ).show();
                                        } else if(response.code() == 405){
                                            Toast.makeText( AccessToComp.this, "Error: 405", Toast.LENGTH_SHORT ).show();
                                        } else if(response.code() == 429){
                                            Toast.makeText( AccessToComp.this, "Error: 429", Toast.LENGTH_SHORT ).show();
                                        } else {
                                            Toast.makeText( AccessToComp.this, "Error: 402", Toast.LENGTH_SHORT ).show();
                                        }
                                    }//end onResponse

                                } );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    getRequest getRequest = new getRequest( "Bearer", respListener );
                    RequestQueue queue = Volley.newRequestQueue( AccessToComp.this );
                    queue.add( getRequest );
                    getRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
                }
            }
        } );
        //txtResult.setText( barcode );
        //fin linkeo

        //detecta codigo DATA_MATRIX y CODE_128
        barcodeDetector = new BarcodeDetector.Builder( this )
                .setBarcodeFormats( Barcode.DATA_MATRIX | Barcode.CODE_128 | Barcode.PDF417 )
//              .setBarcodeFormats(Barcode.QR_CODE)
                .build();

//        public int format;

        cameraSource = new CameraSource
                .Builder( this, barcodeDetector )
                //.setRequestedPreviewSize( 640, 480 )
                .setAutoFocusEnabled( true )
                .build();

        //Add Event
        cameraPreview.getHolder().addCallback( new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions( AccessToComp.this,
                            new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID );
                    return;
                }
                try {
                    cameraSource.start( cameraPreview.getHolder() );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        } );

        barcodeDetector.setProcessor( new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            //seteo accion una vez que la camara detecta algun tipo de codigo
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();

                if (qrcodes != null && qrcodes.size() == 1 && !received) {
                    received = true;
                    //String code = qrcodes.valueAt( 0 ).displayValue;

                    //Create vibrate
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService( Context.VIBRATOR_SERVICE ); //creo servicio de vibración
                    assert vibrator != null;
                    vibrator.vibrate( 100 );   //tiempo en ms de vibrado


                    /*//muestra en pantalla el codigo
                    txtResult.setText( code );  //muestra en pantalla el codigo
*/

                    final DKvars dkvars = new DKvars();

                    com.android.volley.Response.Listener<String> respoListener1 = new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject( response );
                                dkvars.state = jsonResponse.getBoolean( "success" );

                                if (dkvars.state) {   //si NO esta en la base de datos
                                    dkvars.url = jsonResponse.getString( "url" );
                                    dkvars.client_id = jsonResponse.getString( "client_id" );
                                    dkvars.access_token = jsonResponse.getString( "access_token" );
                                    dkvars.refresh_token = jsonResponse.getString( "refresh_token" );
                                } else {
                                    Toast.makeText( AccessToComp.this, "error 2b", Toast.LENGTH_SHORT ).show();
                                }

                                String code = qrcodes.valueAt( 0 ).displayValue;
                                String accept = "application/json";
                                OkHttpClient client = new OkHttpClient();

                                if (qrcodes.valueAt( 0 ).format == Barcode.DATA_MATRIX || qrcodes.valueAt( 0 ).format == Barcode.PDF417) {
                                    code = code.replaceAll( "\036", "\u241E" ).replaceAll( "\035", "\u241D" ); //parcea valores

                                    MediaType mediaType = MediaType.parse( "application/json" );
                                    RequestBody body = RequestBody.create( mediaType, "{\"QRCode\":\"" + code + "\"}" );
                                    Request request = new Request.Builder()
                                            .url( "https://api.digikey.com/services/barcode/v1/productqrcode" )
                                            .post( body )
                                            .addHeader( "x-ibm-client-id", dkvars.client_id )
                                            .addHeader( "authorization", dkvars.access_token )
                                            .addHeader( "content-type", accept )
                                            .addHeader( "accept", accept )
                                            .build();

                                    Call call = client.newCall( request );

                                    call.enqueue( new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            Toast.makeText( AccessToComp.this, getResources().getString( R.string.networkFailed ), Toast.LENGTH_SHORT ).show();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            final String json = response.body().string();
                                            AccessToComp.this.runOnUiThread( new Runnable() {

                                                @Override
                                                public void run() {
                                                    Intent intentA = new Intent( AccessToComp.this, CompInfo.class );
                                                    intentA.putExtra( "json", json ); //envio el json al CompInfo
                                                    AccessToComp.this.startActivity( intentA );
                                                    received = false;
                                                }
                                            } );
                                        }

                                    } );
                                }

                                if (qrcodes.valueAt( 0 ).format == Barcode.CODE_128) {
                                    final String barcodeCodeString = qrcodes.valueAt( 0 ).displayValue;
                                    //int barcodeCodeInt = Integer.parseInt( barcodeCodeString );
                                    if( barcodeCodeString.length() > 6 ){   //es un barcode perteneciente a la DB de dk
                                        String jsonbarcodeURL = "https://api.digikey.com/services/barcode/v1/productbarcode/";
                                        Request request = new Request.Builder()
                                                .url( jsonbarcodeURL + code )
                                                .get()
                                                .addHeader( "x-ibm-client-id", dkvars.client_id )
                                                .addHeader( "authorization", dkvars.access_token )
                                                .addHeader( "accept", accept )
                                                .build();


                                        Call call = client.newCall( request );

                                        call.enqueue( new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                Toast.makeText( AccessToComp.this, getResources().getString( R.string.networkFailed ), Toast.LENGTH_SHORT ).show();
                                            }

                                            @Override
                                            public void onResponse(Call call, final Response response) throws IOException {
                                                if (response.code() == 200) {
                                                    final String json = response.body().string();
                                                    AccessToComp.this.runOnUiThread( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Intent intentB = new Intent( AccessToComp.this, CompInfo.class );
                                                            intentB.putExtra( "json", json );
                                                            AccessToComp.this.startActivity( intentB );
                                                            received = false;
                                                        }
                                                    } );
                                                } else {
                                                    //---------------
                                                    final com.android.volley.Response.Listener<String> respoListener = new com.android.volley.Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                        }
                                                    };
                                                    final AccessToComp.DKvars dkvars = new AccessToComp.DKvars();
                                                    final com.android.volley.Response.Listener<String> respListener = new com.android.volley.Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonResponse = new JSONObject( response );
                                                                dkvars.state = jsonResponse.getBoolean( "success" );

                                                                if (dkvars.state) {
                                                                    dkvars.url = jsonResponse.getString( "url" );
                                                                    dkvars.client_id = jsonResponse.getString( "client_id" );
                                                                    dkvars.access_token = jsonResponse.getString( "access_token" );
                                                                    dkvars.refresh_token = jsonResponse.getString( "refresh_token" );
                                                                    dkvars.client_secret = jsonResponse.getString( "client_secret" );

                                                                    APIRequest apiRequest = new APIRequest(dkvars.refresh_token, respoListener);
                                                                    RequestQueue queue = Volley.newRequestQueue(AccessToComp.this);
                                                                    queue.add(apiRequest);
                                                                    apiRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
                                                                    Intent intentReg = new Intent(AccessToComp.this, AccessToComp.class); //desde el activitymain llamo al activity registro
                                                                    AccessToComp.this.startActivity(intentReg);

                                                                    Toast.makeText(AccessToComp.this, "ERROR 2: Por Favor Intentá Nuevamente", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText( AccessToComp.this, "error en request: dkvars.state = false", Toast.LENGTH_SHORT ).show();
                                                                }
                                                            } catch (JSONException e1) {
                                                                e1.printStackTrace();
                                                            }
                                                        }
                                                    };
                                                    iteda.cnea.StockManager.getRequest getrequest = new iteda.cnea.StockManager.getRequest( "Bearer", respListener );
                                                    RequestQueue queue1 = Volley.newRequestQueue( AccessToComp.this );
                                                    queue1.add( getrequest );

                                                    //---------------

                                                    /*
                                                    AccessToComp.this.runOnUiThread( new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText( AccessToComp.this, "access_token: obsoleto", Toast.LENGTH_SHORT ).show();
                                                        }
                                                    } );*/
                                                }
                                            }//end onResponse
                                        } );
                                    }
                                    else{   //es un barcode perteneciente a la DB interna
                                        com.android.volley.Response.Listener<String> respoListener = new com.android.volley.Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {

                                                    JSONObject jsonResponse = new JSONObject(response);
                                                    boolean success = jsonResponse.getBoolean("success");

                                                    if (success) {
                                                        Intent aux2 = new Intent( AccessToComp.this, CompInfo.class ); //desde el activitymain llamo al activity registro
                                                        aux2.putExtra( "flag2", "flag2" );
                                                        aux2.putExtra( "location", jsonResponse.getString( "location" ));
                                                        aux2.putExtra( "description", jsonResponse.getString( "description" ));
                                                        aux2.putExtra( "quantity", jsonResponse.getString( "quantity" ));
                                                        aux2.putExtra( "m_number", jsonResponse.getString( "m_number" ));
                                                        aux2.putExtra( "id", jsonResponse.getString( "id" ));
                                                        aux2.putExtra( "dk_number", barcodeCodeString);
                                                        AccessToComp.this.startActivity( aux2 );

                                                    } else {
                                                        Toast.makeText( AccessToComp.this, "error 123", Toast.LENGTH_SHORT ).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        GLCRequest GLCRequest= new GLCRequest(barcodeCodeString, respoListener);
                                        RequestQueue queue = Volley.newRequestQueue(AccessToComp.this);
                                        queue.add(GLCRequest);
                                        GLCRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    //String Token_type = "Bearer";
                    getRequest getRequest = new getRequest( "Bearer", respoListener1 );
                    RequestQueue queue = Volley.newRequestQueue( AccessToComp.this );
                    queue.add( getRequest );
                    getRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
                }//end run
            }
        } );
    }

    //begin sidebar navigator
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected( item )) {
            return true;
        } else {
            return super.onOptionsItemSelected( item );
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {   //conecto boton del sidebar con sus correspondientes funciones
        switch (item.getItemId()) {
            case R.id.nav_stock:
                Intent stock = new Intent( AccessToComp.this, Stock.class );
                AccessToComp.this.startActivity( stock );
            break;
            case R.id.nav_alert:
                Intent alert = new Intent( AccessToComp.this, Alert.class );
                AccessToComp.this.startActivity( alert );
                break;
            case R.id.nav_location_matrix:
            Intent lm = new Intent( AccessToComp.this, LocationMatrix.class );
            AccessToComp.this.startActivity( lm );
            break;
            case R.id.nav_sug:
            Intent suge = new Intent( AccessToComp.this, Suggestions.class );
            AccessToComp.this.startActivity( suge );
            break;
            case R.id.nav_status:
            Intent status = new Intent( AccessToComp.this, status.class );
            AccessToComp.this.startActivity( status );
            break;
            case R.id.nav_logout:
            Intent out = new Intent( AccessToComp.this, MainActivity.class );
            AccessToComp.this.startActivity( out );
            break;
            default:
                Toast.makeText( this, "Error: 001", Toast.LENGTH_SHORT ).show();
                break;
        }
        return false;
    }
//end sidebar navigator

    static class IP {
        //ITeDA1 172.18.0.206
        //ITeDA2 172.18.32.220
//        public String ifconfig = "172.18.32.220";   //iteda2
        public String ifconfig = "172.18.32.23";   //iteda2 - cambio no se xq
        //public String ifconfig = "172.18.0.206";    //iteda1
        //public String ifconfig = "172.18.0.204";   //ethernet
    }

    static class DKvars {
        public Boolean state = false;
        public String url = null;
        public String client_id = null;
        public String access_token = null;
        public String refresh_token = null;
        public String client_secret = null;
    }

    static class getRequest extends StringRequest {
        final private static IP ip = new IP();
        private static final String REGISTER_REQUEST_URL = "http://"+ip.ifconfig+"/TokenFinder.php";
        private Map<String, String> params;

        getRequest(String Token_type, com.android.volley.Response.Listener<String> listener) {
            super( Method.POST, REGISTER_REQUEST_URL, listener, null );
            params = new HashMap<>();
            params.put( "token_type", Token_type );
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    static class DBRequest extends StringRequest {
        final private static IP ip = new IP();
        private static final String REGISTER_REQUEST_URL = "http://"+ip.ifconfig+"/UpdateQuantity2.php";
        private Map<String, String> params;

        public DBRequest(String access_token_next, String refresh_token_next, String refresh_token_reg, com.android.volley.Response.Listener<String> listener) {

            super( Method.POST, REGISTER_REQUEST_URL, listener, null );
            params = new HashMap<>();
            params.put( "access_token", access_token_next );
            params.put( "refresh_token", refresh_token_next );
            params.put( "refresh_token", refresh_token_reg );
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }

    static class GLCRequest extends StringRequest {
        final private static IP ip = new IP();
        private static final String REGISTER_REQUEST_URL = "http://"+ip.ifconfig+"/GetLocalComponent.php";
        private Map<String, String> params;

        public GLCRequest(String barcode, com.android.volley.Response.Listener<String> listener) {

            super( Method.POST, REGISTER_REQUEST_URL, listener, null );
            params = new HashMap<>();
            params.put( "barcode", barcode );
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