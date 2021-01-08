package net.skeagle.vrnbot.utils;

import net.skeagle.vrnbot.handlers.exceptions.VRNException;

import java.math.BigInteger;
import java.util.regex.Pattern;

public class TimeUtil {

    private static final Pattern ALLOWED_VALUES = Pattern.compile("[smhdy]");

    public static String timeToMessage(final String time) {
        BigInteger bigint = new BigInteger(time);
        System.out.println(time);
        final BigInteger years = bigint.divide(new BigInteger("31536000"));
        final BigInteger days = (bigint.mod(new BigInteger("31536000"))).divide(new BigInteger("86400"));
        BigInteger moddays = bigint.mod(new BigInteger("31536000")).mod(new BigInteger("86400"));
        final BigInteger hours = moddays.divide(new BigInteger("3600"));
        final BigInteger modhours = moddays.mod(new BigInteger("3600"));
        final BigInteger minutes = modhours.divide(new BigInteger("60"));
        final BigInteger seconds = modhours.mod(new BigInteger("60"));
        String s = ((years.intValue() != 0) ? years + " " + timeGrammarCheck("year", years) + (days.intValue() != 0 || hours.intValue() != 0 || minutes.intValue() != 0 || seconds.intValue() != 0 ? ", " : "") : "") +
                ((days.intValue() != 0) ? days + " " + timeGrammarCheck("day", days) + (hours.intValue() != 0 || minutes.intValue() != 0 || seconds.intValue() != 0 ? ", " : "") : "") +
                ((hours.intValue() != 0) ? hours + " " + timeGrammarCheck("hour", hours) + (minutes.intValue() != 0 || seconds.intValue() != 0 ? ", " : "") : "") +
                ((minutes.intValue() != 0) ? minutes + " " + timeGrammarCheck("minute", minutes) + (seconds.intValue() != 0 ? ", " : "") : "") +
                ((seconds.intValue() != 0) ? seconds + " " + timeGrammarCheck("second", seconds) : "");
        if (s.equals(""))
            return "less than a second";
        return s;
    }

    public static String timeToMessage(final long time) {
        return timeToMessage(Long.toString(time));
    }

    public static String timeToMessage(final int time) {
        return timeToMessage(Long.toString(time));
    }

    private static String timeGrammarCheck(final String s, final BigInteger i) {
        if (i.intValue() != 1)
            return s + "s";
        return s;
    }

    private enum TimeUnits {
        s(1),
        m(60),
        h(3600),
        d(86400),
        y(31536000);

        private final long time_seconds;

        TimeUnits(final long time_seconds) {
            this.time_seconds = time_seconds;
        }
    }

    public static String parseTimeString(String s) {
        s = s.replaceAll("[^a-zA-Z0-9]","");
        s = s.toLowerCase();
        BigInteger bigint = BigInteger.ZERO, bigint2;
        String nums = "";
        String tempunit = "";
        int matches = 0;
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
                if (ALLOWED_VALUES.matcher(Character.toString(c[i])).matches()) {
                    if (nums.equals(""))
                        throw new VRNException("Error in position " + (i + 1) + " of the time, you must provide a number before a time unit.");
                    matches += 1;
                    bigint2 = new BigInteger(nums);
                    bigint2 = bigint2.multiply(new BigInteger(convertUnitToTime(c[i])));
                    bigint = bigint.add(bigint2);
                    tempunit = Character.toString(c[i]);
                    nums = "";
                }
                else if (Character.getNumericValue(c[i]) > -1) {
                    nums += c[i];
                    tempunit = "";
                }
                else {
                    if (!ALLOWED_VALUES.matcher(Character.toString(c[i])).matches() && Character.getNumericValue(c[i]) >= 10)
                        throw new VRNException("Error in position " + (i + 1) + " of the time, unknown time unit. The accepted values are seconds (s), minutes (m), hours (h), days (d), or years (y).");
                    throw new VRNException("Error in parsing the time, please check if you formatted the time correctly.");
                }
        }
        if (!nums.equals("") && matches == 0 || tempunit.equals("")) {
            if (!ALLOWED_VALUES.matcher(s.substring(c.length - 1)).matches() && Character.getNumericValue(s.charAt(c.length - 1)) >= 10)
                throw new VRNException("Error in position " + c.length + " of the time, unknown time unit. The accepted values are seconds (s), minutes (m), hours (h), days (d), or years (y).");
            throw new VRNException("Error in position " + c.length + " of the time, you must specify the time unit.");
        }
        return timeToMessage(bigint.toString());
    }

    private static String convertUnitToTime(char c) {
        for (TimeUnits t : TimeUnits.values())
            if (Character.toString(c).equalsIgnoreCase(t.name()))
                return Long.toString(t.time_seconds);
        return "1";
    }
}
