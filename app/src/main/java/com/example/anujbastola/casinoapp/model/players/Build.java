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


    public void setBuilts(Deque<Cards> builtReceived){
        builts.add(builtReceived);
    }

    public void setOwner( boolean isOwn){
        isOwner = isOwn;
    }

    public void emptyBuilt(){
        builts.clear();
    }

    public void deleleBuilt(Deque<Cards> builtToDelete){
        builts.remove(builtToDelete);
    }

    public Deque<Deque<Cards>> getBuilt(){
        return builts;
    }

}
