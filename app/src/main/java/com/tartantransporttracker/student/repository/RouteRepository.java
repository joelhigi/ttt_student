package com.tartantransporttracker.student.repository;
/*
* Class that functions as the repository for the routes
* by Didier & Joel*/

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tartantransporttracker.student.models.Role;
import com.tartantransporttracker.student.models.Route;
import com.tartantransporttracker.student.models.User;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class RouteRepository {

    private static volatile RouteRepository instance;
    private static final String ROUTES_COLLECTION="routes";
    private static final String USER_COLLECTION="routes";

    public  RouteRepository(){}

    public static RouteRepository getInstance(){
        synchronized (RouteRepository.class){
            RouteRepository result = instance;
            if(result != null){
                return  result;
            }
            if(instance == null){
                instance = new RouteRepository();
            }
            return instance;
        }
    }

    // firestore functions

    // get document reference
    private CollectionReference getRoutesCollection(){
        return FirebaseFirestore.getInstance().collection(ROUTES_COLLECTION);
    }

    // get document reference for user
    private CollectionReference getUserCollection(){
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION);
    }


    // create route in Firestore
    public void createRoute(Route route){
        this.getRoutesCollection().add(route)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    // get all data from firestore
    public Task<QuerySnapshot> findAll(){
        List<Route> routes = new ArrayList<>();
        return this.getRoutesCollection().get();
    }

    //Get Single route from firestore
    public Task<DocumentSnapshot> getRoute(String id){
        if(id !=null){
            return  this.getRoutesCollection().document(id).get();
        }
        return null;
    }


    // Update Route
    public void updateRoute(String id,Route updatedRoute){
        this.getRoutesCollection()
                .document(id)
                .set(updatedRoute)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.w(TAG,"Route updated successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Route not updated");
                    }
                });
    }

    // Update favorite Route
    public void updateUserRoute(String id,Route updatedRoute){
        String route = updatedRoute.getName();
        this.getUserCollection()
                .document(id)
                .set(route)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.w(TAG,"Favorite Route updated successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"Favorite Route not updated");
                    }
                });
    }
    // Delete the Route from Firestore
    public void deleteRoute(String id) {
        if(id != null){
            this.getRoutesCollection().document(id).delete();
        }
    }

}

