package com.majia.alarmalarm;

public class DataList {
    private String rdText;
    private int song;

    public DataList(String rdText, int id) {
        super();
        this.rdText = rdText;
        this.song = id;
    }

    public String getRdText() {
        return rdText;
    }

    public void setRdText(String rdText) {
        this.rdText = rdText;
    }
    
    public int getSong(){
    	return song;
    }
}
