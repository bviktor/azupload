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
        static void Main(string[] args)
        {
            string connString = ConfigurationManager.AppSettings["uploadConn"];
            string azureContainer = args[0];
            string rootFolder = args[1];

            Console.WriteLine("Uploading files from \"" + rootFolder + "\" to the \"" + azureContainer + "\" container");

            CloudStorageAccount storageAccount = CloudStorageAccount.Parse(connString);
            CloudBlobClient blobClient = storageAccount.CreateCloudBlobClient();
            CloudBlobContainer container = blobClient.GetContainerReference(azureContainer);

            string[] fileList = Directory.GetFiles(rootFolder, "*", SearchOption.AllDirectories);
            int fileCount = fileList.Count();
            int i = 0;

            /* serial version:
             * foreach (var item in fileList)
             */
            Parallel.ForEach(fileList, item =>
            {
                i++;

                /* cut leading path */
                string blobItem = item.Substring(rootFolder.Length + 1);
                Console.WriteLine("Uploading file " + i + "/" + fileCount + " : " + blobItem);

                CloudBlockBlob blockBlob = container.GetBlockBlobReference(blobItem);

                using (var fileStream = System.IO.File.OpenRead(item))
                {
                   blockBlob.UploadFromStream(fileStream);
                }
            });

            Console.WriteLine("Upload finished");
        }
    }
}
