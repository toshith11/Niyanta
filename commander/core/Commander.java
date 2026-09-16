package commander.core;
import commander.memory.MemoryManager;
import java.util.Scanner;

import commander.memory.MemoryManager;

public class Commander {

    private final Understanding understanding;
    private final MemoryManager memoryManager;

    public Commander() {
        understanding = new Understanding();
        memoryManager = new MemoryManager();
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Sūtrādhār is awake.");
        System.out.println("Niyanta Commander initialized.");
        System.out.println();

        while (true) {

            System.out.print("You: ");
            String input = scanner.nextLine();

            CommandUnderstanding result =
                    understanding.understand(input);

            if (result.getIntent() == Intent.MEMORY) {

    if ("STORE".equals(result.getAction())) {

        memoryManager.remember(result.getKnowledge());

        System.out.println(
                "Sūtrādhār: Memory stored."
        );
    }

    else if ("RECALL".equals(result.getAction())) {

        String value = memoryManager.recall(
                result.getEntity(),
                result.getParameter()
        );

        if (value != null) {
            System.out.println(
                    "Sūtrādhār: Your project is " + value + "."
            );
        } else {
            System.out.println(
                    "Sūtrādhār: I don't have that information."
            );
        }
    }

    continue;
}

            System.out.println("Sūtrādhār: " + result);

            if (result.getIntent() == Intent.EXIT) {
                System.out.println("Sūtrādhār: Shutting down.");
                break;
            }
        }

        scanner.close();
    }
}
