import java.util.stream.IntStream;

public class Main {
    private static final int TIME_SLEEP = 1000;
    private static final int COUNT_ITERATION = 3;
    volatile static boolean tumbler;

    public static void main(String[] args) {
        //Создаем потоки
        Thread boxThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                while (tumbler) {
                    tumbler = false;
                    System.out.println("Выключил тубмлер");
                }
            }
        }, "Коробка");

        Thread userThread = new Thread(() -> {
            IntStream.range(0, COUNT_ITERATION).forEach(i -> {
                try {
                    System.out.println("Включил тумблер ");
                    tumbler = true;
                    Thread.sleep(TIME_SLEEP);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }, "Пользователель");
        //Запускаем потоки
        boxThread.start();
        userThread.start();

        try {
            //Останавливаем главный поток, пока не выполнится поток Пользователель
            userThread.join();
            //Посылаем сигнал остановки потоку Коробка
            boxThread.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
