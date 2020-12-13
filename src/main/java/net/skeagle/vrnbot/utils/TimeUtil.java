package net.skeagle.vrnbot.utils;

import java.util.regex.Pattern;

public class TimeUtil {

    public static String timeToMessage(final long time) {
        final long hours = (time % 86400L) / 3600L;
        final long minutes = (time % 86400L % 3600L) / 60L;
        final long seconds = time % 86400L % 3600L % 60L;
        final TimeUtil util = new TimeUtil();
        return ((hours != 0) ? hours + " " + util.timeGrammarCheck("hour", hours) + (minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((minutes != 0) ? minutes + " " + util.timeGrammarCheck("minute", minutes) + (seconds != 0 ? ", " : "") : "") +
                ((seconds != 0) ? seconds + " " + util.timeGrammarCheck("second", seconds) : "");
    }

    public static String timeToMessage(final int time) {
        return timeToMessage((long) time);
    }

    private String timeGrammarCheck(final String s, final long i) {
        if (i != 1)
            return s + "s";
        return s;
    }

    private enum TimeUnits {
        s(1),
        m(60),
        h(3600);

        private final long time_seconds;

        TimeUnits(final long time_seconds) {
            this.time_seconds = time_seconds;
        }
    }

    public static class TimeString {
        private static final Pattern ALLOWED_INPUT = Pattern.compile("[smhdwy]");
        long time;
        char unit;

        public TimeString(final long time, final char unit) {
            this.time = time;
            this.unit = unit;
        }

        public static long toSeconds(final TimeString ts) {
            long inseconds = 0;
            for (final TimeUnits unit : TimeUnits.values())
                if (ts.unit == unit.name().charAt(0))
                    inseconds = unit.time_seconds;
            return ts.time * inseconds;
        }
    }
}
