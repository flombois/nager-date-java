package com.github.flombois.models;

/**
 * Enumeration of different types of holidays.
 * <p>
 * This enum represents the various categories of holidays that can be returned
 * by the Nager.Date API. A holiday can have one or more of these types.
 * </p>
 *
 * @since 1.0
 */
public enum HolidayType {

    /** A day observed as a public holiday in the country. */
    PUBLIC,
    /** A day when banks are closed. */
    BANK,
    /** A day when schools are closed. */
    SCHOOL,
    /** A day when government authorities are closed. */
    AUTHORITIES,
    /** An optional or discretionary holiday that workers may observe. */
    OPTIONAL,
    /** A day of religious or cultural observance without official holiday status. */
    OBSERVANCE,
}
