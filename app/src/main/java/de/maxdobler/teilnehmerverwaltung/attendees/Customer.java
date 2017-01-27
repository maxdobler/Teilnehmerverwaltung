package de.maxdobler.teilnehmerverwaltung.attendees;

public class Customer {

    private String name;
    private int quota = 0;

    public Customer() {
    }

    public Customer(String name, int quota) {
        this.name = name;
        this.quota = quota;
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

    public void addQuota(int numberOfDays) {
        this.quota += numberOfDays;
    }

    public void removeQuota(int numberOfDays) {
        this.quota -= numberOfDays;
    }

    public boolean hasQuota() {
        return this.quota > 0;
    }
}
