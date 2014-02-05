package azupload;

import java.io.*;
import java.util.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.System.out;

/* Azure SDK classes */
import com.microsoft.windowsazure.services.core.storage.*;
import com.microsoft.windowsazure.services.blob.client.*;

public class Azupload
{
    /* workaround for finalized variables in executor threads */
    private static class threadCounter
    {
        public int count;
    }

    static final String settingsFileName = "app.properties";

    static void printHelp()
    {
        out.println("Usage:\n\tazupload --upload <threadCount> <sourceDir> <azureContainer> [containerPrefix]");
    }

    /* helper method for listing files recursively */
    static void getFiles(File dir, ArrayList<String> list)
    {
        try
        {
            File[] files = dir.listFiles();
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    getFiles(file, list);
                }
                else
                {
                    list.add(file.getCanonicalPath());
                }
            }
        }
        catch (IOException e)
        {
            out.println(e.toString());
            System.exit(1);
        }
    }

    /* parallel uploader method */
    static void uploadFolder(int threadCount, final String sourceDir, String azureContainer, final String containerPrefix)
    {
        /* parse the properties file */
        Properties properties = new Properties();
        try
        {
            properties.load(new FileReader(settingsFileName));
        }
        catch (IOException e)
        {
            out.println(e.toString());
            System.exit(1);
        }

        String connString = properties.getProperty("uploadConn", "").trim();

        out.println("Uploading files from \"" + sourceDir + "\" to the \"" + azureContainer + "\" container");

        /* get file list recursively */
        File sourceFolder = new File(sourceDir);
        ArrayList<String> fileList = new ArrayList<>();
        getFiles(sourceFolder, fileList);

        final int fileCount = fileList.size();
        final threadCounter counter = new threadCounter();

        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        try
        {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(connString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            final CloudBlobContainer container = blobClient.getContainerReference(azureContainer);

            for (final Object o : fileList)
            {
                exec.submit(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        String blobItem = null;
                        try
                        {
                            /* make sourceDir the root in the container */
                            blobItem = containerPrefix + o.toString().substring(sourceDir.length() + 1);

                            /* avoid printing several filenames while the counter is of the same value */
                            synchronized (counter)
                            {
                                counter.count++;
                                out.println("Uploading file " + counter.count + "/" + fileCount + " : " + blobItem);
                            }

                            CloudBlockBlob blockBlob = container.getBlockBlobReference(blobItem);
                            File source = new File(o.toString());
                            blockBlob.upload(new FileInputStream(source), source.length());
                        }
                        catch (IOException | java.net.URISyntaxException | StorageException e)
                        {
                            //out.println(e.toString());
                            e.printStackTrace();
                            out.println("Exception thrown while uploading " + blobItem);
                            System.exit(1);
                        }
                    }
                });
            }
        }
        catch (java.security.InvalidKeyException | java.net.URISyntaxException | StorageException e)
        {
            out.println(e.toString());
            System.exit(1);
        }
        finally
        {
            exec.shutdown();
        }

        try
        {
            exec.awaitTermination(1,TimeUnit.DAYS);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Azupload.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        out.println("Upload finished");
    }
    
    public static void main(String[] args) throws IOException
    {
            if (args.length == 0)
            {
                printHelp();
                return;
            }

            String mode = args[0];

            if (mode.equals("--upload"))
            {
                String cPrefix;
                String dirSep = System.getProperty("file.separator");

                switch (args.length)
                {
                    case 4:
                        cPrefix = "";
                        break;
                    case 5:
                        cPrefix = args[4];
                        break;
                    default:
                        printHelp();
                        return;
                }

                /* make sure the prefix will be treated as a virtual folder, not as a file name prefix */
                if (!cPrefix.endsWith(dirSep) && !cPrefix.equals(""))
                {
                    cPrefix = cPrefix + dirSep;
                }

                /* trim the trailing slash from sourceDir, otherwise we'd trim the leading character in filenames */
                String sDir;

                if (args[2].endsWith(dirSep))
                {
                    sDir = args[2].substring(0, args[2].length() - 1);
                }
                else
                {
                    sDir = args[2];
                }

                try
                {
                    uploadFolder(Integer.parseInt(args[1]), sDir, args[3], cPrefix);
                }
                catch (java.lang.NumberFormatException e)
                {
                    out.println(args[1] + "is not a valid number!");
                    System.exit(1);
                }
            }
            else
            {
                printHelp();
            }
    }
}
