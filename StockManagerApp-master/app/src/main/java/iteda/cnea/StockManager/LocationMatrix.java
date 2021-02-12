package iteda.cnea.StockManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LocationMatrix extends AppCompatActivity {

    //Button A0I, A0II, A0III, A0IV, A0V, A0VI;
    Button A1, A2, A3, A4, A5, A6, A7, A8;
    //Button B0I, B0II, B0III, B0IV, B0V, B0VI;
    Button B1, B2, B3, B4, B5, B6, B7, B8;
    //Button C0I, C0II, C0III, C0IV, C0V, C0VI;
    Button C1, C2, C3, C4, C5, C6, C7, C8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_location_matrix );
/*----------------------------Columna A--------------------------------------------------------------------------------------------------------------------*/
  //      A0I = (Button) findViewById( R.id.A0I);
  //      A0II = (Button) findViewById( R.id.A0II);
  //      A0III = (Button) findViewById( R.id.AIII);
  //      A0IV = (Button) findViewById( R.id.AIV);
  //      A0V = (Button) findViewById( R.id.AV);
  //      A0VI = (Button) findViewById( R.id.AVI);
        A1 = findViewById( R.id.A1);
        A2 = findViewById( R.id.A2);
        A3 = findViewById( R.id.A3);
        A4 = findViewById( R.id.A4);
        A5 = findViewById( R.id.A5);
        A6 = findViewById( R.id.A6);
        A7 = findViewById( R.id.A7);
        A8 = findViewById( R.id.A8);

/*        A0I.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A0I: Logic", Toast.LENGTH_LONG ).show();
            }
        });
  */
        A1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A1: Resistencias SMD < 10k", Toast.LENGTH_LONG ).show();
            }
        });
        A2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A2: Resistencias SMD > 10k", Toast.LENGTH_LONG ).show();
            }
        });
        A3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A3: Resistencias SMD en rollos", Toast.LENGTH_LONG ).show();
            }
        });
        A4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A4: Resistencias", Toast.LENGTH_LONG ).show();
            }
        });
        A5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A5: Conectores (banana, cocodrilo, pines, etc) y separadores", Toast.LENGTH_LONG ).show();
            }
        });
        A6.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A6: Circuitos Integrados", Toast.LENGTH_LONG ).show();
            }
        });
        A7.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A7: Termocontraibles", Toast.LENGTH_LONG ).show();
            }
        });
        A8.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "A8: Jumpers, Pasacables, Kit Zócalos Tubos, Interlock y Filtros", Toast.LENGTH_LONG ).show();
            }
        });

/*----------------------------Columna B--------------------------------------------------------------------------------------------------------------------*/
        //      B0I = (Button) findViewById( R.id.B0I);
        //      A0II = (Button) findViewById( R.id.A0II);
        //      A0III = (Button) findViewById( R.id.AIII);
        //      A0IV = (Button) findViewById( R.id.AIV);
        //      A0V = (Button) findViewById( R.id.AV);
        //      A0VI = (Button) findViewById( R.id.AVI);
        B1 = findViewById( R.id.B1);
        B2 = findViewById( R.id.B2);
        B3 = findViewById( R.id.B3);
        B4 = findViewById( R.id.B4);
        B5 = findViewById( R.id.B5);
        B6 = findViewById( R.id.B6);
        B7 = findViewById( R.id.B7);
        B8 = findViewById( R.id.B8);
/*
        B0I.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B0I: Logic", Toast.LENGTH_LONG ).show();
            }
        });
*/
        B1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B1: Capacitores SMD", Toast.LENGTH_LONG ).show();
            }
        });
        B2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B2: Capacitores", Toast.LENGTH_LONG ).show();
            }
        });
        B3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B3: Muestras", Toast.LENGTH_LONG ).show();
            }
        });
        B4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B4: Pulsadores, Relays, Llaves y Fusibles", Toast.LENGTH_LONG ).show();
            }
        });
        B5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B5: Conectores DB y Borneras", Toast.LENGTH_LONG ).show();
            }
        });
        B6.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B6: Separadores, Arandelas y Tornillos", Toast.LENGTH_LONG ).show();
            }
        });
        B7.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B7: Impresora 3D", Toast.LENGTH_LONG ).show();
            }
        });
        B8.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "B8: Amplicadores", Toast.LENGTH_LONG ).show();
            }
        });

        /*----------------------------Columna B--------------------------------------------------------------------------------------------------------------------*/
        //      C0I = (Button) findViewById( R.id.C0I);
        //      A0II = (Button) findViewById( R.id.A0II);
        //      A0III = (Button) findViewById( R.id.AIII);
        //      A0IV = (Button) findViewById( R.id.AIV);
        //      A0V = (Button) findViewById( R.id.AV);
        //      A0VI = (Button) findViewById( R.id.AVI);
        C1 = findViewById( R.id.C1);
        C2 = findViewById( R.id.C2);
        C3 = findViewById( R.id.C3);
        C4 = findViewById( R.id.C4);
        C5 = findViewById( R.id.C5);
        C6 = findViewById( R.id.C6);
        C7 = findViewById( R.id.C7);
        C8 = findViewById( R.id.C8);
/*
        C0I.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C0I: Logic", Toast.LENGTH_LONG ).show();
            }
        });
  */
        C1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C1: Fuente", Toast.LENGTH_LONG ).show();
            }
        });
        C2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C2: Conectores RJ11 y RJ45", Toast.LENGTH_LONG ).show();
            }
        });
        C3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C3: Conectores SMA, SMB, TNC y BNC", Toast.LENGTH_LONG ).show();
            }
        });
        C4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C4: Semiconductores y otros", Toast.LENGTH_LONG ).show();
            }
        });
        C5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C5: Inductores", Toast.LENGTH_LONG ).show();
            }
        });
        C6.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C6: Conectores MTA", Toast.LENGTH_LONG ).show();
            }
        });
        C7.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C7: Terminales 100", Toast.LENGTH_LONG ).show();
            }
        });
        C8.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText( LocationMatrix.this, "C8: Circuitos Integrados Varios", Toast.LENGTH_LONG ).show();
            }
        });

    }
}
