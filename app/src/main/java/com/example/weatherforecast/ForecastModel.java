package com.example.weatherforecast;

public class ForecastModel {

    private String date;

    private String time;
    private String imageUrl;
    private String temperature;

    public ForecastModel( String dayTv, String timeTv,
                        String imageUrl, String temperature){

        this.date=dayTv;
        this.time=timeTv;
        this.imageUrl=imageUrl;
        this.temperature=temperature;


    }
    //public String getTimeTv() {return timeTv;}

    public String getDayTv() {return date;}

    public String getTime() {return time;}

    public String getImageUrl() {return imageUrl;}

    public String getTemperature() {return temperature;}
}
