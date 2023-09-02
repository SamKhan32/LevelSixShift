package com.example.levelsixshift;



import static com.example.levelsixshift.UserDataManager.loadData;
import static com.example.levelsixshift.UserDataManager.saveData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ButtonClickListener {
    Context context;

    private static final String LOG_TAG ="ButtonClick"; // for debugging purposes TODO: Delete once finished
    private static final String moneyKey = "levelsixshift/money";
    private static final String avatarKey = "levelsixshift/avatar";
    private static final String levelKey = "levelsixshift/level";

    private static final String experienceKey = "levelsixshift/experience";
    private TextView txtCurrentMoney;
    BottomNavigationView bottomNavigationView; // Represents the bottom bar that has the buttons store, lobby, and profiles

    private int money =-1;
    private int level = -1;
    private int experience = -1;
    private String avatar ="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Main Activity is being created");
        // hides the title bar TODO: find a better way to do this, this feels jank
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        // set up helper variables
        context = getApplicationContext();

        // set up xml objects
        txtCurrentMoney = (TextView) findViewById(R.id.txtMoney);

        //sets the xml for this activity

        // sets up the Bottom Navigation Menu
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this); //deprecated, but works currently TODO: figure out a "permanent" way to do this
        bottomNavigationView.setSelectedItemId(R.id.lobby); // on create, the second fragment, lobby, is put on screen
        // sets up our saved variables

        // Load saved variables and update screen DONE: Having a first start have money at -1 is jury-rigging, fixed
            if(money ==0) {
                // Do stuff, maybe prompt the user to watch a video
            }
            // load in (this handles first use cases)
            money = Integer.parseInt(loadData(context, moneyKey, "1000"));
            avatar = loadData(context,avatarKey,"Alyx"); //TODO: IMPLEMENT BETTER
            level = Integer.parseInt(loadData(context, levelKey, "1"));
            experience = Integer.parseInt(loadData(context,experienceKey, "0"));

            Log.i(LOG_TAG, String.valueOf(money));
            updateViews();


        }

    public void updateViews() {

        txtCurrentMoney.setText(String.valueOf(money));
    }


    fragment_first fragment_first = new fragment_first(); // The fragment representing store
    fragment_second secondFragment = new fragment_second(); // The fragment representing lobby
    fragment_third thirdFragment = new fragment_third();// The fragment representing profile/whatever else
    @Override
    public void onButtonClick(View view) {
        // save all relevant data and move to SlotOfSin
        if(view.getId() == R.id.btnGame ) {


            saveGlobalData();
            navigateToGameActivity();
        }
        if(view.getId() == R.id.button) {
            money +=100;
            saveData(context, moneyKey, txtCurrentMoney.getText().toString());
            updateViews();
        }
        if(view.getId() == R.id.hundred) {
            money =100;
            saveData(context, moneyKey, txtCurrentMoney.getText().toString());
            updateViews();
        }
    }

    private void saveGlobalData() {
        ((RadioNoise) this.getApplication()).setMoney(money);
        ((RadioNoise) this.getApplication()).setAvatar(avatar);
        ((RadioNoise) this.getApplication()).setLevel(level);
        ((RadioNoise) this.getApplication()).setExperience(experience);
    }
    private void loadGlobalData() {
        money = ((RadioNoise) this.getApplication()).getMoney();
        avatar = ((RadioNoise) this.getApplication()).getAvatar();
        level = ((RadioNoise) this.getApplication()).getLevel();
        experience =  ((RadioNoise) this.getApplication()).getExperience();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) // changes the fragment when a user clicks on a part of the bottom Navigation menu
    {

        saveGlobalData();
        if (item.getItemId() == R.id.store) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, fragment_first)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.lobby) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, secondFragment)
                    .commit();
            return true;
        }
        if (item.getItemId() == R.id.profile) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, thirdFragment)
                        .commit();
                return true;
        }
        return false;
    }

    public void navigateToGameActivity() {
        Intent intent = new Intent(MainActivity.this, slotsOfSinActivity.class);
        intent.putExtra("key", "value"); //TODO maybe send coin data or what champion they have selected?
        startActivity(intent);
    }


}