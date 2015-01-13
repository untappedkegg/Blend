package com.untappedkegg.blend;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.untappedkegg.blend.conversations.ConversationFragment;
import com.untappedkegg.blend.conversations.ConversationFragment.OnConversationInteractionListener;
import com.untappedkegg.blend.thread.ThreadFragment;


public class ActivityMain extends Activity implements OnConversationInteractionListener, ThreadFragment.OnThreadInteractionListener {
    /*----- CONSTANTS -----*/
    private final String LOG_TAG = ActivityMain.class.getSimpleName();

    /*----- VARIABLES -----*/
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"Home", "Android", "Sitemap", "About", "Contact Me"};


    /*----- LIFECYCLE METHODS -----*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        leftDrawerList = (ListView) findViewById(R.id.left_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);

        initDrawer();


        if(savedInstanceState == null)  {
            attachFragment(new ConversationFragment(), false, null);
        }

//        MessageUtils.printMessagesToLog(MessagesAdapter.readThreadMessages("32"), true);
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

    /*----- CUSTOM METHODS -----*/
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

    private void attachFragment(Fragment fragment, boolean addToBackStack, Bundle args) {

        if (args != null) {
            fragment.setArguments(args);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container, fragment);
                        if (addToBackStack) {
                            ft.addToBackStack(fragment.getClass().getName());
                        }
                            ft.commit();

    }

    /*----- INHERITED INTERFACES -----*/
    @Override
    public void onConversationSelected(String name, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(AppState.KEY_MSG_NAME, name);
        bundle.putString(AppState.KEY_MSG_ID, id);
        attachFragment(new ThreadFragment(), true, bundle);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
