package com.seffyo.kandaapptesting.activities.adapter.adapters;

/**
 * Created by renkar on 03.10.2017.
 */

public class Grocery {
    private String _name;
    private String _number;

    public Grocery(String name, String number) {
        _name = name;
        _number = number;
    }

    public void SetNumber(String number) {
        _number = number;
    }

    public String GetNumber() {
        return _number;
    }

    public void SetName(String name) {
        _name = name;
    }

    public String GetName() {
        return _name;
    }
}
