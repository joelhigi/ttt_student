package com.tartantransporttracker.models;

public class BusStop {
    private String busStopName;
    private String route;
    private int position;

    public BusStop() {
    }

    public BusStop(String _busStopName, String _route, int _position) {
        busStopName = _busStopName;
        route = _route;
        position = _position;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String _busStopName) {
        busStopName = _busStopName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String _route) {
        route = _route;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int _position) {
        position = _position;
    }
}
