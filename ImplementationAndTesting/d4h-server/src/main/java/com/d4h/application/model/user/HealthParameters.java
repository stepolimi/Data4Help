package com.d4h.application.model.user;

import com.d4h.application.model.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class HealthParameters {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private int heartBeat;
    private int veinPressure;
    private float temperature;
    private Date date;

    @ManyToOne (cascade = CascadeType.ALL)
    private User user;

    public int getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getVeinPressure() {
        return veinPressure;
    }

    public void setVeinPressure(int veinPressure) {
        this.veinPressure = veinPressure;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}