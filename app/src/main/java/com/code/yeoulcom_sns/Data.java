package com.code.yeoulcom_sns;

public class Data {

    //리스트에 추가할 값들 (RecyclerView에 들어갈 값)

    private String title;
    private String main_text;
    private String ImgUrl;
    private String Time;
    private String name;
    private String generation;

    public Data(String title, String main_text, String imgURL,String Time,String name, String generation) {
        this.title = title;
        this.main_text = main_text;
        this.ImgUrl = imgURL;
        this.Time = Time;
        this.name = name;
        this.generation = generation;
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
    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMain_text() {
        return main_text;
    }

    public void setMain_text(String main_text) {
        this.main_text = main_text;
    }

    public String getImgUrl() {
        return ImgUrl;
    }
    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
