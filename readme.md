# azupload

### What is it

A command-line tool for fast, parallel mass (bulk) uploading blobs to your Azure storage.


### Downloads

 * C#
   * [azupload-csharp](http://www.mediafire.com/download/17w7060dclr2max/azupload-csharp-20140201.7z)
   * [Microsoft .NET Framework 4.5](http://www.microsoft.com/en-us/download/details.aspx?id=30653)
 * Java
   * [azupload-java](http://www.mediafire.com/download/3f1wt1teyw1599u/azupload-java-20140201.7z)
   * [JRE 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)


### Usage

 * C#
   * open *azupload.exe.config* and fix your connection string accordingly
   * `azupload.exe --upload <sourceDir> <azureContainer> [ttlMinutes]`

 * Java
   * open *app.properties* and fix your connection string accordingly
   * `java -jar azupload.jar --upload <threadCount> <sourceDir> <azureContainer> [containerPrefix]`

**WARNING**: if the files you try to upload already exist in the container, *azupload* WILL overwrite anything without asking, so use carefully!


### Building

 * C#
   * [Visual Studio 2013](http://www.visualstudio.com/downloads/download-visual-studio-vs) (you'll need Windows Desktop **and** Web)
 * Java
   * [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
   * [NetBeans Java SE](https://netbeans.org/downloads/index.html) or [Ant](http://ant.apache.org/bindownload.cgi) zip


### Differences between the C# and Java versions

 * In the Java version you need to specify the number of uploader threads. The C# version does this automatically.
 * In the Java version you can specify a container prefix that is prepended to filenames. This allows you to upload files to subfolders under the container root.
 * In the C# version you can specify the TTL for uploaded blobs (i.e. the time it takes for a cache refresh on the CDN nodes).

Of course these could be eliminated, it just fits our needs the way it is. PRs are welcome.


### Speed

For my personal tests I've used a folder containing about 3000 files with a total size of about 300 MiB.

* (unnamed commercial GUI application): 20 minutes
* azupload: 8 minutes


### Improvements

There's an excellent write-up on the topic:

[How to use the Windows Azure Blob Storage Service in .NET](http://www.windowsazure.com/en-us/documentation/articles/storage-dotnet-how-to-use-blobs-20/)


### Why not PowerShell?

In fact the initial version used PowerShell. I came to the following conclusions:

 * It's s-l-o-w.
 * It's extremely poorly documented.
 * It seems to be impossible to run jobs in parallel properly thanks to *InitializationScript*'s *Import-Module* directive being completely ignored when trying to *Start-Job* and thus rendering Azure commands unavailable to the child jobs.


### Does it work on Linux or OS X?

The Java version does! Unfortunately Mono doesn't seem to support Azure (it needs the *Microsoft.WindowsAzure.Storage* assembly) so the C# version is Windows-only for now.
