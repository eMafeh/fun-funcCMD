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
    private long BEGIN;
    private int outtimes;
    private int times;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Callable<String> callable = () -> {
        while (flag)
            onehour();
        return "LoopTank正常结束了";
    };

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
            LoopTanker.removeOrder();
            LoopTanker.addOrder();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class LoopTanker {
        private Long begin;
        private Long end;
        private int step;
        private Long last;
        private Runnable runnable;
        private TankKey tankKey = new TankKey();
        private static final Set<LoopTanker> secondtank = new HashSet<>();
        private static final Set<LoopTanker> hourtank = new HashSet<>();

        private static final Set<LoopTanker> addsecondtank = new HashSet<>();
        private static final Set<LoopTanker> addhourtank = new HashSet<>();
        private static final Set<LoopTanker> removeOrdertank = new HashSet<>();

        private LoopTanker(Long end, int step, Runnable runnable) {
            last = begin = System.currentTimeMillis();
            this.end = end;
            this.step = step;
            this.runnable = runnable;
        }

        private static void LoopTankStream(Set<LoopTanker> set) {
            long now = System.currentTimeMillis();
            Set<LoopTanker> removetank = new HashSet<>();
            set.stream().filter(a -> (a.end > 0 && a.end < now)).forEach(removetank::add);
            set.removeAll(removetank);

            set.stream().filter(a -> ((a.last + a.step) <= now)).forEach(a -> {
                try {
                    a.runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    removeOrdertank.add(a);
                }
                a.last = now;
            });

        }

        private static void removeOrder() {
            if (removeOrdertank.size() != 0) {
                secondtank.removeAll(removeOrdertank);
                hourtank.removeAll(removeOrdertank);
                removeOrdertank.clear();
            }
        }

        private static void addOrder() {
            if (addhourtank.size() != 0) {
                hourtank.addAll(addhourtank);
                addhourtank.clear();
            }
            if (addsecondtank.size() != 0) {
                secondtank.addAll(addsecondtank);
                addsecondtank.clear();
            }
        }

        private static LoopTanker getLoopTanker(TankKey tankKey) {
            LoopTanker lt = getTanker(tankKey, hourtank);
            if (lt != null) return lt;
            return getTanker(tankKey, secondtank);
        }

        private static LoopTanker getTanker(TankKey tankKey, Set<LoopTanker> set) {
            for (LoopTanker loopTanker : set)
                if (loopTanker.tankKey == tankKey)
                    return loopTanker;
            return null;
        }
    }

    public synchronized boolean removeLoopTank(TankKey tankKey) {
        if (tankKey == null) return false;
        for (LoopTanker loopTanker : LoopTanker.hourtank) {
            if (loopTanker.tankKey == tankKey) {
                LoopTanker.removeOrdertank.add(loopTanker);
                return true;
            }
        }
        for (LoopTanker loopTanker : LoopTanker.secondtank) {
            if (loopTanker.tankKey == tankKey) {
                LoopTanker.removeOrdertank.add(loopTanker);
                return true;
            }
        }
        return false;
    }

    public TankKey addLoopTankByTenofOneSecond(Runnable runnable, int TenofOneSecond, long end) {
        return addLoopTank(runnable, 100 * TenofOneSecond, end, LoopTanker.addsecondtank);
    }

    public TankKey addLoopTankBySec(Runnable runnable, int second, long end) {
        return addLoopTank(runnable, 1000 * second, end, LoopTanker.addsecondtank);
    }

    public TankKey addLoopTankByHour(Runnable runnable, int hour, long end) {
        return addLoopTank(runnable, 3600000 * hour, end, LoopTanker.addhourtank);
    }

    private synchronized TankKey addLoopTank(Runnable runnable, int loop, long end, Set<LoopTanker> set) {
        if (runnable == null) return null;
        if (loop <= 0)
            return null;
        if (end > 0 && end < System.currentTimeMillis())
            return null;
        LoopTanker tanker = new LoopTanker(end, loop, runnable);
        set.add(tanker);
        return tanker.tankKey;
    }

    public String shutdown() {
        flag = false;
        clear();
        executorService.shutdown();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long runTime() {
        return System.currentTimeMillis() - BEGIN;
    }

    public long runTime(TankKey tankKey) {
        if (tankKey == null) return -1;
        LoopTanker loopTanker = LoopTanker.getLoopTanker(tankKey);
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

    public static class TankKey {
    }
}
