package burp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.sleep;

public class WriteFileFromQueue implements Runnable
{
    private final Logger logger;
    private final Path filePath;
    private final LinkedBlockingQueue<String> toWriteToFileQueue;

    public WriteFileFromQueue(
            Logger logger,
            Path filePath,
            LinkedBlockingQueue<String> toWriteToFileQueue
    )
    {
        this.logger = logger;
        this.filePath = filePath;
        this.toWriteToFileQueue = toWriteToFileQueue;
    }

    @Override
    public void run()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toString(), true)))
        {
            List<String> buffer = new ArrayList<>();

            while (!Thread.currentThread().isInterrupted())
            {
                toWriteToFileQueue.drainTo(buffer);
                if (!buffer.isEmpty())
                {
                    logger.logOutput("Buffer size: " + buffer.size());
                }

                for (String s : buffer)
                {
                    writer.write(s);
                    writer.newLine();
                }

                writer.flush();
                buffer.clear();

                sleep(5000);
            }
        }
        catch (IOException e)
        {
            logger.logError("Failed to write to file", e);
        }
        catch (InterruptedException e)
        {
            logger.logOutput("Shutting down file writing");
            Thread.currentThread().interrupt();
        }
        catch (Exception e)
        {
            logger.logError("Fatal error occurred", e);
            Thread.currentThread().interrupt();
        }
    }
}
