package commander.core;
import commander.background.BackgroundService;
import commander.memory.MemoryManager;
import commander.system.SystemInterface;
import commander.system.SystemSnapshot;

import java.util.Scanner;

public class Commander {

    private final Understanding understanding;
    private final MemoryManager memoryManager;
    private final SystemInterface systemInterface;
    private final ActionEngine actionEngine;
    private final AuthorizationManager authorizationManager;
    private final BackgroundService backgroundService;

    public Commander() {
        understanding = new Understanding();
        memoryManager = new MemoryManager();
        systemInterface = new SystemInterface();
        actionEngine = new ActionEngine();
        authorizationManager = new AuthorizationManager();
        backgroundService = new BackgroundService();
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Sūtrādhār is awake.");
        System.out.println("Niyanta Commander initialized.");
        System.out.println();
        backgroundService.start();

System.out.println(
        "Sūtrādhār background service started."
);

        while (true) {

            System.out.print("You: ");
            String input = scanner.nextLine();

if (input.isBlank()) {
    continue;
}

UserInput userInput =
        new UserInput(
                input,
                InputSource.TEXT
        );

CommandUnderstanding result =
        understanding.understand(userInput);

        double confidence = result.getConfidence();

if (confidence < 0.50) {

    System.out.println(
            "Sūtrādhār: I am not confident enough to understand that command."
    );

    continue;
}

if (confidence < 0.80) {

    System.out.println(
            getClarificationMessage(result)
    );

    continue;
}

            Action action = result.getAction();

            /*
             * 1. AUTHORIZATION
             */
            RiskLevel riskLevel =
                    authorizationManager.getRiskLevel(action);

            if (riskLevel == RiskLevel.NOT_ALLOWED) {

                System.out.println(
                        "Sūtrādhār: This action is not permitted."
                );

                continue;
            }

            if (authorizationManager.requiresConfirmation(action)) {

                System.out.println(
                        "Sūtrādhār: This action requires confirmation: "
                                + action
                );

                System.out.print("Allow? (yes/no): ");

                String confirmation =
                        scanner.nextLine();

                if (!confirmation.equalsIgnoreCase("yes")) {

                    System.out.println(
                            "Sūtrādhār: Action cancelled."
                    );

                    continue;
                }
            }

            /*
             * 2. EXIT
             */
            if (action == Action.EXIT) {

                System.out.println(
                        "Sūtrādhār: " + result
                );

                System.out.println(
                        "Sūtrādhār: Shutting down."
                );

                break;
            }

            /*
             * 3. MEMORY STORE
             */
            if (action == Action.STORE_MEMORY) {

                memoryManager.remember(
                        result.getKnowledge()
                );

                System.out.println(
                        "Sūtrādhār: Memory stored."
                );

                continue;
            }

            /*
             * 4. MEMORY RECALL
             */
            if (action == Action.RECALL_MEMORY) {

                String value =
                        memoryManager.recall(
                                result.getEntity(),
                                result.getParameter()
                        );

                if (value != null) {

                    System.out.println(
                            "Sūtrādhār: Your project is "
                                    + value
                                    + "."
                    );

                } else {

                    System.out.println(
                            "Sūtrādhār: I don't have that information."
                    );
                }

                continue;
            }

            /*
             * 5. SYSTEM ACTIONS
             */
            if (action == Action.GET_TIME) {

                System.out.println(
                        "Sūtrādhār: The current time is "
                                + systemInterface.getCurrentTime()
                                + "."
                );

                continue;
            }

            if (action == Action.GET_MEMORY_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getMemoryStatus()
                );

                continue;
            }

            if (action == Action.GET_CPU_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getCpuStatus()
                );

                continue;
            }

            if (action == Action.GET_STORAGE_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getStorageStatus()
                );

                continue;
            }

            if (action == Action.GET_PROCESS_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getProcessStatus()
                );

                continue;
            }

            if (action == Action.GET_SYSTEM_SNAPSHOT) {

                SystemSnapshot snapshot =
                        systemInterface.getSystemSnapshot();

                System.out.println(
                        "Sūtrādhār:\n" + snapshot
                );

                continue;
            }

            /*
             * 6. ACTION ENGINE
             */
            ActionResult actionResult =
                    actionEngine.execute(result);

            System.out.println(
                    "Sūtrādhār: "
                            + actionResult.getMessage()
            );
        }
        backgroundService.stop();
        scanner.close();
    }

    private String getClarificationMessage(
        CommandUnderstanding result) {

    if (result.getAction() == Action.OPEN_APPLICATION
            && "UNKNOWN_APPLICATION".equals(
                    result.getEntity())) {

        return "Sūtrādhār: Which application should I open?";
    }

    if (result.getIntent() == Intent.RESEARCH
            && (result.getParameter() == null
            || result.getParameter().isBlank())) {

        return "Sūtrādhār: What would you like me to research?";
    }

    if (result.getIntent() == Intent.MEMORY
            && result.getAction() == Action.STORE_MEMORY
            && result.getKnowledge() == null) {

        return "Sūtrādhār: What would you like me to remember?";
    }

    if (result.getIntent() == Intent.QUESTION) {

        return "Sūtrādhār: Could you rephrase the question?";
    }

    return "Sūtrādhār: Could you clarify what you want me to do?";
}
}