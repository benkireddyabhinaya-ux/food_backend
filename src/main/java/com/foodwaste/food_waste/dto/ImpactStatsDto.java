package com.foodwaste.food_waste.dto;

public class ImpactStatsDto {
    private long totalDonations;
    private long activeDonations;
    private long completedPickups;
    private double totalKgFoodSaved;
    private long totalMealsSaved;
    private double totalCo2PreventedKg;
    private long totalDonors;
    private long totalOrganizations;

    public ImpactStatsDto() {}

    public long getTotalDonations() { return totalDonations; }
    public void setTotalDonations(long totalDonations) { this.totalDonations = totalDonations; }

    public long getActiveDonations() { return activeDonations; }
    public void setActiveDonations(long activeDonations) { this.activeDonations = activeDonations; }

    public long getCompletedPickups() { return completedPickups; }
    public void setCompletedPickups(long completedPickups) { this.completedPickups = completedPickups; }

    public double getTotalKgFoodSaved() { return totalKgFoodSaved; }
    public void setTotalKgFoodSaved(double totalKgFoodSaved) { this.totalKgFoodSaved = totalKgFoodSaved; }

    public long getTotalMealsSaved() { return totalMealsSaved; }
    public void setTotalMealsSaved(long totalMealsSaved) { this.totalMealsSaved = totalMealsSaved; }

    public double getTotalCo2PreventedKg() { return totalCo2PreventedKg; }
    public void setTotalCo2PreventedKg(double totalCo2PreventedKg) { this.totalCo2PreventedKg = totalCo2PreventedKg; }

    public long getTotalDonors() { return totalDonors; }
    public void setTotalDonors(long totalDonors) { this.totalDonors = totalDonors; }

    public long getTotalOrganizations() { return totalOrganizations; }
    public void setTotalOrganizations(long totalOrganizations) { this.totalOrganizations = totalOrganizations; }
}
