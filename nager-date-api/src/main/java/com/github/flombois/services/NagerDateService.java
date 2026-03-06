package com.github.flombois.services;

public interface NagerDateService {

    default String name() {
        return this.getClass().getName();
    }
}
