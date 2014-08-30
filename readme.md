# azupload

### What is it

A command-line tool for fast, parallel mass (bulk) uploading blobs to your Azure storage.


### Downloads

 * C#
   * [azupload-csharp](http://www.mediafire.com/download/ihtssa9fq575u8w/azupload-csharp-20140829.7z)
   * [Microsoft .NET Framework 4.5](http://www.microsoft.com/en-us/download/details.aspx?id=30653) or [Mono MRE 3.6](http://www.mono-project.com/download/)
 * Java
   * [azupload-java](http://www.mediafire.com/download/q2jrn5weoxlf0ys/azupload-java-20140829.7z)
   * [JRE 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)


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
   * [Visual Studio 2013](http://www.visualstudio.com/downloads/download-visual-studio-vs) (you'll need Windows Desktop **and** Web) or [Mono MDK 3.6](http://www.mono-project.com/download/) + [MonoDevelop 5](http://monodevelop.com/Download)
 * Java
   * [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
   * [NetBeans 8 Java SE](https://netbeans.org/downloads/index.html) or [Ant](http://ant.apache.org/bindownload.cgi) zip


### Differences between the C# and Java versions

 * In the Java version you need to specify the number of uploader threads. The C# version does this automatically.
 * In the Java version you can specify a container prefix that is prepended to filenames. This allows you to upload files to subfolders under the container root.
 * In the C# version you can specify the TTL for uploaded blobs, i.e. the time it takes for a cache refresh to occur on the CDN nodes.

Of course these could be eliminated, it just fits our needs the way it is. PRs are welcome.


### Speed

For my personal tests I've used a folder containing about 3000 files with a total size of about 300 MiB.

* (unnamed commercial GUI application): 20 minutes
* azupload: 8 minutes


### Improvements

* [How to use Blob Storage from .NET](http://azure.microsoft.com/en-us/documentation/articles/storage-dotnet-how-to-use-blobs/)
* [How to use Blob Storage from Java](http://azure.microsoft.com/en-us/documentation/articles/storage-java-how-to-use-blob-storage/)


### Why not PowerShell?

In fact the initial version used PowerShell. I came to the following conclusions:

 * It's s-l-o-w.
 * It's extremely poorly documented.
 * It seems to be impossible to run jobs in parallel properly thanks to *InitializationScript*'s *Import-Module* directive being completely ignored when trying to *Start-Job* and thus rendering Azure commands unavailable to the child jobs.


### Does it work on Linux or OS X?

Yes, it does!
