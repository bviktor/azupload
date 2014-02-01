# azupload

### What is it?

A command-line tool for fast, parallel mass (bulk) uploading to your Azure storage.

### Prerequisites?

 * C#
   * [Visual Studio 2013](http://www.visualstudio.com/downloads/download-visual-studio-vs) (you'll need Windows Desktop **and** Web)
   * [Azure SDK](http://www.windowsazure.com/en-us/downloads/) for VS 2013
 * Java
   * [JDK 7](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
   * [NetBeans Java SE](https://netbeans.org/downloads/index.html) or [Ant](http://ant.apache.org/bindownload.cgi) zip

### Usage?

 * C#
   * open *csharp\App.config* and fix your connection string accordingly
   * build in Visual Studio or with MSBuild (see *build.bat*)
   * `azupload.exe --upload <sourceDir> <azureContainer>`

 * Java
   * open *java\app.properties* and fix your connection string accordingly
   * build in NetBeans or with Ant (see *build.bat*)
   * `java -jar azupload.jar --upload <sourceDir> <azureContainer> <threadCount>`

**WARNING**: if the files you try to upload already exist in the container, *azupload* WILL overwrite anything without asking, so use carefully!

### Speed?

For my personal tests I've used a folder containing about 3000 files with a total size of about 300 MiB.

* (unnamed commercial GUI application): 20 minutes
* azupload: 8 minutes

### Improvements?

There's an excellent write-up on the topic:

[How to use the Windows Azure Blob Storage Service in .NET](http://www.windowsazure.com/en-us/documentation/articles/storage-dotnet-how-to-use-blobs-20/)

### Why not PowerShell?

In fact the initial version used PowerShell. I came to the following conclusions:

 * It's s-l-o-w.
 * It's extremely poorly documented.
 * It seems to be impossible to run jobs in parallel properly thanks to *InitializationScript*'s *Import-Module* directive being completely ignored when trying to *Start-Job* and thus rendering Azure commands unavailable to the child jobs.

### Does it work on Linux or OS X?

The Java version does! Unfortunately Mono doesn't seem to support Azure (it needs the *Microsoft.WindowsAzure.Storage* assembly) so the C# version is Windows-only for now.
