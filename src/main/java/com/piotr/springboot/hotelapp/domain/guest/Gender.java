package com.piotr.springboot.hotelapp.domain.guest;

import com.piotr.springboot.hotelapp.util.SystemUtils;

public enum Gender {
    MALE(SystemUtils.MALE),
    FEMALE(SystemUtils.FEMALE);

    private final String asStr;

    Gender(String asStr) {
        this.asStr = asStr;
    }

    @Override
    public String toString() {
        return this.asStr;
    }
}

