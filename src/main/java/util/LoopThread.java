package util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by snb on 2017/9/8  14:16
 */
public class LoopThread {
    private boolean flag;
    private Future<String> future;
    private int outtimes;
    private int times;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Callable<String> callable = new Callable<String>() {
        @Override
        public String call() throws Exception {
            while (flag)
                onehour();
            return "LoopTank正常结束了";
        }
    };
    private long BEGIN;
    private static final LoopThread LOOP_THREAD = new LoopThread();

    private LoopThread() {
    }

    public static LoopThread getLoopThread() {
        return getLoopThread(10);
    }

    public static synchronized LoopThread getLoopThread(int outtimes) {
        if (LOOP_THREAD.future == null || LOOP_THREAD.executorService.isShutdown()) {
            LOOP_THREAD.BEGIN = System.currentTimeMillis();
            LOOP_THREAD.outtimes = outtimes;
            LOOP_THREAD.flag = true;
            LOOP_THREAD.clear();
            LOOP_THREAD.executorService = Executors.newSingleThreadExecutor();
            LOOP_THREAD.future = LOOP_THREAD.executorService.submit(LOOP_THREAD.callable);
        }
        return LOOP_THREAD;
    }

    private void onehour() {
        LoopTanker.LoopTankStream(LoopTanker.hourtank);
        long end = System.currentTimeMillis() + 3600000;
        while (end > System.currentTimeMillis() && flag)
            TenofOneSecond();
    }

    private void TenofOneSecond() {
        try {
            LoopTanker.LoopTankStream(LoopTanker.secondtank);
            executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
            if (size() != 0) {
                times = 0;
                return;
            }
            times++;
            if (times >= outtimes) {
                flag = false;
                executorService.shutdown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class LoopTanker implements Runnable {
        private Long begin;
        private Long end;
        private int step;
        private Long last;
        private Runnable runnable;
        private static final Set<LoopTanker> secondtank = new HashSet<>();
        private static final Set<LoopTanker> hourtank = new HashSet<>();

        public LoopTanker(Long end, int step, Runnable runnable) {
            last = begin = System.currentTimeMillis();
            this.end = end;
            this.step = step;
            this.runnable = runnable;
        }

        private static void LoopTankStream(Set<LoopTanker> set) {
            long now = System.currentTimeMillis();
            Set<LoopTanker> s = new HashSet<>();
            set.stream().filter(a -> (a.end > 0 && a.end < now)).forEach(a -> s.add(a));
            set.removeAll(s);
            set.stream().filter(a -> ((a.last + a.step) <= now)).forEach(a -> {
                a.run();
                a.last = now;
            });
        }

        private static LoopTanker getLoopTanker(Runnable runnable) {
            long now = System.currentTimeMillis();
            LoopTanker lt = cleanAndGet(now, runnable, hourtank);
            LoopTanker lt2 = cleanAndGet(now, runnable, secondtank);
            return lt == null ? lt2 : lt;
        }

        private static LoopTanker cleanAndGet(long now, Runnable runnable, Set<LoopTanker> set) {
            for (LoopTanker loopTanker : set) {
                if (loopTanker.end > 0 && loopTanker.end < now) {
                    set.remove(loopTanker);
                    continue;
                }
                if (loopTanker.runnable == runnable)
                    return loopTanker;
            }
            return null;
        }

        @Override
        public void run() {
            runnable.run();
        }
    }

    public synchronized boolean removeLoopTank(Runnable runnable) {
        for (LoopTanker loopTanker : LoopTanker.hourtank) {
            if (loopTanker.runnable == runnable)
                return LoopTanker.hourtank.remove(loopTanker);
        }
        for (LoopTanker loopTanker : LoopTanker.secondtank) {
            if (loopTanker.runnable == runnable)
                return LoopTanker.secondtank.remove(loopTanker);
        }
        return false;
    }

    public boolean addLoopTankByTenofOneSecond(Runnable runnable, int TenofOneSecond, long end) {
        return addLoopTank(runnable, TenofOneSecond, end, 100, LoopTanker.secondtank);
    }

    public boolean addLoopTankBySec(Runnable runnable, int second, long end) {
        return addLoopTank(runnable, second, end, 1000, LoopTanker.secondtank);
    }

    public boolean addLoopTankByHour(Runnable runnable, int hour, long end) {
        return addLoopTank(runnable, hour, end, 3600000, LoopTanker.hourtank);
    }

    private synchronized boolean addLoopTank(Runnable runnable, int hour, long end, int d, Set<LoopTanker> set) {
        if (hour <= 0)
            return false;
        if (end > 0 && end < System.currentTimeMillis())
            return false;
        if (LoopTanker.getLoopTanker(runnable) != null)
            return false;
        LoopTanker l = new LoopTanker(end, hour * d, runnable);
        return set.add(l);
    }

    public String shutdown() {
        flag = false;
        clear();
        executorService.shutdown();
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long runTime() {
        return System.currentTimeMillis() - BEGIN;
    }

    public long runTime(Runnable runnable) {
        LoopTanker loopTanker = LoopTanker.getLoopTanker(runnable);
        if (loopTanker == null) return -1;
        return System.currentTimeMillis() - loopTanker.begin;
    }

    public synchronized void clear() {
        LoopTanker.secondtank.clear();
        LoopTanker.hourtank.clear();
    }

    public int size() {
        return LoopTanker.hourtank.size() + LoopTanker.secondtank.size();
    }
}
