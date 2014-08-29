using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;
using System.IO;
using Microsoft.WindowsAzure.Storage;
using Microsoft.WindowsAzure.Storage.Auth;
using Microsoft.WindowsAzure.Storage.Blob;

namespace azupload
{
    class Program
    {
        static void printHelp()
        {
            Console.WriteLine("Usage:\n\tazupload --upload <sourceDir> <azureContainer> [ttlMinutes]");
        }

        static void uploadFolder(string sourceDir, string azureContainer, int ttlMins)
        {
            string connString = ConfigurationManager.AppSettings["uploadConn"];
            int ttlSecs = ttlMins * 60;

            Console.WriteLine("Uploading files from \"" + sourceDir + "\" to the \"" + azureContainer + "\" container");

            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(connString);
            CloudBlobClient blobClient = storageAccount.CreateCloudBlobClient();
            CloudBlobContainer container = blobClient.GetContainerReference(azureContainer);

            string[] fileList = Directory.GetFiles(sourceDir, "*", SearchOption.AllDirectories);
            int fileCount = fileList.Count();
            int i = 0;

            /* serial version:
             * foreach (var item in fileList)
             */
            Parallel.ForEach(fileList, item =>
            {
                i++;

                /* cut leading path */
                string blobItem = item.Substring(sourceDir.Length + 1);
                Console.WriteLine("Uploading file " + i + "/" + fileCount + " with " + ttlMins + " min TTL : " + blobItem);

                CloudBlockBlob blockBlob = container.GetBlockBlobReference(blobItem);

                using (var fileStream = System.IO.File.OpenRead(item))
                {
                    blockBlob.UploadFromStream(fileStream);
                    blockBlob.Properties.CacheControl = "public, max-age=" + ttlSecs;
                    blockBlob.SetProperties();
                }
            });

            Console.WriteLine("Upload finished");
        }

        static void Main(string[] args)
        {
            if (args.Length == 0)
            {
                printHelp();
                return;
            }

            string mode = args[0];

            if (mode.Equals("--upload"))
            {
                if (args.Length < 3)
                {
                    printHelp();
                    return;
                }

                /* trim trailing slashes */
                string sDir;
                if (args[1].EndsWith("\\"))
                {
                    sDir = args[1].Substring(0, args[1].Length - 1);
                }
                else
                {
                    sDir = args[1];
                }

                /* if TTL is supplied, try to parse it, otherwise use a default of 1440 minutes (1 day) */
                int ttl = 1440;
                if (args.Length == 4)
                {
                    try
                    {
                        ttl = Convert.ToInt32(args[3]);
                    }
                    catch (Exception)
                    {
                        printHelp();
                        return;
                    }
                }

                uploadFolder(sDir, args[2], ttl);
            }
            else
            {
                printHelp();
            }
        }
    }
}
