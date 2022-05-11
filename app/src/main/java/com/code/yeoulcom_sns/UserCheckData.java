package com.code.yeoulcom_sns;

public class UserCheckData {
    private String name;
    private String generation;

    public UserCheckData(){

    }
    public UserCheckData(String name, String generation){
        this.generation = generation;
        this.name = name;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
