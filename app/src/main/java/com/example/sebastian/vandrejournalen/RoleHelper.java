package com.example.sebastian.vandrejournalen;

import com.example.sebastian.journalapp.R;

/**
 * Created by Sebastian on 07-11-2017.
 */

public class RoleHelper {

    public static int getOptionsMenu(String role){
        switch(role){
            case "PL":
                return R.menu.pl_drawer;
            case "MW":
                return R.menu.mw_drawer;
            default:
                return R.menu.activity_main_drawer;
        }
    }

}
