public class Process1 extends Thread {
    private final Somethings somethings;

    public Process1(Somethings somethings) {
        this.somethings = somethings;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (somethings) {
                System.out.println("1 get");

                if (somethings.getNumber() > 5) {
                    System.out.println("1 return");
                    return;
                }

                try {
                    System.out.println("1 wait");
                    somethings.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("1 add");
                if (somethings.getNumber() % 2 == 0) {
                    somethings.setNumber(somethings.getNumber() + 1);
                }

                System.out.println("1 notice");
                somethings.notifyAll();
            }

            try {
                System.out.println("1 sleep");
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}