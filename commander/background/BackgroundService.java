package commander.background;

public class BackgroundService implements Runnable {

    private volatile boolean running;
    private Thread worker;

    public synchronized void start() {

        if (running) {
            return;
        }

        running = true;

        worker = new Thread(
                this,
                "sutradhar-background"
        );

        worker.setDaemon(true);
        worker.start();
    }

    public synchronized void stop() {

        running = false;

        if (worker != null) {
            worker.interrupt();
        }
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {

        while (running) {

            try {

                /*
                 * Background cognition will be added later.
                 * For now the worker simply remains alive.
                 */

                Thread.sleep(5000);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}