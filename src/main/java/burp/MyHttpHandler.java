package burp;

import burp.api.montoya.http.handler.*;

import java.util.concurrent.LinkedBlockingQueue;

public class MyHttpHandler implements HttpHandler
{
    private final Logger logger;
    private final boolean logResponses;
    private final LinkedBlockingQueue<String> queue;

    public MyHttpHandler(
            Logger logger,
            boolean logResponses,
            LinkedBlockingQueue<String> queue
    )
    {
        this.logger = logger;
        this.logResponses = logResponses;
        this.queue = queue;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent)
    {
        log(httpRequestToBeSent.toString());

        return RequestToBeSentAction.continueWith(httpRequestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived)
    {
        if (logResponses)
        {
            log(httpResponseReceived.toString());
        }

        return ResponseReceivedAction.continueWith(httpResponseReceived);
    }

    private void log(String message)
    {
        try
        {
            queue.add(message);
        }
        catch (IllegalStateException e)
        {
            logger.logError("Failed to add message to queue", e);
        }
    }
}
