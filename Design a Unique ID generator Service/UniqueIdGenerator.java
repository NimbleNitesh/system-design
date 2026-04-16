import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class UniqueIdGenerator {
    
    public static final long EPOCH = java.time.Instant.parse("2020-01-01T00:00:00Z").toEpochMilli();

    private static final int WORKER_ID_BITS = 4;
    private static final int SEQUENCE_BITS = 18;

    private static final long MAX_WORKER_ID = (1 << WORKER_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1 << SEQUENCE_BITS) - 1;

    public final long workerId;

    private long lastTimestamp = -1;
    private long sequence = 0;

    public UniqueIdGenerator(long workerId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("Worker ID must be between 0 and " + MAX_WORKER_ID);
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis() - EPOCH;
        
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }

        if (timestamp == lastTimestamp) {
            // Same millisecond, increment sequence and ensure it doesn't overflow the max sequence
            sequence = (sequence + 1) & MAX_SEQUENCE;

            if (sequence == 0) {
                // Sequence overflow, wait for next millisecond
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis() - EPOCH;
                }
            }
        } else {
            // New millisecond, reset sequence
            sequence = 0;
        }
        
        lastTimestamp = timestamp;
        return (timestamp << (WORKER_ID_BITS + SEQUENCE_BITS)) | (workerId << SEQUENCE_BITS) | sequence;
    }

    public static void main(String[] args) throws InterruptedException {
        UniqueIdGenerator generator = new UniqueIdGenerator(1);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                long id = generator.nextId();
                System.out.println("Generated ID: " + id + " at " + System.currentTimeMillis());
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Sample output:
     *
Generated ID: 832702986273947648 at 1776368662801
Generated ID: 832702986244587524 at 1776368662794
Generated ID: 832702986248781825 at 1776368662795
Generated ID: 832702986244587523 at 1776368662794
Generated ID: 832702986311696384 at 1776368662810
Generated ID: 832702986345250816 at 1776368662818
Generated ID: 832702986345250817 at 1776368662818
Generated ID: 832702986345250818 at 1776368662818
Generated ID: 832702986349445124 at 1776368662819
Generated ID: 832702986349445125 at 1776368662819
Generated ID: 832702986349445126 at 1776368662819
Generated ID: 832702986349445127 at 1776368662819
Generated ID: 832702986244587522 at 1776368662794
Generated ID: 832702986353639424 at 1776368662820
Generated ID: 832702986349445120 at 1776368662819
Generated ID: 832702986353639426 at 1776368662820
Generated ID: 832702986244587521 at 1776368662794
Generated ID: 832702986299113472 at 1776368662807
Generated ID: 832702986349445121 at 1776368662819
Generated ID: 832702986349445122 at 1776368662819
Generated ID: 832702986349445123 at 1776368662819
Generated ID: 832702986349445128 at 1776368662819
Generated ID: 832702986357833729 at 1776368662821
Generated ID: 832702986357833730 at 1776368662821
Generated ID: 832702986357833731 at 1776368662821
Generated ID: 832702986357833732 at 1776368662821
Generated ID: 832702986357833733 at 1776368662821
Generated ID: 832702986357833734 at 1776368662821
Generated ID: 832702986357833735 at 1776368662821
Generated ID: 832702986357833736 at 1776368662821
Generated ID: 832702986357833737 at 1776368662821
Generated ID: 832702986357833738 at 1776368662821
Generated ID: 832702986362028032 at 1776368662822
Generated ID: 832702986362028033 at 1776368662822
Generated ID: 832702986362028034 at 1776368662822
Generated ID: 832702986362028035 at 1776368662822
Generated ID: 832702986362028036 at 1776368662822
Generated ID: 832702986362028037 at 1776368662822
Generated ID: 832702986362028038 at 1776368662822
Generated ID: 832702986362028039 at 1776368662822
Generated ID: 832702986362028040 at 1776368662822
Generated ID: 832702986244587520 at 1776368662794
Generated ID: 832702986353639425 at 1776368662820
Generated ID: 832702986366222336 at 1776368662823
Generated ID: 832702986353639427 at 1776368662820
Generated ID: 832702986353639428 at 1776368662820
Generated ID: 832702986353639429 at 1776368662820
Generated ID: 832702986366222341 at 1776368662823
Generated ID: 832702986353639430 at 1776368662820
Generated ID: 832702986353639431 at 1776368662820
Generated ID: 832702986366222343 at 1776368662823
Generated ID: 832702986357833728 at 1776368662821
Generated ID: 832702986362028041 at 1776368662822
Generated ID: 832702986366222337 at 1776368662823
Generated ID: 832702986366222338 at 1776368662823
Generated ID: 832702986370416641 at 1776368662824
Generated ID: 832702986366222339 at 1776368662823
Generated ID: 832702986366222340 at 1776368662823
Generated ID: 832702986366222342 at 1776368662823
Generated ID: 832702986366222344 at 1776368662823
Generated ID: 832702986366222345 at 1776368662823
Generated ID: 832702986366222346 at 1776368662823
Generated ID: 832702986370416640 at 1776368662824
Generated ID: 832702986370416649 at 1776368662824
Generated ID: 832702986370416642 at 1776368662824
Generated ID: 832702986370416643 at 1776368662824
Generated ID: 832702986370416644 at 1776368662824
Generated ID: 832702986370416645 at 1776368662824
Generated ID: 832702986370416646 at 1776368662824
Generated ID: 832702986370416647 at 1776368662824
Generated ID: 832702986370416648 at 1776368662824
Generated ID: 832702986370416650 at 1776368662824
Generated ID: 832702986374610944 at 1776368662825
Generated ID: 832702986374610945 at 1776368662825
Generated ID: 832702986374610946 at 1776368662825
Generated ID: 832702986374610947 at 1776368662825
Generated ID: 832702986378805249 at 1776368662826
Generated ID: 832702986378805250 at 1776368662826
Generated ID: 832702986378805251 at 1776368662826
Generated ID: 832702986378805252 at 1776368662826
Generated ID: 832702986378805253 at 1776368662826
Generated ID: 832702986378805254 at 1776368662826
Generated ID: 832702986378805255 at 1776368662826
Generated ID: 832702986382999552 at 1776368662827
Generated ID: 832702986382999553 at 1776368662827
Generated ID: 832702986382999554 at 1776368662827
Generated ID: 832702986382999555 at 1776368662827
Generated ID: 832702986382999556 at 1776368662827
Generated ID: 832702986382999557 at 1776368662827
Generated ID: 832702986382999558 at 1776368662827
Generated ID: 832702986382999559 at 1776368662827
Generated ID: 832702986374610948 at 1776368662825
Generated ID: 832702986374610949 at 1776368662825
Generated ID: 832702986374610950 at 1776368662825
Generated ID: 832702986248781824 at 1776368662795
Generated ID: 832702986374610951 at 1776368662825
Generated ID: 832702986374610952 at 1776368662825
Generated ID: 832702986374610953 at 1776368662825
Generated ID: 832702986374610954 at 1776368662825
Generated ID: 832702986378805248 at 1776368662826
     */
}