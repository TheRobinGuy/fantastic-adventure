package com.example.API.Dto;

public class User {

    private final String userName;
    private final String surName;
    private final String firstName;
    private final Boolean gender; //True is Male, False Female

    public User(Builder builder) {
        this.userName = builder.userName;
        this.surName = builder.surName;
        this.firstName = builder.firstName;
        this.gender = builder.gender;
    }

    public String getUserName() {
        return userName;
    }

    public String getSurName() {
        return surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Boolean getGender() {
        return gender;
    }

    class Builder{

        String userName;
        String surName;
        String firstName;
        Boolean gender; //True is Male, False Female

        void withUserName(final String userName){
            this.userName = userName;
        }

        void withSurName(final String surName){
            this.surName = surName;
        }

        void withFirstName(final String firstName){
            this.firstName = firstName;
        }

        void withGender(final Boolean gender){
            this.gender = gender;
        }

        Builder build(){
            return this;
        }
    }

}
