package com.example.freeman_option1;

public class Item {

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String name;
    private String count;
    public String getName() {
        return name;
    }
    public String getCount() {
        return count;
    }


    public Item(String name, String count){
        this.name = name;
        this.count = count;
    }
}
