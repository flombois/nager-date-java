package com.github.flombois.models.v3;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a long weekend opportunity from the Nager.Date API v3.
 * <p>
 * This model contains information about consecutive non-working days that could form
 * a long weekend, including whether additional days off ("bridge days") are needed to
 * extend the break.
 * </p>
 *
 * @since 1.0
 */
public class LongWeekendV3 {

    private String[] bridgeDays;
    private int dayCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean needBridgeDay;

    /**
     * Returns the bridge days needed to form this long weekend.
     *
     * @return an array of bridge day identifiers
     */
    public String[] getBridgeDays() {
        return bridgeDays;
    }

    /**
     * Sets the bridge days needed to form this long weekend.
     *
     * @param bridgeDays an array of bridge day identifiers
     */
    public void setBridgeDays(String[] bridgeDays) {
        this.bridgeDays = bridgeDays;
    }

    /**
     * Returns the total number of consecutive days in this long weekend.
     *
     * @return the day count
     */
    public int getDayCount() {
        return dayCount;
    }

    /**
     * Sets the total number of consecutive days in this long weekend.
     *
     * @param dayCount the day count
     */
    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    /**
     * Returns the start date of the long weekend.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the long weekend.
     *
     * @param startDate the start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the long weekend.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the long weekend.
     *
     * @param endDate the end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns whether a bridge day (day off) is needed to form this long weekend.
     *
     * @return {@code true} if a bridge day is required, {@code false} otherwise
     */
    public boolean isNeedBridgeDay() {
        return needBridgeDay;
    }

    /**
     * Sets whether a bridge day is needed to form this long weekend.
     *
     * @param needBridgeDay {@code true} if a bridge day is required
     */
    public void setNeedBridgeDay(boolean needBridgeDay) {
        this.needBridgeDay = needBridgeDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongWeekendV3 that)) return false;
        return dayCount == that.dayCount && needBridgeDay == that.needBridgeDay
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Arrays.equals(bridgeDays, that.bridgeDays);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dayCount, startDate, endDate, needBridgeDay);
        result = 31 * result + Arrays.hashCode(bridgeDays);
        return result;
    }
}
