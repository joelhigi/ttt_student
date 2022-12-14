package com.tartantransporttracker.student.managers;
/*
 * A class that manages the User objects
 * by Didier
 * */

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.tartantransporttracker.student.models.User;
import com.tartantransporttracker.student.repository.UserRepository;

;

public class UserManager {
    private static volatile UserManager instance;
    private UserRepository userRepo;

    public UserManager() {
        userRepo = userRepo.getInstance();
    }

    public  static UserManager getInstance(){
        UserManager result = instance;
        if(result != null){
            return result;
        }
        synchronized (UserRepository.class){
            if(instance == null){
                instance = new UserManager();
            }
            return instance;
        }
    }

    public FirebaseUser getCurrentUser(){
        return  userRepo.getCurrentUser();
    }

    public Boolean isCurrentUserLogged(){
        return (this.getCurrentUser() !=null);
    }

    public Task<Void> signOut(Context context){
        return userRepo.signOut(context);
    }

    public void createUser(){
        this.userRepo.createUser();
    }

    public Task<User> getUserData(){
        return userRepo.getUserData().continueWith(task ->
                task.getResult().toObject(User.class));
    }

    public Task<Void> updateUsername(String username){
        return userRepo.updateUserName(username);
    }

    public void updateRole(String role){
        userRepo.updateRole(role);
    }

    public Task<Void> deleteUser(Context context){
        // Delete the user account from the Auth
        return userRepo.deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user data from Firestore
            userRepo.deleteUserFromFirestore();
        });
    }

}
