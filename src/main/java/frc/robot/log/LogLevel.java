package frc.robot.log;

public enum LogLevel {
    VALUE(1),
    INFO(2),
    WARNING(3),
    ERROR(4),
    WTF_JUST_WENT_WRONG(5);

    private int id;

    private LogLevel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static LogLevel fromId(int id) {
        return switch (id) {
            case 1 -> LogLevel.VALUE;
            case 2 -> LogLevel.INFO;
            case 3 -> LogLevel.WARNING;
            case 4 -> LogLevel.ERROR;
            case 5 -> LogLevel.WTF_JUST_WENT_WRONG;
            default ->throw new IllegalStateException("not a valid id");
        };
    }
}
