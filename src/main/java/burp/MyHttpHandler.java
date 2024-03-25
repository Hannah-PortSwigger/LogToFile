package burp;

import burp.api.montoya.http.handler.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyHttpHandler implements HttpHandler
{
    private final Logger logger;
    private final boolean logResponses;
    private final Path filePath;

    public MyHttpHandler(Logger logger, boolean logResponses, Path filePath)
    {
        this.logger = logger;
        this.logResponses = logResponses;
        this.filePath = filePath;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent)
    {
        logToFile(httpRequestToBeSent.toString());

        return RequestToBeSentAction.continueWith(httpRequestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived)
    {
        if (logResponses)
        {
            logToFile(httpResponseReceived.toString());
        }

        return ResponseReceivedAction.continueWith(httpResponseReceived);
    }

    private void logToFile(String message) // TODO better file writing strategy
    {
        try
        {
            Files.writeString(filePath, message);
        }
        catch (IOException e)
        {
            logger.logError("Failed to write to file", e);
        }
    }
}
