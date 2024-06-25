package constants;

public class RegularExpression {
    public static final String POTION_PATTERN_STR =
            "(\\d{4}/(0[1-9]|1[0-2]))-([^\\s@#\\-]+)-([^\\s@#\\-]+)";

    public static final String SINGLE_ATTACK_PATTERN_STR =
            "(\\d{4}/(0[1-9]|1[0-2]))-([^@\\s#\\-]{1,40})@([^@\\s#\\-]{1,40})-([^@\\s#\\-]{1,40})";

    public static final String AOE_ATTACK_PATTERN_STR =
            "(\\d{4}/(0[1-9]|1[0-2]))-([^\\s@#\\-]+)@#-([^\\s@#\\-]+)";
}
