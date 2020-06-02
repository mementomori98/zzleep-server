package zzleep.core.models;

public class SleepRating {

    private int sleepId;
    private int rating;

    public SleepRating(int sleepId, int rating) {
        this.sleepId = sleepId;
        this.rating = rating;
    }

    public int getSleepId() {
        return sleepId;
    }

    public void setSleepId(int sleepId) {
        this.sleepId = sleepId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
