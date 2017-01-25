package de.maxdobler.teilnehmerverwaltung;

public class Customer {

    private String name;
    private int quota = 0;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public void addRemainingEvents(int numberOfDays) {
        this.quota += numberOfDays;
    }

    public void removeRemainingEvents(int numberOfDays) {
        this.quota -= numberOfDays;
    }

    public boolean hasRemainingEvents() {
        return this.quota > 0;
    }
}
