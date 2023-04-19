package com.piotr.springboot.hotelapp.domain.guest;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class GuestDTO {
    private String firstName;
    private String lastName;
    private int age;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public GuestDTO(){};

    public GuestDTO(String firstName, String lastName, int age, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", age: " + age + ", gender: " + gender;
    }
}
