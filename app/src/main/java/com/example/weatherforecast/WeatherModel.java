package com.example.weatherforecast;

public class WeatherModel {

    private String date;


    private String city;

    private String image;

    private String desc;

    private String wind;

    private String temp;

    private String humidity;

    public WeatherModel(String date,
                        String city, String image, String desc,
                        String wind, String temp, String humidity ){

        this.date=date;
        this.city=city;
        this.image=image;
        this.desc=desc;
        this.wind=wind;
        this.temp=temp;
        this.humidity=humidity;
    }
    public String getDate() {return date;}

    public String getCity() {return city;}

    public String getImage() {return image;}

    public String getDesc() {return desc;}

    public String getWind() {return wind;}

    public String getTemp() {return temp;}

    public String getHumidity() {return humidity;}
}
