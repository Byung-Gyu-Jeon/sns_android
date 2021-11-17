package com.example.sns.main.ui.feed.Recomment;

public class Data5 {
    private String name;
    private String tme;
    private String comment;


    public Data5(String name,String tme,String comment){
        this.name = name;
        this.tme = tme;
        this.comment = comment;
    }

    public String getName() { return name;}
    public String getTme() {return tme;}
    public String getComment() {return comment;}

    public void setName(String name) { this.name=name;}
    public void setTme(String tme) {this.tme=tme;}
    public void setComment(String comment) { this.comment=comment;}
}
