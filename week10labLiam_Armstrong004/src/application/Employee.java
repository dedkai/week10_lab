package application;

import java.time.LocalDate;

public class Employee {

    private int id;
    private String fullName;
    private String currentAddress;
    private String contactNumber;
    private String emailAddress;
    private String education;
    private String gender;
    private LocalDate dateAvailable;
    private String desiredPosition;
    private String desiredSalary;
    private boolean legallyAuthorized;
    private boolean hasRelatives;
    private String relativesExplanation;
    private String signature;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateAvailable() {
        return dateAvailable;
    }

    public void setDateAvailable(LocalDate dateAvailable) {
        this.dateAvailable = dateAvailable;
    }

    public String getDesiredPosition() {
        return desiredPosition;
    }

    public void setDesiredPosition(String desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    public String getDesiredSalary() {
        return desiredSalary;
    }

    public void setDesiredSalary(String desiredSalary) {
        this.desiredSalary = desiredSalary;
    }

    public boolean isLegallyAuthorized() {
        return legallyAuthorized;
    }

    public void setLegallyAuthorized(boolean legallyAuthorized) {
        this.legallyAuthorized = legallyAuthorized;
    }

    public boolean isHasRelatives() {
        return hasRelatives;
    }

    public void setHasRelatives(boolean hasRelatives) {
        this.hasRelatives = hasRelatives;
    }

    public String getRelativesExplanation() {
        return relativesExplanation;
    }

    public void setRelativesExplanation(String relativesExplanation) {
        this.relativesExplanation = relativesExplanation;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}