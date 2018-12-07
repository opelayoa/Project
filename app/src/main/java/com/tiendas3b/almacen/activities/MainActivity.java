package com.tiendas3b.almacen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tiendas3b.almacen.BuildConfig;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.fragments.AuditFragment;
import com.tiendas3b.almacen.fragments.InputTransferenceCaptureFragment;
import com.tiendas3b.almacen.fragments.MainFragment;
import com.tiendas3b.almacen.fragments.ReceiptFragment;
import com.tiendas3b.almacen.fragments.TransferenceOutputFragment;
import com.tiendas3b.almacen.sync.SyncUtils;
import com.tiendas3b.almacen.util.Preferences;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener/*, AuditFragment.OnFragmentInteractionListener*/ {


    private IDatabaseManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Answers.getInstance().logCustom(new CustomEvent("MainActivity").putCustomAttribute("Main", "onCreate"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView lblMail = navigationView.getHeaderView(0).findViewById(R.id.lbl_mail);
        Preferences p = new Preferences(this);
        lblMail.setText(p.getSharedStringSafe(Preferences.KEY_REGION, getString(R.string.caption)) + "/" + p.getSharedStringSafe(Preferences.KEY_LOGIN, getString(R.string.caption)));

        TextView lbl = navigationView.getHeaderView(0).findViewById(R.id.lbl_username);
        lbl.setText("v.".concat(BuildConfig.VERSION_NAME));



        changeMainContent(new MainFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            back();
        }
    }

    private void back() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.nav_receipt) {
//            changeMainContent(new ReceiptFragment());
//        } else if (id == R.id.nav_picking) {
//            changeMainContent(new PickingFragment());
//        } else if (id == R.id.nav_audit) {
//            changeMainContent(new AuditFragment());
//        } else if (id == R.id.nav_shipment) {
//            changeMainContent(new ShipmentFragment());
//        } else if (id == R.id.nav_miscellaneous) {
//            changeMainContent(new MiscellaneousFragment());
////            startActivity(new Intent(this, BarcodeActivity.class));
//        } else
            if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_sing_out) {
            singOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void singOut() {
        Preferences preferences = new Preferences(this);
        preferences.deleteData();
        IDatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.dropDatabase();
        databaseManager.closeDbConnections();
        SyncUtils.deleteSyncAccount(this);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.content_frame, fragment)
//                .commit();
//
//        // Highlight the selected item, update the title, and close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void changeMainContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

//    /**
//     *
//     * @param btnOptions
//     */
//    @Override
//    public void showPopupMenu(Button btnOptions) {
//        PopupMenu popup = new PopupMenu(this, btnOptions);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.transference_popup_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.action_input:
//                        Intent intentTicketDetail = new Intent(MainActivity.this, ConstructionActivity.class);
//                        startActivity(intentTicketDetail);
//                        break;
//                    case R.id.action_output:
//                        Intent intentTicketDetailEdit = new Intent(MainActivity.this, OutputTransferenceActivity.class);
//                        startActivity(intentTicketDetailEdit);
//                        break;
//                }
//                return true;
//            }
//        });
//        popup.show();
//    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int resultResId = R.string.error_message_not_found;
        if (requestCode == AuditFragment.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                resultResId = data.getIntExtra(TransferenceOutputFragment.RESULT, R.string.error_message_not_found);
            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
        } else if (requestCode == ReceiptFragment.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                resultResId = data.getIntExtra(InputTransferenceCaptureFragment.RESULT, R.string.error_message_not_found);
            }
        } else if (requestCode == ReceiptFragment.RQ_RETURN_VENDOR) {
            if (resultCode == Activity.RESULT_OK) {
                resultResId = data.getIntExtra(InputTransferenceCaptureFragment.RESULT, R.string.error_message_not_found);
            }
        }

        if (R.string.error_message_not_found != resultResId) {
            final Snackbar sb = Snackbar.make(findViewById(R.id.content), resultResId, Snackbar.LENGTH_INDEFINITE);//TODO validar R.id.content
            sb.setAction("Aceptar", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sb.dismiss();
                }
            }).show();
        }
    }
}
