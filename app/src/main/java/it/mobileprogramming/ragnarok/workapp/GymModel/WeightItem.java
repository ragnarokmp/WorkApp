package it.mobileprogramming.ragnarok.workapp.GymModel;

import java.util.Comparator;
import java.util.Date;

public class WeightItem implements Comparable<WeightItem>{
    public Date date;
    public float value;

    @Override
    public String toString() {
        return "WeightItem{" +
                "date=" + date +
                ", value=" + value +
                '}';
    }

    @Override
    public int compareTo(WeightItem another) {
        if(another.date.before(this.date)){
            return 1;
        }
        else if(another.date.after(this.date)){
            return -1;
        }
        return 0;
    }
}
