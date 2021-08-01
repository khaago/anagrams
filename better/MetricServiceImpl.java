public class MetricServiceImpl implements MetricService {
    private long startTime;
    private long endTime;

    public void logStartTime() {
        this.startTime = System.currentTimeMillis();
        this.endTime = 0L;  // reset end time
    }

    public void logEndTime() {
        this.endTime = System.currentTimeMillis();
    }

    public void printDuration(String format) {
        long duration = endTime - startTime;
        System.out.printf(format + " %dms\n", duration);
    }

    public long getDurationInMillis() {
        return this.endTime - this.startTime;
    }
}
