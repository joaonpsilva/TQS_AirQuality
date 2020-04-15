package com.frstassign.airquality;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Component;

@Component
public class MyCache {

    private Queue<String> names;
    private Map<String, Region> cache;
    private int maxSize;
    private int ind;
    private int nrequests;
    private int hits;
    private int misses;
    
    public MyCache(){
        this.maxSize = 3;
        this.names = new LinkedList<>();
        this.cache = new HashMap<>();
        this.ind = 0;
        this.nrequests = 0;
        this.hits = 0;
        this.misses = 0;

    }

    public void addToCache(Region r){
        
        //Remove last element
        if (ind >= maxSize){
            String name = this.names.remove();
            cache.remove(name);
            ind--;
        }
            
        this.names.add(r.getName());
        this.cache.put(r.getName(), r);
        ind++;

    }

    public Region searchInCache(String name){
        this.nrequests++;
        if (cache.containsKey(name)){
            this.hits++;
            return cache.get(name);
        }
        this.misses++;
        return null;
    }

    public void sendToEnd(String name){
        this.names.remove(name);
        this.names.add(name);
    }

    public String toString(){
        return this.cache.toString();
    }

    public Map<String, Region> getCache(){
        return this.cache;
    }

    public int getNrequests(){
        return this.nrequests;
    }
    public int getHits(){
        return this.hits;
    }
    public int getMisses(){
        return this.misses;
    }


}