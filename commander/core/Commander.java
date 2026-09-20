package commander.core;

import commander.background.BackgroundService;
import commander.memory.MemoryManager;
import commander.memory.WorkingMemory;
import commander.memory.EpisodicMemory;
import commander.memory.MemoryRetriever;
import commander.memory.MemorySearchResult;
import commander.system.SystemInterface;
import commander.system.SystemSnapshot;

import java.util.List;
import java.util.Scanner;

public class Commander {

    private final Understanding understanding;
    private final MemoryManager memoryManager;
    private final SystemInterface systemInterface;
    private final ActionEngine actionEngine;
    private final AuthorizationManager authorizationManager;
    private final BackgroundService backgroundService;
    private final ContextManager contextManager;
    private final ContextResolver contextResolver;
    private final WorkingMemory workingMemory;
    private final EpisodicMemory episodicMemory;
    private final MemoryRetriever memoryRetriever;

    public Commander() {

        understanding = new Understanding();

        memoryManager = new MemoryManager();

        systemInterface = new SystemInterface();

        actionEngine = new ActionEngine();

        authorizationManager =
                new AuthorizationManager();

        backgroundService =
                new BackgroundService();

        contextManager =
                new ContextManager();

        contextResolver =
                new ContextResolver();

        workingMemory =
                new WorkingMemory();

        episodicMemory =
                new EpisodicMemory();

        memoryRetriever =
                new MemoryRetriever(
                        memoryManager,
                        episodicMemory
                );
    }

