package commander.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class ActionEngine {

    public ActionResult execute(CommandUnderstanding command) {

        if (command == null) {

            return new ActionResult(
                    false,
                    "No command received."
            );
        }

        return switch (command.getAction()) {

            case LIST_FILES ->
                    listFiles();

            case OPEN_APPLICATION ->
                    openApplication(
                            command.getEntity()
                    );

            case RESPOND ->
                    new ActionResult(
                            true,
                            "Hello. I am Sūtrādhār."
                    );

            case SEARCH ->
                    new ActionResult(
                            false,
                            "Research execution is not implemented yet."
                    );

            default ->
                    new ActionResult(
                            false,
                            "I understand the command, but I cannot execute it yet."
                    );
        };
    }

    private ActionResult listFiles() {

        Path currentDirectory =
                Path.of(".")
                        .toAbsolutePath()
                        .normalize();

        try {

            String files = Files.list(currentDirectory)
                    .map(path ->
                            path.getFileName().toString()
                    )
                    .sorted()
                    .collect(Collectors.joining("\n"));

            return new ActionResult(
                    true,
                    "Files and folders:\n" + files
            );

        } catch (IOException e) {

            return new ActionResult(
                    false,
                    "Unable to read the current directory."
            );
        }
    }

    private ActionResult openApplication(
            String application) {

        if (application == null) {

            return new ActionResult(
                    false,
                    "No application was specified."
            );
        }

        return new ActionResult(
                false,
                "Application control is not implemented yet: "
                        + application
        );
    }
}