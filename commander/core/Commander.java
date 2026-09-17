package commander.core;
import commander.memory.MemoryManager;
import commander.system.SystemInterface;
import java.util.Scanner;


public class Commander {

    private final Understanding understanding;
    private final MemoryManager memoryManager;
    private final SystemInterface systemInterface;

    public Commander() {
        understanding = new Understanding();
        memoryManager = new MemoryManager();
        systemInterface = new SystemInterface();
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
                    if (result.getIntent() == Intent.SYSTEM ||
        result.getIntent() == Intent.QUESTION) {

    if ("GET_STATUS".equals(result.getAction())
        && "MEMORY".equals(result.getEntity())) {

    System.out.println(
            "Sūtrādhār: " +
            systemInterface.getMemoryStatus()
    );

    } else if ("ANSWER".equals(result.getAction())
            && result.getParameter().contains("time")) {

        System.out.println(
                "Sūtrādhār: The current time is "
                        + systemInterface.getCurrentTime()
                        + "."
        );

    } else if ("GET_STATUS".equals(result.getAction())) {

        System.out.println(
                "Sūtrādhār: "
                        + systemInterface.getSystemStatus()
        );
    }

    continue;
}

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
