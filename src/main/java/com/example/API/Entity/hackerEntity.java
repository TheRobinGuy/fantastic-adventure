package com.example.API.Entity;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class hackerEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private final Integer id;
//    private final String hackerName;
//    private final String surName;
//    private final String firstName;
//    private final Boolean gender; //True is Male, False Female

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hackerName;
    private String surName;
    private String firstName;
    private Boolean gender; //True is Male, False Female

    public hackerEntity(){}

    private hackerEntity(Builder builder) {
        this.id = builder.id;
        this.hackerName = builder.hackerName;
        this.surName = builder.surName;
        this.firstName = builder.firstName;
        this.gender = builder.gender;
    }

    public Integer getId(){
        return id;
    }

    public String getHackerName() {
        return hackerName;
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

    public static class Builder{
        Integer id;
        String hackerName;
        String surName;
        String firstName;
        Boolean gender; //True is Male, False Female

        public Builder withId(final Integer id){
            this.id = id;
            return this;
        }

        public Builder withUserName(final String userName){
            this.hackerName = userName;
            return this;
        }

        public Builder withSurName(final String surName){
            this.surName = surName;
            return this;
        }

        public Builder withFirstName(final String firstName){
            this.firstName = firstName;
            return this;
        }

        public Builder withGender(final Boolean gender){
            this.gender = gender;
            return this;
        }

        public hackerEntity build(){
            return new hackerEntity(this);
        }
    }
}
