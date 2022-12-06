package com.tartantransporttracker.models;

public class Route {
    String routeName;

    public Route() {
    }

    public Route(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String _routeName) {
        routeName = _routeName;
    }
}
