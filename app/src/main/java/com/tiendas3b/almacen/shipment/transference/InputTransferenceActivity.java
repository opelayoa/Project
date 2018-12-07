package com.tiendas3b.almacen.shipment.transference;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.activities.InputTransferenceCaptureActivity;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.fragments.InputTransferenceFragment;

public class InputTransferenceActivity extends AppCompatActivity implements InputTransferenceFragment.OnFragmentInteractionListener {

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_transference_shipment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.bb_menu_recyclable :
                        changeMainContent(InputTransferenceFragment.newInstance((short) 2));
                        break;
                    case R.id.bb_menu_returnable :
                        changeMainContent(InputTransferenceFragment.newInstance((short) 1));
                        break;
//                    case R.id.bb_menu_io :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) 3));
//                        break;
//                    case R.id.bb_menu_rm :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) 5));
//                        break;
//                    case R.id.bb_menu_product :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) -1));
//                        break;
                    default:
                        break;
                }
            }
        });


//        mBottomBar = BottomBar.attach(this, savedInstanceState);
////        mBottomBar.noTopOffset();//cuando hay 4 o mas tabs
//        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu_transference, new OnMenuTabClickListener() {
//            @Override
//            public void onMenuTabSelected(@IdRes int menuItemId) {
//                switch (menuItemId){
//                    case R.id.bb_menu_recyclable :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) 2));
//                        break;
//                    case R.id.bb_menu_returnable :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) 1));
//                        break;
//                    case R.id.bb_menu_io :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) 3));
//                        break;
//                    case R.id.bb_menu_rm :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) 5));
//                        break;
//                    case R.id.bb_menu_product :
//                        changeMainContent(InputTransferenceFragment.newInstance((short) -1));
//                        break;
//                    default:
//                        break;
//                }
////                mBottomBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onMenuTabReSelected(@IdRes int menuItemId) {
//                if (menuItemId == R.id.bb_menu_product) {
//                    // The user reselected item number one, scroll your content to top.
//                }
//            }
//        });
        mBottomBar.setDefaultTabPosition(1);

        Fragment f = InputTransferenceFragment.newInstance((short) -1);
        changeMainContent(f);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        // Necessary to restore the BottomBar's state, otherwise we would
//        // lose the current tab on orientation change.
//        mBottomBar.onSaveInstanceState(outState);
    }

    private void changeMainContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(InputTransference transference) {
//        mBottomBar.hide();
//        InputTransferenceCaptureFragment fragment = InputTransferenceCaptureFragment.newInstance(transference.getId());
//        changeMainContent(fragment);
        startActivity(new Intent(this, InputTransferenceCaptureActivity.class).putExtra("t", transference.getId()));
    }
}
