package com.example.alfattah.absensiproject.map_Helper;



import java.util.List;

/**
 * Created by jon_snow on 11/11/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
