package com.paymongo.parking.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FeeUtilTest {

    private FeeUtil feeUtil;

    @BeforeEach
    void setUp() {
        feeUtil = new FeeUtil();
    }

    @Test
    @DisplayName(value = "Should Compute Base Fee")
    void computeBaseFee() {
        LocalDateTime start = LocalDateTime.parse("2022-01-01T01:00:00");
        LocalDateTime end = LocalDateTime.parse("2022-01-01T03:00:00");

        HashMap<String, String> fees =  feeUtil.compute(start, end, 40);

        assertThat(Double.parseDouble(fees.get("total_fee"))).isEqualTo(40);
    }

    @Test
    @DisplayName(value = "Should Compute Fee More Than 24 hours")
    void computeOneDay() {
        LocalDateTime start = LocalDateTime.parse("2022-01-01T01:00:00");
        LocalDateTime end = LocalDateTime.parse("2022-01-02T02:00:00");

        HashMap<String, String> fees =  feeUtil.compute(start, end, 80);

        assertThat(Double.parseDouble(fees.get("total_fee"))).isEqualTo(5080);
    }

    @Test
    @DisplayName(value = "Should Compute More Than Base Fee")
    void computeMoreThanBaseFee() {
        LocalDateTime start = LocalDateTime.parse("2022-01-01T01:00:00");
        LocalDateTime end = LocalDateTime.parse("2022-01-01T08:00:00");

        HashMap<String, String> fees =  feeUtil.compute(start, end, 100);

        assertThat(Double.parseDouble(fees.get("total_fee"))).isEqualTo(240);
    }

}