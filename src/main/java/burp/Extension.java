package burp;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;

import java.time.Instant;

@SuppressWarnings("unused")
public class Extension implements BurpExtension
{
    private final boolean logResponses = true;                                  // CHANGE ME

    public static final String EXTENSION_NAME = "Log to File";
    @Override
    public void initialize(MontoyaApi montoyaApi)
    {
        Instant instant = Instant.now();

        montoyaApi.extension().setName(EXTENSION_NAME);

        montoyaApi.logging().logToOutput(Extension.EXTENSION_NAME + " - Loaded");

        montoyaApi.http().registerHttpHandler(new MyHttpHandler(montoyaApi.logging(), logResponses));
    }
}
