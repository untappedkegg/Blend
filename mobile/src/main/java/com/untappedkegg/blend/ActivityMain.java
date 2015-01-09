package com.untappedkegg.blend;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.untappedkegg.blend.ui.BaseRecycler;
import com.untappedkegg.blend.ui.adapter.RecyclerAdapter;
import com.untappedkegg.blend.utils.MessageUtils;


public class ActivityMain extends Activity {
//    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"Home", "Android", "Sitemap", "About", "Contact Me"};
    private static Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        this.setActionBar(toolbar);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);

//
//        if (toolbar != null) {
//            toolbar.setTitle(R.string.app_name);
//            this.setActionBar(toolbar);
//        }
        initDrawer();



        Uri uri = Uri.parse(MessageUtils.ALL);
         c= getContentResolver().query(uri, null, null, null, "date DESC");

        MessageUtils.printColumnsToLog(c, false);
        MessageUtils.printMessagesToLog(c, false);
        // Read the sms data and store it in the list
/*
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {

                Log.w(c.getString(0), c.getString(1) + " " + c.getString(2));


                c.moveToNext();
            }
        } else {
            Log.e(getString(R.string.app_name), "Cursor is empty, exiting");
            this.finish();
        }
        c.close();
*/

        if(savedInstanceState==null)

        {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }


    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item) || id == android.R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends BaseRecycler {

        public PlaceholderFragment() {
        }

        @Override
        protected RecyclerView.Adapter getAdapter() {
            return new RecyclerAdapter(c, R.layout.generic_card);
        }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_activity_main, container, false);
//            return rootView;
//        }
    }
}
