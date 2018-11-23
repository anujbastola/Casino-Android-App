package com.example.anujbastola.casinoapp.model.players;

import com.example.anujbastola.casinoapp.model.setup.Cards;

import java.util.ArrayDeque;
import java.util.Deque;

public class Build {

    private Deque<Deque<Cards>> builts = new ArrayDeque<>();

    //private String buildOwner;

    private boolean isOwner;

    Build(){
        isOwner = false;
    }

    // Setter for built
    public void setBuilts(Deque<Cards> builtReceived){
        builts.add(builtReceived);
    }

    // Setter for owner
    public void setOwner( boolean isOwn){
        isOwner = isOwn;
    }

    // getter for owner
    public boolean isOwner() {
        return isOwner;
    }

    // empty built when the build is captured
    public void emptyBuilt(){
        builts.clear();
    }

    public void deleleBuilt(Deque<Cards> builtToDelete){
        builts.remove(builtToDelete);
    }

    // Getter for player's built
    public Deque<Deque<Cards>> getBuilt(){
        return builts;
    }

}