    public void start() {

        Scanner scanner =
                new Scanner(System.in);

        System.out.println(
                "Sūtrādhār is awake."
        );

        System.out.println(
                "Niyanta Commander initialized."
        );

        System.out.println();

        backgroundService.start();

        System.out.println(
                "Sūtrādhār background service started."
        );

        while (true) {

            System.out.print("You: ");

            String input =
                    scanner.nextLine();

            /*
             * Ignore empty input.
             */
            if (input.isBlank()) {
                continue;
            }

            /*
             * Create user input abstraction.
             */
            UserInput userInput =
                    new UserInput(
                            input,
                            InputSource.TEXT
                    );

            /*
             * 1. UNDERSTAND
             */
            CommandUnderstanding result =
                    understanding.understand(
                            userInput
                    );

            /*
             * 2. RESOLVE USING PREVIOUS CONTEXT
             */
            result =
                    contextResolver.resolve(
                            result,
                            contextManager.getContext(),
                            input
                    );

            /*
             * 3. STORE KNOWLEDGE IN WORKING MEMORY
             */
            if (result.getKnowledge() != null) {

                workingMemory.add(
                        result.getKnowledge()
                );
            }

            /*
             * 4. UPDATE CONVERSATIONAL CONTEXT
             */
            contextManager.update(
                    input,
                    result
            );

            /*
             * 5. CONFIDENCE CHECK
             */
            double confidence =
                    result.getConfidence();

            if (confidence < 0.50) {

                System.out.println(
                        "Sūtrādhār: "
                                + "I am not confident enough "
                                + "to understand that command."
                );

                continue;
            }

            if (confidence < 0.80) {

                /*
                 * Currently research is the only
                 * clarification state implemented.
                 */
                if (result.getIntent()
                        == Intent.RESEARCH) {

                    contextManager.setAwaitingInput(
                            "RESEARCH_TOPIC"
                    );
                }

                System.out.println(
                        getClarificationMessage(
                                result
                        )
                );

                continue;
            }

            /*
             * 6. SHOW RECENT EPISODES
             *
             * Diagnostic command.
             */
            if ("EPISODES".equals(
                    result.getEntity())) {

                System.out.println(
                        "Sūtrādhār: Recent episodes:"
                );

                List<String> episodes =
                        episodicMemory
                                .getRecentEpisodes(5);

                if (episodes.isEmpty()) {

                    System.out.println(
                            "- No episodes recorded."
                    );

                } else {

                    for (String episode :
                            episodes) {

                        System.out.println(
                                "- " + episode
                        );
                    }
                }

                continue;
            }

            /*
             * 7. SHOW CURRENT CONTEXT
             *
             * Diagnostic command.
             */
            if ("CONTEXT".equals(
                    result.getEntity())) {

                ConversationContext context =
                        contextManager.getContext();

                System.out.println(
                        "Sūtrādhār: Current context:"
                );

                System.out.println(
                        "Last input: "
                                + context.getLastInput()
                );

                System.out.println(
                        "Last intent: "
                                + context.getLastIntent()
                );

                System.out.println(
                        "Last action: "
                                + context.getLastAction()
                );

                System.out.println(
                        "Active task: "
                                + context.getActiveTask()
                );

                System.out.println(
                        "Active topic: "
                                + context.getActiveTopic()
                );

                System.out.println(
                        "Recent history:"
                );

                for (ContextEntry entry :
                        context.getRecentHistory()) {

                    System.out.println(
                            "- "
                                    + entry.getInput()
                                    + " → "
                                    + entry.getIntent()
                                    + " / "
                                    + entry.getAction()
                    );
                }

                continue;
            }

            /*
             * 8. AUTHORIZATION
             */
            Action action =
                    result.getAction();

            RiskLevel riskLevel =
                    authorizationManager
                            .getRiskLevel(action);

            if (riskLevel
                    == RiskLevel.NOT_ALLOWED) {

                System.out.println(
                        "Sūtrādhār: "
                                + "This action is not permitted."
                );

                continue;
            }

            /*
             * 9. CONFIRMATION
             *
             * Temporary text-based prototype.
             * Later this will be replaced by
             * voice authentication / confirmation.
             */
            if (authorizationManager
                    .requiresConfirmation(action)) {

                System.out.println(
                        "Sūtrādhār: "
                                + "This action requires confirmation: "
                                + action
                );

                System.out.print(
                        "Allow? (yes/no): "
                );

                String confirmation =
                        scanner.nextLine();

                if (!confirmation
                        .equalsIgnoreCase("yes")) {

                    System.out.println(
                            "Sūtrādhār: "
                                    + "Action cancelled."
                    );

                    recordEpisode(
                            input,
                            result,
                            "Action cancelled."
                    );

                    continue;
                }
            }

            /*
             * 10. EXIT
             */
            if (action == Action.EXIT) {

                System.out.println(
                        "Sūtrādhār: "
                                + result
                );

                System.out.println(
                        "Sūtrādhār: "
                                + "Shutting down."
                );

                break;
            }

            /*
             * 11. STORE LONG-TERM MEMORY
             */
            if (action == Action.STORE_MEMORY) {

                memoryManager.rememberLongTerm(
                        result.getKnowledge()
                );

                String response =
                        "Memory stored.";

                System.out.println(
                        "Sūtrādhār: "
                                + response
                );

                recordEpisode(
                        input,
                        result,
                        response
                );

                continue;
            }

            /*
             * 12. RECALL MEMORY
             */
            if (action == Action.RECALL_MEMORY) {

                /*
                 * Research history
                 */
                if ("RESEARCH".equals(
                        result.getEntity())) {

                    List<MemorySearchResult> memories =
                            memoryRetriever.retrieve(
                                    "research",
                                    5
                            );

                    if (memories.isEmpty()) {

                        System.out.println(
                                "Sūtrādhār: "
                                        + "I don't have any research history."
                        );

                    } else {

                        System.out.println(
                                "Sūtrādhār: Recent research:"
                        );

                        for (MemorySearchResult memory :
                                memories) {

                            System.out.println(
                                    "- "
                                            + memory.getContent()
                            );
                        }
                    }

                    continue;
                }

                /*
                 * Normal structured memory recall
                 */
                if (result.getEntity() != null
                        && result.getParameter() != null) {

                    String value =
                            memoryManager.recall(
                                    result.getEntity(),
                                    result.getParameter()
                            );

                    if (value != null) {

                        String response =
                                "Your project is "
                                        + value
                                        + ".";

                        System.out.println(
                                "Sūtrādhār: "
                                        + response
                        );

                        recordEpisode(
                                input,
                                result,
                                response
                        );

                    } else {

                        String response =
                                "I don't have that information.";

                        System.out.println(
                                "Sūtrādhār: "
                                        + response
                        );

                        recordEpisode(
                                input,
                                result,
                                response
                        );
                    }
                }

                continue;
            }

            /*
             * 13. GET TIME
             */
            if (action == Action.GET_TIME) {

                String response =
                        "The current time is "
                                + systemInterface
                                .getCurrentTime()
                                + ".";

                System.out.println(
                        "Sūtrādhār: "
                                + response
                );

                recordEpisode(
                        input,
                        result,
                        response
                );

                continue;
            }

            /*
             * 14. GET MEMORY STATUS
             */
            if (action
                    == Action.GET_MEMORY_STATUS) {

                String response =
                        systemInterface
                                .getMemoryStatus();

                System.out.println(
                        "Sūtrādhār: "
                                + response
                );

                recordEpisode(
                        input,
                        result,
                        response
                );

                continue;
            }

            /*
             * 15. GET CPU STATUS
             */
            if (action
                    == Action.GET_CPU_STATUS) {

                String response =
                        systemInterface
                                .getCpuStatus();

                System.out.println(
                        "Sūtrādhār: "
                                + response
                );

                recordEpisode(
                        input,
                        result,
                        response
                );

                continue;
            }

            /*
             * 16. GET STORAGE STATUS
             */
            if (action
                    == Action.GET_STORAGE_STATUS) {

                String response =
                        systemInterface
                                .getStorageStatus();

                System.out.println(
                        "Sūtrādhār: "
                                + response
                );

                recordEpisode(
                        input,
                        result,
                        response
                );

                continue;
            }

            /*
             * 17. GET PROCESS STATUS
             */
            if (action
                    == Action.GET_PROCESS_STATUS) {

                String response =
                        systemInterface
                                .getProcessStatus();

                System.out.println(
                        "Sūtrādhār: "
                                + response
                );

                recordEpisode(
                        input,
                        result,
                        response
                );

                continue;
            }

            /*
             * 18. GET SYSTEM SNAPSHOT
             */
            if (action
                    == Action.GET_SYSTEM_SNAPSHOT) {

                SystemSnapshot snapshot =
                        systemInterface
                                .getSystemSnapshot();

                String response =
                        snapshot.toString();

                System.out.println(
                        "Sūtrādhār:\n"
                                + response
                );

                recordEpisode(
                        input,
                        result,
                        response
                );

                continue;
            }

            /*
             * 19. ACTION ENGINE
             *
             * Remaining executable actions
             * are delegated here.
             */
            ActionResult actionResult =
                    actionEngine.execute(
                            result
                    );

            String response =
                    actionResult.getMessage();

            System.out.println(
                    "Sūtrādhār: "
                            + response
            );

            recordEpisode(
                    input,
                    result,
                    response
            );
        }

        /*
         * 20. STOP BACKGROUND SERVICE
         */
        backgroundService.stop();

        scanner.close();
    }

