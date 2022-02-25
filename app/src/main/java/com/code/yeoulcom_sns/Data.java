package com.code.yeoulcom_sns;

public class Data {

    //리스트에 추가할 값들 (RecyclerView에 들어갈 값)

    private String title;
    private String main_text;
    private int resld;

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

    public int getResld() {
        return resld;
    }

    public void setResld(int resld) {
        this.resld = resld;
    }
}
