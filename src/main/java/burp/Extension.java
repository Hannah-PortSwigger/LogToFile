package burp;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;

import static java.time.ZoneId.systemDefault;

public class Extension implements BurpExtension
{
    private final String folderPath = "/Users/hannah.law/Downloads/";   // CHANGE ME
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
        Path filePath = Path.of(folderPath, "logging-file-" + dateTimeFormatter.format(instant) + ".txt");

        LinkedBlockingQueue<String> toWriteToFileQueue = new LinkedBlockingQueue<>();

        Thread fileWritingThread = new Thread(new WriteFileFromQueue(logger, filePath, toWriteToFileQueue));
        fileWritingThread.start();
        logger.logOutput(fileWritingThread.getState().name());

        montoyaApi.http().registerHttpHandler(new MyHttpHandler(logger, logResponses, toWriteToFileQueue));
        montoyaApi.extension().registerUnloadingHandler(fileWritingThread::interrupt);
    }
}
