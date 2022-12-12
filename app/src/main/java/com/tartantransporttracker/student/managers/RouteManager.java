package com.tartantransporttracker.student.managers;
/*
* A class that manages the Route objects
* by Didier
* */

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.student.models.Route;
import com.tartantransporttracker.student.models.User;
import com.tartantransporttracker.student.repository.RouteRepository;
import com.tartantransporttracker.student.repository.UserRepository;

import java.util.List;

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

    public Task<QuerySnapshot> findAllRoutes(){
        return routeRepository.findAll();
    }

    public Task<Route> getRoute(String id){
        return  routeRepository.getRoute(id).continueWith(task ->
                task.getResult().toObject(Route.class));
    }

    public void updateRoute(String id, Route route){routeRepository.updateRoute(id,route);}

    public void deleteRoute(String id){
        routeRepository.deleteRoute(id);
    }

}

