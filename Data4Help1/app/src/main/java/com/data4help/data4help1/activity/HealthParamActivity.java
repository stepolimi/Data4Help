package com.data4help.data4help1.activity;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.data4help.data4help1.AuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.data4help.data4help1.Config.MAXHEARTBEAT;
import static com.data4help.data4help1.Config.MAXMAXPRESSURE;
import static com.data4help.data4help1.Config.MAXMINPRESSURE;
import static com.data4help.data4help1.Config.MAXTEMPERATURE;
import static com.data4help.data4help1.Config.MINHEARTBEAT;
import static com.data4help.data4help1.Config.MINMAXPRESSURE;
import static com.data4help.data4help1.Config.MINMINPRESSURE;
import static com.data4help.data4help1.Config.MINTEMPERATURE;
import static com.data4help.data4help1.Config.SENDPARAMURL;

public class HealthParamActivity extends Thread {

    //this class should contain the sensor listener

    public void startThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            String url = "";

                do {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int heartBeat = createRandomBpm();
                    int maxPressure = createRandomMaxPressure();
                    int minPressure = createRandomMinPressure();
                    Double temperature = createRandomTemperature();

                    JSONObject param = new JSONObject();
                    try {
                        param.put("userId", AuthToken.getId());
                        param.put("heartBeat", heartBeat);
                        param.put("minPressure", minPressure);
                        param.put("maxPressure", maxPressure);
                        param.put("temperature", temperature);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest sendParam = new JsonObjectRequest(Request.Method.POST, SENDPARAMURL, param, response -> {}, volleyError -> {});
                    MenuActivity.queue.add(sendParam);
                } while (true);
            }
        }).start();
    }


    /**
     * @return a random value between 60 and 100. Those bounds are the typical one of a normal
     * heart beat.
     */
    private int createRandomBpm() {
        Random random = new Random();
        return random.nextInt(MAXHEARTBEAT - MINHEARTBEAT) + MINHEARTBEAT;
    }

    /**
     * @return a random value between 80 and 120. Those bounds are the typical one of a normal
     * max pressure.
     */
    private int createRandomMaxPressure(){
        Random random = new Random();
        return random.nextInt(MAXMAXPRESSURE - MINMAXPRESSURE) + MINMAXPRESSURE;
    }

    /**
     * @return a random value between 50 and 80. Those bounds are the typical one of a normal
     * min pressure.
     */
    private int createRandomMinPressure(){
        Random random = new Random();
        return random.nextInt(MAXMINPRESSURE - MINMINPRESSURE) + MINMINPRESSURE;
    }

    /**
     * @return a random value between 50 and 80. Those bounds are the typical one of a normal
     * min pressure.
     */
    private Double createRandomTemperature() {
        Random random = new Random();
        return random.nextDouble()*(MAXTEMPERATURE - MINTEMPERATURE) + MINTEMPERATURE;
    }

}
