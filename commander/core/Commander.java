import java.util.Scanner;

public class Commander {

    private final Understanding understanding;

    public Commander() {
        understanding = new Understanding();
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Sūtrādhār is awake.");
        System.out.println("Niyanta Commander initialized.");
        System.out.println();

        while (true) {

            System.out.print("You: ");
            String input = scanner.nextLine();

            UnderstandingResult result =
                    understanding.understand(input);

            System.out.println("Sūtrādhār: " + result);

            if (result.getIntent() == Intent.EXIT) {
                System.out.println("Sūtrādhār: Shutting down.");
                break;
            }
        }

        scanner.close();
    }
}
