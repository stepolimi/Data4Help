package com.data4help.d4h_thirdparty.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.data4help.d4h_thirdparty.R.*;

public class ShowSingleDataActivity extends AppCompatActivity {

    private Button today;
    private Button yesterday;
    private Button twoDaysAgo;
    private Button threeDaysAgo;
    private Button fourDaysAgo;
    private Button fiveDaysAgo;
    private Button sixDaysAgo;

    private static TextView userName;
    private static TextView userSurname;
    private static TextView userAge;
    private static TextView userWeight;
    private static TextView userHeight;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.show_single_data);

        runOnUiThread(() -> {
            setAttributes();

            viewPager = findViewById(id.dataPage);
            ShowDataPagerViewAdapter pagerAdapter = new ShowDataPagerViewAdapter(getSupportFragmentManager());
            viewPager.setAdapter(pagerAdapter);

            setItem(today, 0);
            setItem(yesterday, 1);
            setItem(twoDaysAgo, 2);
            setItem(threeDaysAgo, 3);
            setItem(fourDaysAgo, 4);
            setItem(fiveDaysAgo, 5);
            setItem(sixDaysAgo, 6);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float v, int i1) {
                }

                @Override
                public void onPageSelected(int position) {
                    changeTab(position);
                }

                @Override
                public void onPageScrollStateChanged(int position) {
                }
            });
        });
    }


    /**
     * @param button is the selected textVIew
     * @param position is the fragment position associated to the selected text view
     *
     * Changes the fragment depending on the selected textView
     */
    private void setItem(Button button, int position) {
        button.setOnClickListener(v -> {
            changeTab(position);
            viewPager.setCurrentItem(position);});
    }

    /**
     * @param position is the int related to the chosen fragment
     *
     * Modifies the textView color in relation to the the chosen position
     */
    private void changeTab(int position) {
        switch (position){
            case 0:
                setColor(today, yesterday, twoDaysAgo, threeDaysAgo, fourDaysAgo, fiveDaysAgo, sixDaysAgo);
                break;
            case 1:
                setColor(yesterday, twoDaysAgo, threeDaysAgo, fourDaysAgo, fiveDaysAgo, sixDaysAgo, today);
                break;
            case 2:
                setColor(twoDaysAgo, threeDaysAgo, fourDaysAgo, fiveDaysAgo, sixDaysAgo, today, yesterday);
                break;
            case 3:
                setColor(threeDaysAgo, fourDaysAgo, fiveDaysAgo, sixDaysAgo, today, yesterday, twoDaysAgo);
                break;
            case 4:
                setColor(fourDaysAgo, fiveDaysAgo, sixDaysAgo, today, yesterday, twoDaysAgo, threeDaysAgo);
                break;
            case 5:
                setColor(fiveDaysAgo, sixDaysAgo, today, yesterday, twoDaysAgo, threeDaysAgo, fourDaysAgo);
                break;
            case 6:
                setColor(sixDaysAgo, today, yesterday, twoDaysAgo, threeDaysAgo, fourDaysAgo, fiveDaysAgo);
                break;
            default:
                setColor(sixDaysAgo, today, yesterday, twoDaysAgo, threeDaysAgo, fourDaysAgo, fiveDaysAgo);
                break;
        }
    }

    /**
     * @param selected is the textView related to the selected fragment
     * @param unselected1 is one of the textView not related to the fragment
     * @param unselected2 is one of the textView not related to the selected fragment
     * @param unselected3 is one of the textView not related to the selected fragment
     */
    @SuppressLint("ResourceAsColor")
    private void setColor(Button selected, Button unselected1, Button unselected2, Button unselected3,
                          Button unselected4, Button unselected5, Button unselected6){
        selected.setTextColor(color.colorAccent);
        unselected1.setTextColor(color.black);
        unselected2.setTextColor(color.black);
        unselected3.setTextColor(color.black);
        unselected4.setTextColor(color.black);
        unselected5.setTextColor(color.black);
        unselected6.setTextColor(color.black);
    }


    /**
     * Associates attributes to registration.xml elements
     */
    private void setAttributes() {
        today = findViewById(id.todayButton);
        yesterday = findViewById(id.yesterdayButton);
        twoDaysAgo = findViewById(id.twoDaysAgoButton);
        threeDaysAgo = findViewById(id.threeDaysButton);
        fourDaysAgo = findViewById(id.fourDaysButton);
        fiveDaysAgo = findViewById(id.fiveDaysButton);
        sixDaysAgo = findViewById(id.sixDaysButton);
        userName = findViewById(id.userName);
        userSurname = findViewById(id.userSurname);
        userAge = findViewById(id.userAge);
        userWeight = findViewById(id.userWeight);
        userHeight = findViewById(id.userHeight);

    }

    /**
     * @param param is the JSONObject that must be filled
     *
     * Sets all param associated to the request done
     */
    public static void setRequestParam(JSONObject param) throws JSONException {
        userName.setText(param.getString("name"));
        userSurname.setText(param.getString("surname"));
        userAge.setText(param.getString("age"));
        userWeight.setText(param.getString("weight"));
        userHeight.setText(param.getString("height"));
    }
}
