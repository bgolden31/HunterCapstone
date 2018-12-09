package com.example.kchu0.home;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    ProfileFragment profileFragment;
    SearchFragment searchFragment;
    HistoryFragment historyFragment;
    AddRecipeFragment addRecipeFragment;
    FavouriteFragment favouriteFragment;
    FridgeFragment fridgeFragment;
    ShoppingListFragment shoppingListFragment;
    PersonalRecipeFragment personalRecipeFragment;
    RecommendedFragment recommendedFragment;

    SessionManager session;
    HashMap<String, String> user_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        user_s = session.getUserDetails();

        //Initiate objects.
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black);

        //Display
        TextView user_display = (TextView)navigationView.getHeaderView(0).findViewById(R.id.Username);
        user_display.setText(user_s.get(SessionManager.KEY_USERNAME));
        TextView email_display = (TextView)navigationView.getHeaderView(0).findViewById(R.id.Email);
        email_display.setText(user_s.get(SessionManager.KEY_EMAIL));
        //

        set_default_frag();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_profile:
                        item.setChecked(true);
                        createProfile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_search:
                        item.setChecked(true);
                        createSearch();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_history:
                        item.setChecked(true);
                        createHistory();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, historyFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_addrecipe:
                        item.setChecked(true);
                        createAddRecipe();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, addRecipeFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_favourite:
                        item.setChecked(true);
                        createfavourite();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, favouriteFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_fridge:
                        item.setChecked(true);
                        createFridge();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fridgeFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_shopping_list:
                        item.setChecked(true);
                        createShoppinglist();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, shoppingListFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_recommended:
                        item.setChecked(true);
                        createRecommended();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, recommendedFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_personal_recipe:
                        item.setChecked(true);
                        createPersonal();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, personalRecipeFragment).commit();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_logout:
                        item.setChecked(true);
                        logout(navigationView);
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingFragment()).commit();
                        //drawerLayout.closeDrawers();
                        break;
                }
            return false;
            }

        });

    }

    /**
     * Used to make the NavDrawer Popup
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Used to make the deault fragment that the Main activity is located on.
     */
    public void set_default_frag() {
        createSearch();
        navigationView.setCheckedItem(R.id.nav_search);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment ).commit();
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createProfile() {
        profileFragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", user_s.get(SessionManager.KEY_NAME));
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        bundle.putString("email", user_s.get(SessionManager.KEY_EMAIL));
        bundle.putString("age", user_s.get(SessionManager.KEY_AGE));
        bundle.putString("password", user_s.get(SessionManager.KEY_PASS));
        profileFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createSearch() {
        searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", user_s.get(SessionManager.KEY_NAME));
        searchFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createHistory() {
        historyFragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        historyFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createfavourite(){
        favouriteFragment = new FavouriteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        favouriteFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createFridge() {
        fridgeFragment = new FridgeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        fridgeFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createShoppinglist() {
        shoppingListFragment = new ShoppingListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        shoppingListFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createAddRecipe() {
        addRecipeFragment = new AddRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        addRecipeFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createPersonal() {
        personalRecipeFragment = new PersonalRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        personalRecipeFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void createRecommended() {
        recommendedFragment = new RecommendedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", user_s.get(SessionManager.KEY_USERNAME));
        recommendedFragment.setArguments(bundle);
    }

    /**
     * Creates Fragment, and stores it with certain information required before each Nav Menu is selected
     */
    public void logout(View arg0) {
        // Clear the session data
        // This will clear all session data and
        // redirect user to LoginActivity
        session.logoutUser();
    }

    /**
     * Function to pervent the user from clicking back on this screen
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
