package prak.travelerapp.WeatherAPI.model;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by Michael on 24.11.15.
 */
public class Day implements Serializable{

    public Temperature temperature = new Temperature();
    public Condition condition = new Condition();
    private int deg;
    private int clouds;
    private DateTime date;


    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(long timestamp){

        this.date = new DateTime(timestamp * 1000L);

    }


    public  class Temperature {
        private float minTemp;
        private float maxTemp;
        private float dayTemp;
        private float nightTemp;
        private float mornTemp;
        private float eveTemp;

        public float getEveTemp() {
            return eveTemp;
        }

        public void setEveTemp(float eveTemp) {
            this.eveTemp = eveTemp;
        }

        public float getMornTemp() {
            return mornTemp;
        }

        public void setMornTemp(float mornTemp) {
            this.mornTemp = mornTemp;
        }

        public float getNightTemp() {
            return nightTemp;
        }

        public void setNightTemp(float nightTemp) {
            this.nightTemp = nightTemp;
        }

        public float getDayTemp() {
            return dayTemp;
        }

        public void setDayTemp(float dayTemp) {
            this.dayTemp = dayTemp;
        }

        public float getMinTemp() {
            return minTemp;
        }
        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }
        public float getMaxTemp() {
            return maxTemp;
        }
        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }

    }

    public class Condition{

        private String main;
        private String description;
        private String icon;

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }


    }
}
