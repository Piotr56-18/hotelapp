package com.piotr.springboot.hotelapp.domain.room;

import com.piotr.springboot.hotelapp.util.SystemUtils;

public enum BedType {

        SINGLE(SystemUtils.SINGLE),
        DOUBLE(SystemUtils.DOUBLE),
        KING_SIZE(SystemUtils.KING_SIZE),;

        private final String asStr;

        BedType(String asStr) {
            this.asStr = asStr;
        }

        @Override
        public String toString() {
            return this.asStr;
        }
    }
