/*
 * Copyright (C) 2016 Charles Brophy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.granite.math;

import com.google.common.collect.ImmutableSortedMap;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class CDFTools<T extends Number> {
    private final static int NO_EPOCH = -1;
    private final int minutesPerEpoch;
    private final int totalEpochs;

    private LocalDateTime minimumTimestamp;
    private LocalDateTime maximumTimestamp;

    private final List<EpochValue> epochValues = new ArrayList<>();

    public CDFTools(
            final int minutesPerEpoch,
            final LocalDateTime minimumTimestamp,
            final LocalDateTime maximumTimestamp) {
        checkArgument(minutesPerEpoch > 0, "minutesPerEpoch must be positive");
        this.minutesPerEpoch = minutesPerEpoch;
        this.minimumTimestamp = checkNotNull(minimumTimestamp, "minimumTimestamp");
        this.maximumTimestamp = checkNotNull(maximumTimestamp, "maximumTimestamp");
        checkArgument(this.minimumTimestamp.isBefore(this.maximumTimestamp), "minimumTimestamp must come before maximumTimestamp");
        this.totalEpochs = calculateTotalEpochs(minimumTimestamp, maximumTimestamp, minutesPerEpoch);

    }

    private static int calculateTotalEpochs(final LocalDateTime minimumTimestamp,
                                            final LocalDateTime maximumTimestamp,
                                            final int epochMinutes) {
        checkNotNull(minimumTimestamp, "minimumTimestamp");
        checkNotNull(maximumTimestamp, "maximumTimestamp");
        checkArgument(epochMinutes > 0, "minutesPerEpoch must be positive");

        return ((int) Duration.between(minimumTimestamp, maximumTimestamp).toMinutes()) / epochMinutes;
    }

    private int calculateTimestampEpoch(final LocalDateTime timestamp) {
        checkNotNull(timestamp, "timestamp");

        if (timestamp.isBefore(this.minimumTimestamp) || timestamp.isAfter(this.maximumTimestamp))
            return NO_EPOCH;

        final double minutesBetween = (double) Duration.between(this.minimumTimestamp, timestamp).toMinutes();

        return (int) Math.floor(minutesBetween / (double) minutesPerEpoch);
    }

    public void add(final LocalDateTime timestamp, final T value) {
        epochValues.add(new EpochValue(timestamp, value));
    }

    public void clear() {
        epochValues.clear();
    }

    public ImmutableSortedMap<Integer, Double> generateSumCDF() {
        if (epochValues.isEmpty()) return ImmutableSortedMap.of();

        final TreeMap<Integer, Double> totals = new TreeMap<>();

        for (EpochValue epochValue : epochValues) {

            final int epoch = calculateTimestampEpoch(epochValue.timestamp);

            final double value = epochValue.value.doubleValue();

            if (epoch == NO_EPOCH) continue;

            final Double current = totals.get(epoch);

            totals.put(epoch, current == null ? epochValue.value.doubleValue() : value + current);
        }

        final ImmutableSortedMap.Builder<Integer, Double> builder = ImmutableSortedMap.naturalOrder();

        double currentTotal = 0.0;

        for (int epoch = 0; epoch < totalEpochs; epoch++) {
            currentTotal += totals.getOrDefault(epoch, 0.0);
            builder.put(epoch, currentTotal);
        }

        return builder.build();
    }

    public ImmutableSortedMap<Integer, Double> generateDecimalCDF() {
        final ImmutableSortedMap<Integer, Double> sumCDF = generateSumCDF();

        final double total = sumCDF.lastEntry().getValue();

        if (Double.isNaN(1.0 / total)) return ImmutableSortedMap.of();

        final ImmutableSortedMap.Builder<Integer, Double> builder = ImmutableSortedMap.naturalOrder();

        for (Map.Entry<Integer, Double> epochSumEntry : sumCDF.entrySet()) {
            builder.put(epochSumEntry.getKey(), epochSumEntry.getValue() / total);
        }

        return builder.build();
    }

    public int getMinutesPerEpoch() {
        return minutesPerEpoch;
    }

    public int getTotalEpochs() {
        return totalEpochs;
    }

    public LocalDateTime getMinimumTimestamp() {
        return minimumTimestamp;
    }

    public LocalDateTime getMaximumTimestamp() {
        return maximumTimestamp;
    }

    private class EpochValue {
        private LocalDateTime timestamp;
        private T value;

        public EpochValue(final LocalDateTime timestamp, final T value) {
            checkNotNull(timestamp, "timestamp");
            checkNotNull(value, "value");
            checkArgument(value.doubleValue() >= 0.0, "All values in CDF must be positive or zero");

            this.timestamp = timestamp;
            this.value = value;
        }
    }
}
