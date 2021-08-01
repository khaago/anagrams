public interface MetricService {
    void logStartTime();

    void logEndTime();

    long getDurationInMillis();

    void printDuration(String format);
}
