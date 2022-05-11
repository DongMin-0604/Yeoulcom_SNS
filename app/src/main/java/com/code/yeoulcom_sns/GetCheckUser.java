package com.code.yeoulcom_sns;

public class GetCheckUser {
    private String name;
    private String generation;

    public GetCheckUser(){

    }
    public GetCheckUser(String name, String generation){
        this.generation = generation;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }


}
