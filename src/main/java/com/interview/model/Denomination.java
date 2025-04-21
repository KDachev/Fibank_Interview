package com.interview.model;

public enum Denomination {
    TEN(10),
    TWENTY(20),
    FIFTY(50);

    private final int value;

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
