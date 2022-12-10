package com.tartantransporttracker.repository;

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
import com.tartantransporttracker.models.Role;
import com.tartantransporttracker.models.Route;
import com.tartantransporttracker.models.User;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class RouteRepository {

    private static volatile RouteRepository instance;
    private static  final String COLLECTION_NAME="routes";

    public  RouteRepository(){}

    public static RouteRepository getInstance(){
        synchronized (BusRepository.class){
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
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
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
        Log.e("================Before ===========", String.valueOf(routes.size()));
        return this.getRoutesCollection().get();
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(!queryDocumentSnapshots.isEmpty()){
//                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                            for(DocumentSnapshot doc:list){
//                                Route route = doc.toObject(Route.class);
//                                Log.e("================Document route: ===========", doc.get("name").toString());
//                                Log.e("================Document get name: ===========", route.getName());
//                                routes.add(route);
//
//                            }
//                        }else{
//                            Log.w(TAG,"No data found in the database");
//                        }
//                    }
//                });
//        Log.e("================After: ===========", String.valueOf(routes.size()));
//        return routes;
    }


    //Get Single route from firestore
    public Task<DocumentSnapshot> getRoute(String id){
        if(id !=null){
            return  this.getRoutesCollection().document(id).get();
        }
        return null;
    }




    // Update Route
    public void updateRoute(String routeName,Route updatedRoute){
        this.getRoutesCollection()
                .document(routeName)
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

    // Delete the Route from Firestore
    public void deleteRoute(String routeName) {
//        String docId = this.findDocId(routeName);
        if(routeName != null){
            this.getRoutesCollection().document(routeName).delete();
        }
    }
    
}
