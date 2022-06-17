package com.paymongo.parking.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

@Component
public class FeeUtil {

    @Getter
    @Setter
    @ToString
    class ComputationModel {
        public long minuteDiffLeft;
        public double totalFee;
        public boolean isFinished;
    }

    private final double BASE_PARKING_FEE = 40.00;
    private final double FEE_PER_DAY = 5000.00;
    private final long PER_3_HOURS = 180; /* 3 hours */
    private final long DAY_IN_MINUTES = 1440; /* 24 hours */

    private long divideRoundUp(long dividend, long divisor) {
        return (long) Math.ceil((double) dividend / divisor);
    }

    private ComputationModel computePerDay(long minuteDiff, double totalFee, double parkingSlotFee) {
        ComputationModel computationModel = new ComputationModel();

        long dayDividend = minuteDiff / DAY_IN_MINUTES;
        if (dayDividend >= 1) {
            totalFee += (dayDividend * FEE_PER_DAY);
            minuteDiff -= (dayDividend * DAY_IN_MINUTES);

            long per3hoursDividend = divideRoundUp(minuteDiff, PER_3_HOURS);
            if (per3hoursDividend > 0) {
                totalFee += (per3hoursDividend * parkingSlotFee);
            }

            computationModel.setFinished(true);
        } else {
            computationModel.setFinished(false);
        }

        computationModel.setTotalFee(totalFee);
        computationModel.setMinuteDiffLeft(minuteDiff);

        return computationModel;
    }

    private ComputationModel computePerHours(long minuteDiff, double totalFee, double parkingSlotFee) {
        ComputationModel computationModel = new ComputationModel();

        if (minuteDiff >= 0) {
            totalFee += BASE_PARKING_FEE;

            minuteDiff -= PER_3_HOURS;
            if (minuteDiff <= 0) {
                computationModel.setTotalFee(totalFee);
                computationModel.setMinuteDiffLeft(minuteDiff);
                computationModel.setFinished(true);
                return computationModel;
            }
        }

        /* compute succeeding hours */
        long hoursLeftDividend = divideRoundUp(minuteDiff, PER_3_HOURS);
        if (hoursLeftDividend > 0) {
            totalFee += (hoursLeftDividend * parkingSlotFee);
        }

        computationModel.setTotalFee(totalFee);
        computationModel.setMinuteDiffLeft((minuteDiff <= 0) ? 0 : minuteDiff);
        computationModel.setFinished(true);

        return computationModel;
    }

    public HashMap<String, String> compute(LocalDateTime startTime, LocalDateTime endTime, double parkingSlotFee) {
        HashMap<String, String> response = new HashMap<>();

        double totalFee = 0.0;

        long minuteDiff = ChronoUnit.MINUTES.between(startTime, endTime);

        /* if parking is more than 24 hours */
        ComputationModel dayComputation = computePerDay(minuteDiff, totalFee, parkingSlotFee);
        if (dayComputation.isFinished) {
            response.put("total_hours_parked", String.valueOf(divideRoundUp(minuteDiff, 60)));
            response.put("total_fee", String.valueOf(dayComputation.getTotalFee()));
            return response;
        } else {
            totalFee = dayComputation.getTotalFee();
            minuteDiff =  dayComputation.getMinuteDiffLeft();
        }

        /* compute base parking fee for first 3 and succeeding hours */
        ComputationModel hourComputation = computePerHours(minuteDiff, totalFee, parkingSlotFee);
        if (hourComputation.isFinished) {
            totalFee = hourComputation.getTotalFee();
        }

        response.put("total_hours_parked", String.valueOf(divideRoundUp(minuteDiff, 60)));
        response.put("total_fee", String.valueOf(totalFee));

        return response;
    }
}




