package com.cinntra.ledure.spinner;

public class SpinnerLocal {

    private String name;
    private String employeeId;

    public SpinnerLocal(String name, String employeeId) {
        this.name = name;
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    // Override the toString method so the name is displayed in the spinner
    @Override
    public String toString() {
        return name;
    }
}
