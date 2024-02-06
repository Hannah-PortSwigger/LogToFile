package burp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BurpExtender implements IBurpExtender, IHttpListener {
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private PrintWriter stdout, stderr;
    private static String NAME = "Logger for Burp Suite Enterprise Edition";
    private LocalDateTime extensionLoadedTime;
    private String fileName;
    private File loggingFile;

    // CHANGE ME
    private String folderPath = "/var/log/BurpSuiteEnterpriseEdition/";     // Mac/Linux format
//    private String folderPath = "C:\\Users\\MyName\\";                    // Windows format
    private boolean logRequests = true;
    private boolean logResponses = true;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        this.callbacks = callbacks;
        helpers = callbacks.getHelpers();

        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);

        callbacks.registerHttpListener(this);
        callbacks.setExtensionName(NAME);

        extensionLoadedTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        fileName = "log-" + dtf.format(extensionLoadedTime) + ".txt";

        stdout.println(NAME + " - loaded");

        createNewFile(folderPath + fileName);
    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {
        if (logRequests && messageIsRequest)
        {
            String rqString = helpers.bytesToString(messageInfo.getRequest());
            writeToFile(rqString);
        }
        if (logResponses && !messageIsRequest)
        {
            String respString = helpers.bytesToString(messageInfo.getResponse());
            writeToFile(respString);
        }
    }

    private void createNewFile(String filePath)
    {
        try
        {
            loggingFile = new File(filePath);

            if (loggingFile.createNewFile())
            {
                stdout.println(NAME + " - File created: " + filePath);
            }
            else
            {
                System.out.println(NAME + " - " + filePath + " already exists.");
            }
        }
        catch (IOException e)
        {
            stderr.println(NAME + " - An error occurred creating file.");
            e.printStackTrace();
        }
    }

    private void writeToFile(String message)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(loggingFile, true);
            fileWriter.write(message + "\r\n\r\n");
            fileWriter.close();
        }
        catch (IOException e)
        {
            stderr.println(NAME + " - An error writing to file occurred.");
            e.printStackTrace();
        }
    }
}