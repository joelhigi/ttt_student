package com.tartantransporttracker.managers;

import com.tartantransporttracker.models.Route;
import com.tartantransporttracker.repository.RouteRepository;
import com.tartantransporttracker.repository.UserRepository;

public class RouteManager {
    private static volatile RouteManager instance;
    private RouteRepository routeRepository;

    public RouteManager() {
        routeRepository = routeRepository.getInstance();
    }

    public  static RouteManager getInstance(){
        RouteManager result = instance;
        if(result != null){
            return result;
        }
        synchronized (UserRepository.class){
            if(instance == null){
                instance = new RouteManager();
            }
            return instance;
        }
    }

    public void createRoute(Route route){
        routeRepository.createRoute(route);
    }

}
