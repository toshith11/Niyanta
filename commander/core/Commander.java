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
            if (result.getIntent() == Intent.EXIT) {

                System.out.println("Sūtrādhār: " + result);
                System.out.println("Sūtrādhār: Shutting down.");

                break;
            }

            /*
             * SYSTEM / QUESTION
             */
            if (result.getIntent() == Intent.SYSTEM
                    || result.getIntent() == Intent.QUESTION) {

                /*
                 * MEMORY STATUS
                 */
                        if ("GET_STATUS".equals(result.getAction())
        && "SNAPSHOT".equals(result.getEntity())) {

    SystemSnapshot snapshot =
            systemInterface.getSystemSnapshot();

    System.out.println(
            "Sūtrādhār:\n" + snapshot
    );

    continue;
}

                if ("GET_STATUS".equals(result.getAction())
                        && "MEMORY".equals(result.getEntity())) {

                    System.out.println(
                            "Sūtrādhār: "
                                    + systemInterface.getMemoryStatus()
                    );
                }

                /*
                 * CPU STATUS
                 */
                else if ("GET_STATUS".equals(result.getAction())
                        && "CPU".equals(result.getEntity())) {

                    System.out.println(
                            "Sūtrādhār: "
                                    + systemInterface.getCpuStatus()
                    );
                }

                /*
                 * STORAGE STATUS
                 */
                else if ("GET_STATUS".equals(result.getAction())
                        && "STORAGE".equals(result.getEntity())) {

                    System.out.println(
                            "Sūtrādhār: "
                                    + systemInterface.getStorageStatus()
                    );
                }

                /*
                 * PROCESS STATUS
                 */
                else if ("GET_STATUS".equals(result.getAction())
                        && "PROCESSES".equals(result.getEntity())) {

                    System.out.println(
                            "Sūtrādhār: "
                                    + systemInterface.getProcessStatus()
                    );
                }

                /*
                 * TIME QUESTION
                 */
                else if ("ANSWER".equals(result.getAction())
                        && result.getParameter() != null
                        && result.getParameter().contains("time")) {

                    System.out.println(
                            "Sūtrādhār: The current time is "
                                    + systemInterface.getCurrentTime()
                                    + "."
                    );
                }

                /*
                 * Generic system status
                 */
                else if ("GET_STATUS".equals(result.getAction())) {

                    System.out.println(
                            "Sūtrādhār: "
                                    + systemInterface.getSystemStatus()
                    );
                }

                /*
                 * Generic question
                 */
                else if ("ANSWER".equals(result.getAction())) {

                    System.out.println(
                            "Sūtrādhār: I understand that you are asking a question."
                    );
                }

                continue;
            }

            /*
             * MEMORY
             */
            if (result.getIntent() == Intent.MEMORY) {

                /*
                 * STORE MEMORY
                 */
                if ("STORE".equals(result.getAction())) {

                    memoryManager.remember(
                            result.getKnowledge()
                    );

                    System.out.println(
                            "Sūtrādhār: Memory stored."
                    );
                }

                /*
                 * RECALL MEMORY
                 */
                else if ("RECALL".equals(result.getAction())) {

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
                }

                continue;
            }

            /*
             * OTHER COMMANDS
             */
            ActionResult actionResult =
        actionEngine.execute(result);

System.out.println(
        "Sūtrādhār: " + actionResult.getMessage()
);
        }

        scanner.close();
    }
}