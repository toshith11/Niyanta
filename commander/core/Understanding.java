package commander.core;

public class Understanding {

    private final IntentClassifier intentClassifier;

    public Understanding() {
        intentClassifier = new IntentClassifier();
    }

    public CommandUnderstanding understand(UserInput input) {

        return understand(input.getText());
    }

    public CommandUnderstanding understand(String input) {

        String text = normalize(input);

        /*
         * -----------------------------------------
         * 1. EXIT
         * -----------------------------------------
         */
        if (isExit(text)) {

            return new CommandUnderstanding(
                    Intent.EXIT,
                    Action.EXIT,
                    null,
                    null,
                    1.0,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 2. GREETING
         * -----------------------------------------
         */
        if (isGreeting(text)) {

            return new CommandUnderstanding(
                    Intent.GREETING,
                    Action.RESPOND,
                    null,
                    null,
                    0.95,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 3. EPISODE HISTORY QUERY
         *
         * Must come before generic system/research
         * matching.
         * -----------------------------------------
         */
        if (isEpisodeQuery(text)) {

            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    Action.NONE,
                    "EPISODES",
                    null,
                    0.95,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 4. MEMORY QUESTIONS
         *
         * Specific memory questions must come before
         * generic research/question detection.
         * -----------------------------------------
         */
        if (isMemoryQuestion(text)) {

            /*
             * Research history
             */
            if (isResearchHistoryQuestion(text)) {

                return new CommandUnderstanding(
                        Intent.MEMORY,
                        Action.RECALL_MEMORY,
                        "RESEARCH",
                        "RESEARCH_HISTORY",
                        0.95,
                        null
                );
            }

            /*
             * Project memory
             */
            return new CommandUnderstanding(
                    Intent.MEMORY,
                    Action.RECALL_MEMORY,
                    "PROJECT",
                    "NAME",
                    0.95,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 5. CONTEXT FOLLOW-UP
         * -----------------------------------------
         */
        if (isContextFollowUp(text)) {

            return new CommandUnderstanding(
                    Intent.RESEARCH,
                    Action.CONTINUE_TASK,
                    "TOPIC",
                    text,
                    0.85,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 6. RESEARCH
         * -----------------------------------------
         */
        if (isResearch(text)) {

            String topic =
                    extractResearchTopic(text);

            double researchConfidence = 0.90;

            if (topic == null || topic.isBlank()) {
                researchConfidence = 0.60;
            }

            return new CommandUnderstanding(
                    Intent.RESEARCH,
                    Action.SEARCH,
                    "TOPIC",
                    topic,
                    researchConfidence,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 7. MEMORY STORE
         * -----------------------------------------
         */
        if (isMemory(text)) {

            String memory =
                    extractMemory(text);

            Knowledge knowledge =
                    extractMemoryKnowledge(memory);

            return new CommandUnderstanding(
                    Intent.MEMORY,
                    Action.STORE_MEMORY,
                    "MEMORY",
                    memory,
                    0.90,
                    knowledge
            );
        }

        /*
         * -----------------------------------------
         * 8. FILE
         * -----------------------------------------
         */
        if (isFile(text)) {

            return new CommandUnderstanding(
                    Intent.FILE,
                    Action.LIST_FILES,
                    "FILES",
                    null,
                    0.85,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 9. APPLICATION
         * -----------------------------------------
         */
        if (isApplication(text)) {

            String application =
                    extractApplication(text);

            double applicationConfidence = 0.90;

            if ("UNKNOWN_APPLICATION"
                    .equals(application)) {

                applicationConfidence = 0.60;
            }

            return new CommandUnderstanding(
                    Intent.APPLICATION,
                    Action.OPEN_APPLICATION,
                    application,
                    null,
                    applicationConfidence,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 10. SYSTEM
         * -----------------------------------------
         */
        if (isSystem(text)) {

            String target =
                    extractSystemTarget(text);

            /*
             * Context inspection
             */
            if ("CONTEXT".equals(target)) {

                return new CommandUnderstanding(
                        Intent.SYSTEM,
                        Action.NONE,
                        "CONTEXT",
                        null,
                        0.90,
                        null
                );
            }

            /*
             * Memory status
             */
            if ("MEMORY".equals(target)) {

                return new CommandUnderstanding(
                        Intent.SYSTEM,
                        Action.GET_MEMORY_STATUS,
                        "MEMORY",
                        null,
                        0.90,
                        null
                );
            }

            /*
             * CPU status
             */
            if ("CPU".equals(target)) {

                return new CommandUnderstanding(
                        Intent.SYSTEM,
                        Action.GET_CPU_STATUS,
                        "CPU",
                        null,
                        0.90,
                        null
                );
            }

            /*
             * Storage status
             */
            if ("STORAGE".equals(target)) {

                return new CommandUnderstanding(
                        Intent.SYSTEM,
                        Action.GET_STORAGE_STATUS,
                        "STORAGE",
                        null,
                        0.90,
                        null
                );
            }

            /*
             * Process status
             */
            if ("PROCESSES".equals(target)) {

                return new CommandUnderstanding(
                        Intent.SYSTEM,
                        Action.GET_PROCESS_STATUS,
                        "PROCESSES",
                        null,
                        0.90,
                        null
                );
            }

            /*
             * Full system snapshot
             */
            if ("SNAPSHOT".equals(target)) {

                return new CommandUnderstanding(
                        Intent.SYSTEM,
                        Action.GET_SYSTEM_SNAPSHOT,
                        "SNAPSHOT",
                        null,
                        0.90,
                        null
                );
            }

            /*
             * Episode history
             */
            if ("EPISODES".equals(target)) {

                return new CommandUnderstanding(
                        Intent.SYSTEM,
                        Action.NONE,
                        "EPISODES",
                        null,
                        0.90,
                        null
                );
            }

            /*
             * Generic system command
             */
            return new CommandUnderstanding(
                    Intent.SYSTEM,
                    Action.NONE,
                    "SYSTEM",
                    null,
                    0.70,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 11. GENERAL QUESTIONS
         * -----------------------------------------
         */
        if (isQuestion(text)) {

            /*
             * Time question
             */
            if (text.contains("time")) {

                return new CommandUnderstanding(
                        Intent.QUESTION,
                        Action.GET_TIME,
                        "TIME",
                        text,
                        0.90,
                        null
                );
            }

            /*
             * Generic question
             */
            return new CommandUnderstanding(
                    Intent.QUESTION,
                    Action.NONE,
                    null,
                    text,
                    0.75,
                    null
            );
        }

        /*
         * -----------------------------------------
         * 12. UNKNOWN
         * -----------------------------------------
         */
        return new CommandUnderstanding(
                Intent.UNKNOWN,
                Action.NONE,
                null,
                null,
                0.20,
                null
        );
    }


    /*
     * =============================================
     * NORMALIZATION
     * =============================================
     */

    private String normalize(String input) {

        if (input == null) {
            return "";
        }

        return input
                .toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }


    /*
     * =============================================
     * EXIT
     * =============================================
     */

    private boolean isExit(String text) {

        return text.equals("exit")
                || text.equals("quit")
                || text.equals("shutdown")
                || text.equals("shut down")
                || text.contains("close commander");
    }


    /*
     * =============================================
     * GREETING
     * =============================================
     */

    private boolean isGreeting(String text) {

        return text.equals("hello")
                || text.equals("hi")
                || text.equals("hey")
                || text.contains("good morning")
                || text.contains("good afternoon")
                || text.contains("good evening");
    }


    /*
     * =============================================
     * EPISODE QUERY
     * =============================================
     */

    private boolean isEpisodeQuery(String text) {

        return text.contains("show recent episodes")
                || text.contains("recent episodes")
                || text.contains("show history");
    }


    /*
     * =============================================
     * MEMORY QUESTIONS
     * =============================================
     */

    private boolean isMemoryQuestion(String text) {

        return text.contains("what is my project")
                || text.contains("what's my project")
                || text.contains("what was my project")
                || text.contains("tell me my project")
                || text.contains("what project am i working on")
                || isResearchHistoryQuestion(text);
    }


    private boolean isResearchHistoryQuestion(
            String text) {

        return text.contains("what was i researching")
                || text.contains("what did i research")
                || text.contains("what have i researched")
                || text.contains("show my research history")
                || text.contains("what was my research");
    }


    /*
     * =============================================
     * CONTEXT FOLLOW-UP
     * =============================================
     */

    private boolean isContextFollowUp(String text) {

        return text.startsWith("focus on ")
                || text.startsWith("continue the research")
                || text.startsWith("continue research")
                || text.startsWith("continue it")
                || text.startsWith("go deeper into ")
                || text.startsWith("tell me more about ")
                || text.equals("continue");
    }


    /*
     * =============================================
     * RESEARCH
     * =============================================
     */

    private boolean isResearch(String text) {

        /*
         * Prevent specific memory/context commands
         * from being mistaken for new research.
         */
        if (isMemoryQuestion(text)
                || isEpisodeQuery(text)
                || isContextFollowUp(text)) {

            return false;
        }

        return text.contains("research")
                || text.contains("investigate")
                || text.contains("study")
                || text.contains("learn about")
                || text.contains("find information about")
                || text.contains("look into")
                || text.contains("explore the topic")
                || text.contains("find out about");
    }


    /*
     * =============================================
     * MEMORY STORE
     * =============================================
     */

    private boolean isMemory(String text) {

        return text.contains("remember")
                || text.contains("memorize")
                || text.contains("save this")
                || text.contains("store this");
    }


    /*
     * =============================================
     * FILE
     * =============================================
     */

    private boolean isFile(String text) {

        return text.contains("file")
                || text.contains("files")
                || text.contains("folder")
                || text.contains("folders")
                || text.contains("directory")
                || text.contains("directories")
                || text.contains("document")
                || text.contains("documents");
    }


    /*
     * =============================================
     * APPLICATION
     * =============================================
     */

    private boolean isApplication(String text) {

        return text.contains("open")
                || text.contains("launch")
                || text.contains("start")
                || text.contains("run");
    }


    /*
     * =============================================
     * SYSTEM
     * =============================================
     */

    private boolean isSystem(String text) {

        return text.contains("battery")
                || text.contains("memory")
                || text.contains("ram")
                || text.contains("storage")
                || text.contains("disk")
                || text.contains("cpu")
                || text.contains("processor")
                || text.contains("process")
                || text.contains("processes")
                || text.contains("system status")
                || text.contains("system snapshot")
                || text.contains("overall system")
                || text.contains("how is my system")
                || text.contains("show context")
                || text.contains("what are we working on")
                || text.contains("show recent episodes")
                || text.contains("recent episodes")
                || text.contains("show history");
    }


    /*
     * =============================================
     * QUESTIONS
     * =============================================
     */

    private boolean isQuestion(String text) {

        return text.startsWith("what ")
                || text.startsWith("how ")
                || text.startsWith("why ")
                || text.startsWith("when ")
                || text.startsWith("where ")
                || text.startsWith("who ")
                || text.startsWith("can you ")
                || text.startsWith("could you ")
                || text.startsWith("would you ");
    }


    /*
     * =============================================
     * RESEARCH TOPIC EXTRACTION
     * =============================================
     */

    private String extractResearchTopic(
            String text) {

        String[] markers = {
                "research ",
                "investigate ",
                "study ",
                "learn about ",
                "find information about ",
                "look into ",
                "explore the topic ",
                "find out about "
        };

        for (String marker : markers) {

            int index =
                    text.indexOf(marker);

            if (index != -1) {

                return text.substring(
                        index + marker.length()
                ).trim();
            }
        }

        return null;
    }


    /*
     * =============================================
     * MEMORY EXTRACTION
     * =============================================
     */

    private String extractMemory(String text) {

        String[] markers = {
                "remember that ",
                "remember ",
                "memorize ",
                "save this ",
                "store this "
        };

        for (String marker : markers) {

            int index =
                    text.indexOf(marker);

            if (index != -1) {

                return text.substring(
                        index + marker.length()
                ).trim();
            }
        }

        return null;
    }


    /*
     * =============================================
     * MEMORY KNOWLEDGE EXTRACTION
     * =============================================
     */

    private Knowledge extractMemoryKnowledge(
            String memory) {

        if (memory == null) {
            return null;
        }

        /*
         * Example:
         *
         * "my project is niyanta"
         *
         * becomes:
         *
         * PROJECT --NAME--> NIYANTA
         */
        if (memory.contains("my project is")) {

            String value =
                    memory.substring(
                            memory.indexOf(
                                    "my project is"
                            )
                            + "my project is".length()
                    ).trim();

            if (!value.isBlank()) {

                return new Knowledge(
                        "PROJECT",
                        "NAME",
                        value
                );
            }
        }

        return null;
    }


    /*
     * =============================================
     * SYSTEM TARGET EXTRACTION
     * =============================================
     */

    private String extractSystemTarget(
            String text) {

        /*
         * Context must be checked before
         * generic system keywords.
         */
        if (text.contains("show context")
                || text.contains("what are we working on")) {

            return "CONTEXT";
        }

        /*
         * Snapshot
         */
        if (text.contains("system status")
                || text.contains("system snapshot")
                || text.contains("overall system")
                || text.contains("how is my system")) {

            return "SNAPSHOT";
        }

        /*
         * Episode history
         */
        if (text.contains("show recent episodes")
                || text.contains("recent episodes")
                || text.contains("show history")) {

            return "EPISODES";
        }

        /*
         * Memory
         */
        if (text.contains("memory")
                || text.contains("ram")) {

            return "MEMORY";
        }

        /*
         * Battery
         */
        if (text.contains("battery")) {

            return "BATTERY";
        }

        /*
         * Storage
         */
        if (text.contains("storage")
                || text.contains("disk")) {

            return "STORAGE";
        }

        /*
         * CPU
         */
        if (text.contains("cpu")
                || text.contains("processor")) {

            return "CPU";
        }

        /*
         * Processes
         */
        if (text.contains("process")) {

            return "PROCESSES";
        }

        return "SYSTEM";
    }


    /*
     * =============================================
     * APPLICATION EXTRACTION
     * =============================================
     */

    private String extractApplication(
            String text) {

        String[] applications = {
                "chrome",
                "browser",
                "camera",
                "youtube",
                "settings",
                "vscode",
                "vs code"
        };

        for (String application :
                applications) {

            if (text.contains(application)) {

                return application
                        .toUpperCase()
                        .replace(" ", "_");
            }
        }

        return "UNKNOWN_APPLICATION";
    }
}