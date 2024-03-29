package com.data4help.d4h_thirdparty.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.data4help.d4h_thirdparty.R.*;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowGroupDataActivity extends AppCompatActivity {

    private Button today;
    private Button yesterday;
    private Button twoDaysAgo;
    private Button threeDaysAgo;
    private Button fourDaysAgo;
    private Button fiveDaysAgo;
    private Button sixDaysAgo;

    private  TextView ageRange;
    private  TextView heightRange;
    private  TextView weightRange;
    private TextView sexRange;
    private TextView addressRange;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.data4help.d4h_thirdparty.R.layout.show_group_data);

        runOnUiThread(() -> {
        setAttributes();

        viewPager = findViewById(id.dataPage);
        ShowGroupDataPagerViewAdapter pagerAdapter = new ShowGroupDataPagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);


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
        ageRange = findViewById(id.ageRange);
        heightRange = findViewById(id.heightRange);
        weightRange = findViewById(id.weightRange);
        sexRange = findViewById(id.sexRange);
        addressRange = findViewById(id.addressRange);

    }

    /**
     * @param param is the JSONObject that must be filled
     *
     * Sets all param associated to the request done
     */
    public void setGroupRequestParam(JSONObject param) throws JSONException {
        String age = param.getString("minAge") + "-" + param.getString("maxAge");
        ageRange.setText(age);

        String weight = param.getString("minWeight") + "-" + param.getString("maxWeight");
        weightRange.setText(weight);

        String height = param.getString("minHeight") + "-" + param.getString("maxHeight");
        heightRange.setText(height);

        String sex = param.getString("sex");
        if(sex.equals("male") || sex.equals("female"))
            sex = "male - female";
        sexRange.setText(sex);

        String address = param.getString("state") + "-" + param.getString("region");
        addressRange.setText(address);


    }
}
