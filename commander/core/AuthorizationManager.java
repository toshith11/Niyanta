package commander.core;

public class AuthorizationManager {

    public RiskLevel getRiskLevel(Action action) {

        return switch (action) {

            case GET_TIME,
                 GET_MEMORY_STATUS,
                 GET_CPU_STATUS,
                 GET_STORAGE_STATUS,
                 GET_PROCESS_STATUS,
                 GET_SYSTEM_SNAPSHOT,
                 LIST_FILES,
                 RECALL_MEMORY,
                 RESPOND,
                 SEARCH ->
                    RiskLevel.READ_ONLY;

            case STORE_MEMORY,
                 OPEN_APPLICATION ->
                    RiskLevel.CONFIRMATION_REQUIRED;

            case EXIT ->
                    RiskLevel.READ_ONLY;

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