package com.example.levelsixshift;




import static com.example.levelsixshift.UserDataManager.loadData;
import static com.example.levelsixshift.UserDataManager.saveData;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
//TODO: Create a "checker" method which contains logic for when money = 0, bet> money, etc.
public class slotsOfSinActivity extends AppCompatActivity implements View.OnClickListener {
    // declare toast
    Toast toastCenter;
    // declare xml objects
    TextView txtCurrentMoney, txtBetAmount;
    ImageButton btnSpin, btnMinus, btnPlus, btnExit, btnHelp;

    Context context;
    // define sharedPreferences keys that will be used in this activity.
    private static final String moneyKey = "levelsixshift/money";
    private static final String avatarKey = "levelsixshift/avatar";
    private static final String levelKey = "levelsixshift/level";
    private static final String experienceKey = "levelsixshift/experience";
    // declare, init helper variables
    int numberOfReels = 5; // TODO: make this numberOfReels in standard use
    int bet = 0;
    int maxBet = 100; // Designates the maximum bet
    // the below determines how much the current bet will be multiplied by in case of a 5/3 symbol reel
    int fiveJackpotMultiplier = 1000;
    int threeJackpotMultiplier = 100;
    int threeSevenMultiplier = 70;
    int threeCherryMultiplier = 10;
    int threeBellMultiplier = 5;
    int threeBarMultiplier = 3;


    int betAtSpin = 0; // helper for the bet at time of Spin, so a user cannot
    int amountToChange = 100; // amount that each press of the button should change TODO: Make this dependent on the amount of money teh user has
    int[] amountToChangeValues = {10, 100, 500, 1000, 10000}; // This designates the values that are dynamically updating amountToChange could be set to. Default is 100. If the length changes, dynamic() will need to be altered

    ImageView[] reels = new ImageView[numberOfReels];  // TODO: make this numberOfReels in standard use
    int numberOfJackpots = 0;
    int numberOfSevens = 0;
    int numberOfBars = 0;
    int numberOfCherries = 0;
    int numberOfBells = 0;
    // declaring + initializing global variables
    int money = 0;
    int level = 0;
    int experience =0;
    String avatar = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hides the title bar TODO: find a better way to do this, this feels jank
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //sets the xml for this activity
        setContentView(R.layout.slots_activity);
        context = getApplicationContext();
        // set up our Toast
        toastCenter = new Toast(context);
        toastCenter.setGravity(Gravity.CENTER, 0, 0);
        // set our ImageButtons
        btnSpin = (ImageButton) findViewById(R.id.btnSpin);
        btnMinus = (ImageButton) findViewById(R.id.btnMinus);
        btnPlus = (ImageButton) findViewById(R.id.btnPlus);
        btnExit = (ImageButton) findViewById(R.id.btnExit);
        btnHelp = (ImageButton) findViewById(R.id.btnHelp);
        // set our TextViews
        txtCurrentMoney = (TextView) findViewById(R.id.txtMoneyAmount);
        txtBetAmount = (TextView) findViewById(R.id.txtBetAmount);
        // set our Reels and assign our reels to the reels array
        reels[0] = (ImageView) findViewById(R.id.reelOne); //TODO: make this reel's naming scheme like the others
        reels[1] = (ImageView) findViewById(R.id.reel2);
        reels[2] = (ImageView) findViewById(R.id.reel3);
        reels[3] = (ImageView) findViewById(R.id.reel4);
        reels[4] = (ImageView) findViewById(R.id.reel5);
        // set onClickListeners to our ImageButtons
        btnSpin.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnHelp.setOnClickListener(this);
        // Load and Set Views

