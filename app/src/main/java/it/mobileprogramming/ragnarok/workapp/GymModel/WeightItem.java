package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Date;

public class WeightItem {
    public Date date;
    public float value;

    @Override
    public String toString() {
        return "WeightItem{" +
                "date=" + date +
                ", value=" + value +
                '}';
    }

}
