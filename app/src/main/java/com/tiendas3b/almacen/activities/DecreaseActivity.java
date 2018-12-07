package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tiendas3b.almacen.R;

public class DecreaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrease);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Merma");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_decrease, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_decrease_insert) {
            startActivityForResult(new Intent(this, DecreaseCaptureActivity.class), REQUEST_CODE);
//            startActivity(new Intent(this, DecreaseCaptureActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE) {
//            if(resultCode == Activity.RESULT_OK){
////                String result=data.getStringExtra("result");
//                final Snackbar sb = Snackbar.make(findViewById(R.id.rltDecrease), "Merma guardada!", Snackbar.LENGTH_INDEFINITE);
//                sb.setAction("Aceptar", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sb.dismiss();
//                    }
//                }).show();
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
//    }
}
