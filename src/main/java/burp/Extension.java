package burp;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneId.systemDefault;

public class Extension implements BurpExtension
{
    private final String folderPath = "/var/log/BurpSuiteEnterpriseEdition/";   // CHANGE ME
    private final boolean logResponses = true;                                  // CHANGE ME

    public static final String EXTENSION_NAME = "Log to File";
    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        Instant instant = Instant.now();

        montoyaApi.extension().setName(EXTENSION_NAME);

        Logger logger = new Logger(montoyaApi.logging());
        logger.logOutput("Loaded");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm").withZone(systemDefault());

        Path filePath = Path.of(folderPath + "logging-file-" + dateTimeFormatter.format(instant) + ".txt");

        try
        {
            Files.createFile(filePath);
            logger.logOutput("File created at " + filePath);
            montoyaApi.http().registerHttpHandler(new MyHttpHandler(logger, logResponses, filePath));
        }
        catch (IOException e)
        {
            logger.logError("Failed to create file", e);
        }
    }
}
