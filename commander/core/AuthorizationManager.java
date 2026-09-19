package commander.core;

public class AuthorizationManager {

    public RiskLevel getRiskLevel(Action action) {

        return switch (action) {

            /*
             * READ-ONLY ACTIONS
             *
             * These actions only read information,
             * answer the user, or continue an existing
             * reasoning/research task.
             */
            case GET_TIME,
                 GET_MEMORY_STATUS,
                 GET_CPU_STATUS,
                 GET_STORAGE_STATUS,
                 GET_PROCESS_STATUS,
                 GET_SYSTEM_SNAPSHOT,
                 LIST_FILES,
                 RECALL_MEMORY,
                 RESPOND,
                 SEARCH,
                 CONTINUE_TASK ->
                    RiskLevel.READ_ONLY;

            /*
             * ACTIONS REQUIRING USER CONFIRMATION
             */
            case STORE_MEMORY,
                 OPEN_APPLICATION ->
                    RiskLevel.CONFIRMATION_REQUIRED;

            /*
             * NORMAL TERMINATION
             */
            case EXIT ->
                    RiskLevel.READ_ONLY;

            /*
             * Unknown / unsupported action
             */
            case NONE ->
                    RiskLevel.NOT_ALLOWED;
        };
    }

    public boolean requiresConfirmation(Action action) {

        return getRiskLevel(action)
                == RiskLevel.CONFIRMATION_REQUIRED;
    }

    public boolean isAllowed(Action action) {

        return getRiskLevel(action)
                != RiskLevel.NOT_ALLOWED;
    }
}