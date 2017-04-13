package nl.gottogo.gottogoapplication;

import java.util.Comparator;
import java.util.HashMap;
import java.util.SimpleTimeZone;

/**
 * Created by Merik on 13/04/2017.
 */

public class ValueComparator implements Comparator<String> {

    HashMap<String, Integer> map = new HashMap<String, Integer>();

    public ValueComparator(HashMap<String, Integer> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String c1, String  c2) {
        if(map.get(c1) >= map.get(c2)){
            return -1;
        }else{
            return 1;
        }
    }
}