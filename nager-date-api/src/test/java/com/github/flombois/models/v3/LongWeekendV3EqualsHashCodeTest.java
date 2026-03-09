package com.github.flombois.models.v3;

import com.github.flombois.models.AbstractEqualsHashCodeTest;

import java.time.LocalDate;

class LongWeekendV3EqualsHashCodeTest extends AbstractEqualsHashCodeTest<LongWeekendV3> {

    @Override
    protected LongWeekendV3 createInstance() {
        var lw = new LongWeekendV3();
        lw.setStartDate(LocalDate.of(2026, 5, 1));
        lw.setEndDate(LocalDate.of(2026, 5, 3));
        lw.setDayCount(3);
        lw.setNeedBridgeDay(false);
        lw.setBridgeDays(new String[]{});
        return lw;
    }

    @Override
    protected LongWeekendV3 createEqualInstance() {
        var lw = new LongWeekendV3();
        lw.setStartDate(LocalDate.of(2026, 5, 1));
        lw.setEndDate(LocalDate.of(2026, 5, 3));
        lw.setDayCount(3);
        lw.setNeedBridgeDay(false);
        lw.setBridgeDays(new String[]{});
        return lw;
    }

    @Override
    protected LongWeekendV3 createThirdEqualInstance() {
        var lw = new LongWeekendV3();
        lw.setStartDate(LocalDate.of(2026, 5, 1));
        lw.setEndDate(LocalDate.of(2026, 5, 3));
        lw.setDayCount(3);
        lw.setNeedBridgeDay(false);
        lw.setBridgeDays(new String[]{});
        return lw;
    }

    @Override
    protected LongWeekendV3 createDifferentInstance() {
        var lw = new LongWeekendV3();
        lw.setStartDate(LocalDate.of(2026, 12, 25));
        lw.setEndDate(LocalDate.of(2026, 12, 28));
        lw.setDayCount(4);
        lw.setNeedBridgeDay(true);
        lw.setBridgeDays(new String[]{"2026-12-26"});
        return lw;
    }
}
