package burp;

import burp.api.montoya.http.handler.*;
import burp.api.montoya.logging.Logging;

public class MyHttpHandler implements HttpHandler
{
    private final Logging logging;
    private final boolean logResponses;

    public MyHttpHandler(Logging logging, boolean logResponses)
    {
        this.logging = logging;
        this.logResponses = logResponses;
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent httpRequestToBeSent)
    {
        logging.logToOutput("REQUEST " + httpRequestToBeSent.messageId() + ":\n" + httpRequestToBeSent);

        return RequestToBeSentAction.continueWith(httpRequestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived httpResponseReceived)
    {
        if (logResponses)
        {
            logging.logToOutput("RESPONSE " + httpResponseReceived.messageId() + " content-length: " + httpResponseReceived.headerValue("Content-Length"));
        }

        return ResponseReceivedAction.continueWith(httpResponseReceived);
    }
}