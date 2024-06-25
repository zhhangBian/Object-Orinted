public class Process2 extends Thread {
    private final Somethings somethings;

    public Process2(Somethings somethings) {
        this.somethings = somethings;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (somethings) {
                System.out.println("2 get");

                if (somethings.getNumber() > 5) {
                    System.out.println("2 return");
                    return;
                }

                System.out.println("2 add");
                if (somethings.getNumber() % 2 == 1) {
                    somethings.setNumber(somethings.getNumber() + 1);
                }

                System.out.println("2 notice");
                somethings.notifyAll();
            }

            try {
                System.out.println("2 sleep");
                sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (somethings) {
                System.out.println("2 get");

                try {
                    System.out.println("2 wait");
                    somethings.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}