    /*
     * Record an interaction as an episode.
     */
    private void recordEpisode(
            String input,
            CommandUnderstanding result,
            String response) {

        episodicMemory.record(
                input,
                result.getIntent().toString(),
                result.getAction().toString(),
                response
        );
    }

    /*
     * Generate clarification messages.
     */
    private String getClarificationMessage(
            CommandUnderstanding result) {

        if (result.getAction()
                == Action.OPEN_APPLICATION
                && "UNKNOWN_APPLICATION".equals(
                        result.getEntity())) {

            return "Sūtrādhār: "
                    + "Which application should I open?";
        }

        if (result.getIntent()
                == Intent.RESEARCH
                && (result.getParameter() == null
                || result.getParameter().isBlank())) {

            return "Sūtrādhār: "
                    + "What would you like me to research?";
        }

        if (result.getIntent()
                == Intent.MEMORY
                && result.getAction()
                == Action.STORE_MEMORY
                && result.getKnowledge() == null) {

            return "Sūtrādhār: "
                    + "What would you like me to remember?";
        }

        if (result.getIntent()
                == Intent.QUESTION) {

            return "Sūtrādhār: "
                    + "Could you rephrase the question?";
        }

        return "Sūtrādhār: "
                + "Could you clarify what you want me to do?";
    }
}