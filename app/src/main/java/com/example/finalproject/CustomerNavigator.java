package com.example.finalproject;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class CustomerNavigator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout root_layout;
    FragmentManager fragmentManager;
    SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout);
        if (!sharedPreferencesManager.getSignedIn()) {
            try {
                Cursor cursor = MyApplication.getDatabaseManager().customerInfo(sharedPreferencesManager.getEmail());

                if (cursor != null && cursor.moveToFirst()) {
                    // Retrieve and process data from the cursor
                    @SuppressLint("Range") String customerEmail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_EMAIL));
                    @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_FIRST_NAME));
                    @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_LAST_NAME));
                    @SuppressLint("Range") String passwordHashed = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_PASSWORD_HASHED));
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_PHONE_NUMBER));
                    @SuppressLint("Range") String country = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_COUNTRY));
                    @SuppressLint("Range") String city = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_CITY));
                    @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_GENDER));

                    // Ensure that integer columns have valid values
                    @SuppressLint("Range") int isAdmin = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_ADMIN));
                    // Save the retrieved information into SharedPreferences
                    sharedPreferencesManager.saveUserInfo(customerEmail, firstName, lastName, passwordHashed,
                            phoneNumber, country, city, gender, isAdmin);
                } else {
                    // Handle the case where no matching record is found
                    Toast.makeText(this, "No customer found for the signed-in email", Toast.LENGTH_SHORT).show();
                }

                // Close the cursor when done
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                // Handle any exceptions here
                e.printStackTrace();
                Toast.makeText(this, "An error happened in getSignedIn()", Toast.LENGTH_SHORT).show();
            }
        }
        sharedPreferencesManager.setSignedIn(true);
        toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitle("Home");
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.homeItem);
        root_layout = findViewById(R.id.layout_root);
        fragmentManager = getSupportFragmentManager();
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.homeItem));
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.emailHeader);
        email.setText(sharedPreferencesManager.getEmail());
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.nameHeader);
        String n = sharedPreferencesManager.getFirstName() + " " + sharedPreferencesManager.getLastName();
        name.setText(n);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.layout_root);

        int id = item.getItemId();
        if (item.isCheckable()) toolbar.setTitle(item.getTitle());
        if (id == R.id.homeItem) {
            if (!(currentFragment instanceof HomeFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);

                fragmentTransaction.replace(R.id.layout_root, new HomeFragment(), "HomeFrag");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.carMenuItem) {

            if (!(currentFragment instanceof CarMenuFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);

                fragmentTransaction.replace(R.id.layout_root, new CarMenuFragment(), "CarMenuFrag");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.contactMenuItem) {
            if (!(currentFragment instanceof ContactFragment)) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);

                fragmentTransaction.replace(R.id.layout_root, new ContactFragment(), "ContactFrag");
                fragmentTransaction.commit();
            }
        } else if (id == R.id.signOutMenuItem) {
            sharedPreferencesManager.setSignedIn(false);
            sharedPreferencesManager.clearAllButRememberMe();
            Intent intent = new Intent(CustomerNavigator.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.profileMenuItem) {
            if (!(currentFragment instanceof ProfileFragment)) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.layout_root, new ProfileFragment(), "ProfileFrag");
                fragmentTransaction.commit();

            }
        }

        // close drawer when item is tapped
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

}