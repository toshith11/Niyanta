package commander.core;

import commander.memory.MemoryManager;
import commander.system.SystemInterface;
import commander.system.SystemSnapshot;

import java.util.Scanner;

public class Commander {

    private final Understanding understanding;
    private final MemoryManager memoryManager;
    private final SystemInterface systemInterface;
    private final ActionEngine actionEngine;

    public Commander() {
        understanding = new Understanding();
        memoryManager = new MemoryManager();
        systemInterface = new SystemInterface();
        actionEngine = new ActionEngine();
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

            /*
             * EXIT
             */
            if (result.getAction() == Action.EXIT) {

                System.out.println(
                        "Sūtrādhār: " + result
                );

                System.out.println(
                        "Sūtrādhār: Shutting down."
                );

                break;
            }

            /*
             * MEMORY STORE
             */
            if (result.getAction() == Action.STORE_MEMORY) {

                memoryManager.remember(
                        result.getKnowledge()
                );

                System.out.println(
                        "Sūtrādhār: Memory stored."
                );

                continue;
            }

            /*
             * MEMORY RECALL
             */
            if (result.getAction() == Action.RECALL_MEMORY) {

                String value = memoryManager.recall(
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
             * GET TIME
             */
            if (result.getAction() == Action.GET_TIME) {

                System.out.println(
                        "Sūtrādhār: The current time is "
                                + systemInterface.getCurrentTime()
                                + "."
                );

                continue;
            }

            /*
             * MEMORY STATUS
             */
            if (result.getAction() == Action.GET_MEMORY_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getMemoryStatus()
                );

                continue;
            }

            /*
             * CPU STATUS
             */
            if (result.getAction() == Action.GET_CPU_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getCpuStatus()
                );

                continue;
            }

            /*
             * STORAGE STATUS
             */
            if (result.getAction() == Action.GET_STORAGE_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getStorageStatus()
                );

                continue;
            }

            /*
             * PROCESS STATUS
             */
            if (result.getAction() == Action.GET_PROCESS_STATUS) {

                System.out.println(
                        "Sūtrādhār: "
                                + systemInterface.getProcessStatus()
                );

                continue;
            }

            /*
             * COMPLETE SYSTEM SNAPSHOT
             */
            if (result.getAction() == Action.GET_SYSTEM_SNAPSHOT) {

                SystemSnapshot snapshot =
                        systemInterface.getSystemSnapshot();

                System.out.println(
                        "Sūtrādhār:\n" + snapshot
                );

                continue;
            }

            /*
             * OTHER ACTIONS
             *
             * FILES
             * APPLICATIONS
             * GREETINGS
             * RESEARCH
             * etc.
             */
            ActionResult actionResult =
                    actionEngine.execute(result);

            System.out.println(
                    "Sūtrādhār: "
                            + actionResult.getMessage()
            );
        }

        scanner.close();
    }
}