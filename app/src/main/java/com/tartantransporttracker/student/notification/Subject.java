package com.tartantransporttracker.student.notification;


import com.tartantransporttracker.student.models.User;

public interface Subject {
    //methods to register and unregister observers
    public void register(User obj);
    public void unregister(User obj);

    //method to notify observers of change
    public void notifyObservers(String notification);

}
