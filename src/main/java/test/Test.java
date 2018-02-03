package test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Test {
    private static ThreadLocal<SimpleDateFormat> local = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd-MMM-yyyy", Locale.US));


    static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    static String[] date = {"01-Jan-1999", "01-Jan-2000", "01-Jan-2001"};

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        for (int i = 0; i < date.length; i++) {
            int temp = i;
            new Thread(() -> {
                try {
                    while (true) {
                        String str1 = date[temp];
                        String str2 = local.get().format(local.get().parse(str1));
                        System.out.println(Thread.currentThread().getName() + "," + str1 + "," + str2);
                        if (!str1.equals(str2)){
                            System.out.println(Thread.currentThread().getName() + "expected" + str1 + " : " + str2);
                            throw new RuntimeException(Thread.currentThread().getName() + "expected" + str1 + " : " + str2);}
                        Thread.sleep(0);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("shibaile");
                }

            }).start();
        }




//        long l = System.currentTimeMillis();
//        for (int i = 0; i < 10000000; i++) {
//
//            System.out.println(yyyyMMddHHmmss.format(new Date()));
//        }
//        System.out.println(System.currentTimeMillis() - l);
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Future<?> submit = executorService.submit(() -> {
//            Thread.sleep(1000);
//            System.out.println(1);
//            return 2;
//        });
//        System.out.println(0);
//        System.out.println(submit.get());
//        System.out.println(1);
//        System.out.println(submit.get());
//        System.out.println(new File("D:\\").getPath());
//        System.out.println(1);
//        ServerSocketInMessageQueue.start();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        class Flag{
//            boolean flag=true;
//        }
//        final Flag flag= new Flag();
//        new Thread(() -> {
//            while (flag.flag) System.out.println(ServerSocketInMessageQueue.nextMessage());
//        }).start();
//        for (int i = 0; i < 100; i++)
//            try {
//                Socket socket = new Socket("10.39.14.191", 4043);
//                try (OutputStream outputStream = socket.getOutputStream()
//                ) {
//                    System.out.println(i + "a");
//                    outputStream.write((i + "").getBytes());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        System.out.println(3);
//        ServerSocketInMessageQueue.shutdown();
//        flag.flag=false;
    }
    public static void TEST_SimpleDateFormat(){
        for (int i = 0; i < date.length; i++) {
            int temp = i;
            new Thread(() -> {
                try {
                    while (true) {
                        String str1 = date[temp];
                        String str2 = yyyyMMddHHmmss.format(yyyyMMddHHmmss.parse(str1));
//                        System.out.println(Thread.currentThread().getName() + "," + str1 + "," + str2);
                        if (!str1.equals(str2)){
                            System.out.println(Thread.currentThread().getName() + "expected" + str1 + " : " + str2);
                            throw new RuntimeException(Thread.currentThread().getName() + "expected" + str1 + " : " + str2);}
                        Thread.sleep(0);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("shibaile");
                }

            }).start();
        }
    }

}
