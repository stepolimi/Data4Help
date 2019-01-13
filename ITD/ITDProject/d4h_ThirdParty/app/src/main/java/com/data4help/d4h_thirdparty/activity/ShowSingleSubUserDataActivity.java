package com.data4help.d4h_thirdparty.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.data4help.d4h_thirdparty.R.*;

public class ShowSingleSubUserDataActivity extends AppCompatActivity {

    private Button today;
    private Button yesterday;
    private Button twoDaysAgo;
    private Button threeDaysAgo;
    private Button fourDaysAgo;
    private Button fiveDaysAgo;
    private Button sixDaysAgo;

    private ViewPager viewPager;


    private TextView userName;
    private TextView userAge;
    private TextView userWeight;
    private TextView userHeight;
    private TextView userSex;
    private TextView userFiscalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.show_single_data);

        runOnUiThread(() -> {
            setAttributes();

            viewPager = findViewById(id.dataPage);
            ShowSingleSubUserDataPagerViewAdapter pagerAdapter = new ShowSingleSubUserDataPagerViewAdapter(getSupportFragmentManager());
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
        selected.setTextColor(getResources().getColor(color.colorAccent));
        unselected1.setTextColor(getResources().getColor(color.black));
        unselected2.setTextColor(getResources().getColor(color.black));
        unselected3.setTextColor(getResources().getColor(color.black));
        unselected4.setTextColor(getResources().getColor(color.black));
        unselected5.setTextColor(getResources().getColor(color.black));
        unselected6.setTextColor(getResources().getColor(color.black));
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
        userName = (findViewById(id.userName));
        userAge = (findViewById(id.userAge));
        userWeight = findViewById(id.userWeight);
        userHeight = (findViewById(id.userHeight));
        userSex = (findViewById(id.userSex));
        userFiscalCode = findViewById(id.userFiscalCode);

    }

    /**
     *
     * Sets all param associated to the request done
     */
    public void setRequestParam(String name, String surname,int yearOfBirth, int height, int weight, String sex, String fiscalCode) {
        String fullName = name + " " +surname;
        userName.setText(fullName);
        userAge.setText(String.valueOf(yearOfBirth));
        userHeight.setText(String.valueOf(height));
        userWeight.setText(String.valueOf(weight));
        userSex.setText(sex);
        userFiscalCode.setText(fiscalCode);
    }
}
