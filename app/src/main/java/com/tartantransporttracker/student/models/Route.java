package com.tartantransporttracker.student.models;
/*
 * A class that acts as the model for all routes.
 * by Didier
 * */
import com.google.firebase.firestore.DocumentId;
import com.tartantransporttracker.student.notification.Subject;

import java.util.List;

public class Route implements Subject {
    @DocumentId
    private String id;
    private String name;

    private List<User> students;

    public Route() {
    }

    public Route(String routeName) {
        name = routeName;
    }

    public String getName() {
        return name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> _users) {
        students = _users;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void register(User obj) {
        students.add(obj);
    }

    @Override
    public void unregister(User obj) {
        students.remove(obj);
    }

    @Override
    public void notifyObservers(String notification) {
        for (User std : students) {
            std.update(notification);
        }
    }
}
