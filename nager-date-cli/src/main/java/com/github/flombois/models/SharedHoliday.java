package com.github.flombois.models;

import java.time.LocalDate;

/**
 * Represents a public holiday date celebrated in two countries, with the local name from each.
 *
 * @param date       the shared holiday date
 * @param localName1 the local name in the first country
 * @param localName2 the local name in the second country
 * @since 1.0
 */
public record SharedHoliday(LocalDate date, String localName1, String localName2) {}
