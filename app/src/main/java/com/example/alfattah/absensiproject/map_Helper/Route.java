package com.example.alfattah.absensiproject.map_Helper;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * Created by jon_snow on 11/11/2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