        // set key elements
        loadGlobalData();
        amountToChange =  (int)(maxBet *0.20);
        bet = 0;
        updateViews();

    }


    @Override
    public void onClick(View view) {
        // Operations involving the Spin Button
        if (view.getId() == R.id.btnSpin) {
            //
            btnSpin.setEnabled(false); // making it so that users can't spam
            btnExit.setEnabled(false); // making it so that users cannot exit during spin

            betAtSpin = Integer.parseInt((String) txtBetAmount.getText());
            // animates the slot machine, and assigns the cards
            if (betAtSpin == 0) {
                Toast.makeText(context, "You can't make a bet at 0, baka!", Toast.LENGTH_SHORT).show();
            } else {
                money -= betAtSpin;
                updateViews();
                spin();
                // this is a really jank solution. PROBLEM: Score adds either on next spin or before the anim finsihes
                // TODO: find a better solution to the above than a delay.
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        int currentWinnings = getScore(betAtSpin);
                        money += currentWinnings;

                        if (currentWinnings != 0) {
                            toastCenter.makeText(context, "You won " + currentWinnings + " coins!", Toast.LENGTH_SHORT).show();
                        }
                        checkLogic();
                        updateViews();
                        saveData(context, moneyKey, txtCurrentMoney.getText().toString());
                        // resetting helper checks TODO: clean this up and put this in a "reset" method
                        numberOfJackpots = 0;
                        numberOfSevens = 0;
                        numberOfBars = 0;
                        numberOfCherries = 0;
                        numberOfBells = 0;
                    }

                };
                Handler ScoreHandler = new Handler();
                ScoreHandler.postDelayed(run, 1000);
            }
            btnSpin.setEnabled(true); //TODO: put these in the theoretical "reset" method
            btnExit.setEnabled(true);


        }
        // Operations involving the Exit Button
        if (view.getId() == R.id.btnExit) {
            saveGlobalData();
            saveData(context, moneyKey, String.valueOf(money));
            // not making this a method, its pretty simple and just goes back to MainActivity
            Intent intent = new Intent(slotsOfSinActivity.this, MainActivity.class);
            startActivity(intent);
        }
        // Operations involving the Minus Bet Button
        if (view.getId() == R.id.btnMinus) {
            // do stuff

            if (bet - amountToChange < 0) {
                toastCenter.makeText(context, "You can't make a negative bet, baka!", Toast.LENGTH_SHORT).show();
            } else {
                bet -= amountToChange;

                updateViews();
            }

        }
        // Operations involving the Plus Bet Button
        if (view.getId() == R.id.btnPlus) {
            // do stuff
            if(bet+amountToChange > money) {

                toastCenter.makeText(context,"Not Enough Money",Toast.LENGTH_SHORT).show();
            }
            else if(bet + amountToChange >maxBet ) {

                bet = (  maxBet );

                toastCenter.makeText(context, "Max b-b-bet l-l-li-limit, reached!", Toast.LENGTH_SHORT).show();
                updateViews();
            }
            else {
                bet += amountToChange;
                updateViews();
            }

        }
        // Operations involving the Help Button
        if (view.getId() == R.id.btnHelp) {
            // do stuff
        }

    }

    /*
       Sets and declares a Random variable (r) between 1 and 100 for each reel
       Cards are assigned according to these percentages by calling getCard()
        JACKPOT - 5% | ie. if 1 <= r <= 5
	    7 - 15% | ie. if 6 <= r <= 20
	    BAR - 25% | ie. if 21 <= r <= 45
	    BELL - 30% | ie. if 46 <= r <= 75
	    CHERRY - 25% | ie. if 76 <= r <= 100
       Sets our cards according to these percentages
        */
    private void setCards() {
        int r = 0;
        for (int i = 0; i < numberOfReels; i++) {
            r = new Random().nextInt(100) + 1; // .nextInt(( max -min) +1) + min;
            reels[i].setBackgroundResource(getCard(r));
        }
    }

    /*
        Note: Perhaps a bit redundant, but it makes the code neater.
        Given an int r, randomized between 1, inclusive, to 100, inclusive
        Evaluates according to the percentages
         JACKPOT - 5% | ie. if 1 <= r <= 5
	     7 - 15% | ie. if 6 <= r <= 20
	     BAR - 25% | ie. if 21 <= r <= 45
	     BELL - 30% | ie. if 46 <= r <= 75
	     CHERRY - 25% | ie. if 76 <= r <= 100
	     Once evaluated adds 1 to the respective NumberOf_____
     */
    private int getCard(int r) {
        // jackpot
        if (r >= 1 && r <= 5) {
            numberOfJackpots++;
            return R.drawable.jackpot;
        }
        // seven
        if (r >= 6 && r <= 20) {
            numberOfSevens++;
            return R.drawable.seven;
        }
        // bar
        if (r >= 21 && r <= 45) {
            numberOfBars++;
            return R.drawable.bar;
        }
        // bell
        if (r >= 46 && r <= 75) {
            numberOfBells++;
            return R.drawable.bell;
        }
        //cherry
        if (r >= 76 && r <= 100) {
            numberOfCherries++;
            return R.drawable.cherry;
        }
        // error
        return 0;
    }

    /*
      Determines the Score Multiplier, multiples SM and bet and adds it to the Money.
      uses numberOfJackpots, numberOfSevens, numberOfBars, numberOfBells, numberOfCherries
      returns an int because I wanted a way to break out without using switches or nested if/else statements
      Certainly a better way to do that

      The order of these else if is meant to reflect the order of importance of winnings
      Switch cases would probably work better than, but there isn't really one case, it might get
      pretty inundated
     `
      TODO: Actually have the bet be multiplied and added once we implement that
      TODO: find out if we are making the combinations consecutive or all ways, it will be all ways for now
   */
    private int getScore(int betAtSpin) {

        if (numberOfJackpots >= 3) {
            if (numberOfJackpots == 5) {

                return betAtSpin * fiveJackpotMultiplier;

            }
            return betAtSpin * threeJackpotMultiplier;
        }
        if (numberOfSevens >= 3) {
            return betAtSpin * threeSevenMultiplier;
        }
        if (numberOfCherries >= 3) {
            return betAtSpin * threeCherryMultiplier;
        }
        if (numberOfBells >= 3) {
            return betAtSpin * threeBellMultiplier;
        }
        if (numberOfBells >= 3) {
            return betAtSpin * threeBellMultiplier;
        }
        if (numberOfBars >= 3) {
            return betAtSpin * threeBarMultiplier;
        }
        return 0;
    }

    /*
    Animates the all reels in the array reels once and then stops them, then
    calls setCards();
     NOTE: TESTED if we could make setCard outside of the Runnable, looks bad
     */
    private void spin() {
        AnimationDrawable[] anims = new AnimationDrawable[reels.length];
        // start the animations
        for (int i = 0; i < reels.length; i++) {
            reels[i].setBackgroundResource(R.drawable.anim);
            final AnimationDrawable reelAnim = (AnimationDrawable) reels[i].getBackground();
            reelAnim.start();
            anims[i] = reelAnim;

        }
        // stop the animations
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < reels.length; i++) {
                    anims[i].stop();

                }
                setCards();


            }
        }, 500);
        // this might have to go in the anonymous runnable TODO: Further Testing + Finishing getScore
    }

    /*
        Updates all appropriate textViews on screen
        Could be easier to just update individually, but the performance increase
        would be negligible
     */
    public void updateViews() {
        txtCurrentMoney.setText(String.valueOf(money));
        txtBetAmount.setText(String.valueOf(bet));
    }

    /*
        Checks various parts of every new betting instance. Such as
        is money < amountToChange
        is bet > money

        and then does relevant code
        might be prudent to delete this, since it is possible that it is just increasing complexity
        It will depend on the amount of checks we do.
     */
    public void checkLogic() {



        if (money < amountToChange) {

        }
        if (bet > money) {
            bet = money / amountToChange * amountToChange; // this sets bet to money rounded to the amountToChange in the event of bet being greater than money. For instance, if amountToChange = 100, money 2860, then bet would equal 2800. This would only occur if bet <money, however TODO: Find a better way to say this cause goddamn

        }
    }

    private int amountToChangeSelector() {



        return 0;
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
}


