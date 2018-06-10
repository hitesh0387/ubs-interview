package com.ubs.opsit.interviews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeConverterImpl implements TimeConverter {

    private static final String EMPTY_STRING = "";
    private static final String OFF_STATE = "O";
    private static final String NEW_LINE_STRING = "\r\n";
    private static final String[] SECONDS_ROW = {"Y"};
    private static final String[] HOURS_ROW = {"R", "R", "R", "R"};
    private static final String[] FIRST_ROW_OF_MINUTES = {"Y", "Y", "R", "Y", "Y", "R", "Y", "Y", "R", "Y", "Y"};
    private static final String[] SECOND_ROW_OF_MINUTES = {"Y", "Y", "Y", "Y"};
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeConverterImpl.class);

    /**
     * Method convertTime converts the passed input time into its equivalent Berlin clock representation
     *
     * @param aTime Time in HH:mm:ss format
     * @return Berlin clock representation of the passed input
     */
    @Override
    public String convertTime(final String aTime) {

        final StringBuilder convertedTime = new StringBuilder(EMPTY_STRING);

        if (aTime == null || aTime.isEmpty() || !isTimeValid(aTime)) {
            return convertedTime.toString();
        }

        final int[] timeComponents = extractTimeComponents(aTime);

        //Second conversion
        convertedTime.append(convertSeconds(timeComponents[2]));
        convertedTime.append(NEW_LINE_STRING);

        //Hours conversion
        convertedTime.append(convertHours(timeComponents[0]));
        convertedTime.append(NEW_LINE_STRING);

        //Minutes conversion
        convertedTime.append(convertMinutes(timeComponents[1]));

        return convertedTime.toString();
    }

    /**
     * Method isTimeValid validates if the passed input is in format HH:mm:ss
     *
     * @param aTime time to be validated
     * @return true - if the passed time is valid & false otherwise
     */
    private boolean isTimeValid(final String aTime) {
        try {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            simpleDateFormat.parse(aTime);
            return true;
        } catch (ParseException e) {
            LOGGER.error("Invalid time: {}", aTime, e);
            return false;
        }
    }

    /**
     * Method extractTimeComponents splits the passed input time to extract the hours, minutes and seconds components
     *
     * @param aTime passed input time in HH:mm:ss format
     * @return integer array where elements are in order: hours, minutes and seconds
     */
    private int[] extractTimeComponents(final String aTime) {

        final String[] timeComponents = aTime.split("\\:");
        final int[] integerTimeComponents = new int[timeComponents.length];

        for (int i = 0; i < timeComponents.length; i++) {
            integerTimeComponents[i] = Integer.parseInt(timeComponents[i]);
        }

        return integerTimeComponents;
    }

    /**
     * Method converts the passed seconds to it's equivalent Berlin clock representation
     *
     * @param seconds seconds value in ss (00-59) format
     * @return converted seconds
     */
    private String convertSeconds(int seconds) {

        if (seconds % 2 == 0) {
            return SECONDS_ROW[0];
        }

        return OFF_STATE;
    }

    /**
     * Method converts the passed hours to it's equivalent Berlin clock representation
     *
     * @param hours hours in HH (00-24) format
     * @return converted hours
     */
    private String convertHours(final int hours) {

        final StringBuilder convertedHours = new StringBuilder(EMPTY_STRING);
        final int firstRow = hours / 5;
        final int secondRow = hours % 5;

        convertedHours.append(convertToBerlinFormat(firstRow, HOURS_ROW));
        convertedHours.append(appendZeroes(HOURS_ROW.length - firstRow));
        convertedHours.append(NEW_LINE_STRING);
        convertedHours.append(convertToBerlinFormat(secondRow, HOURS_ROW));
        convertedHours.append(appendZeroes(HOURS_ROW.length - secondRow));

        return convertedHours.toString();
    }

    /**
     * Method converts the passed minutes to it's equivalent Berlin clock representation
     *
     * @param minutes minutes in mm (00-59) format
     * @return converted minutes
     */
    private String convertMinutes(final int minutes) {

        final StringBuilder convertedMinutes = new StringBuilder(EMPTY_STRING);
        final int firstRow = minutes / 5;
        final int secondRow = minutes % 5;

        convertedMinutes.append(convertToBerlinFormat(firstRow, FIRST_ROW_OF_MINUTES));
        convertedMinutes.append(appendZeroes(FIRST_ROW_OF_MINUTES.length - firstRow));
        convertedMinutes.append(NEW_LINE_STRING);
        convertedMinutes.append(convertToBerlinFormat(secondRow, SECOND_ROW_OF_MINUTES));
        convertedMinutes.append(appendZeroes(SECOND_ROW_OF_MINUTES.length - secondRow));

        return convertedMinutes.toString();
    }

    /**
     * Method convertToBerlinFormat creates the Berlin clock representation of a time based on the active bits
     *
     * @param activeBits number of bits to be activated
     * @param timeRow    Berlin clock row representation of time
     * @return Berlin clock bit states
     */
    private String convertToBerlinFormat(int activeBits, String[] timeRow) {

        final StringBuilder timeString = new StringBuilder(EMPTY_STRING);

        for (int i = 0; i < activeBits; i++) {
            timeString.append(timeRow[i]);
        }

        return timeString.toString();
    }

    /**
     * Method appendZeroes appends the off states to a string
     *
     * @param numberOfZeroes number of zeroes to be appended
     * @return off states
     */
    private String appendZeroes(final int numberOfZeroes) {

        final StringBuilder timeString = new StringBuilder(EMPTY_STRING);

        for (int i = 0; i < numberOfZeroes; i++) {
            timeString.append(OFF_STATE);
        }

        return timeString.toString();
    }
}
