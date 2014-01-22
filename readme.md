# azupload

### What is it?

A command-line tool for fast, parallel mass/bulk uploading to your Azure storage

### Prerequisites?

 * [Visual Studio 2013](http://www.visualstudio.com/downloads/download-visual-studio-vs) (you'll need Windows Desktop **and** Web)
 * [Azure SDK](http://www.windowsazure.com/en-us/downloads/) for VS 2013

### Usage?

Open App.config and fix your connection string accordingly. Then you can run azupload.exe with:

`azupload.exe <azure_storage_container> <local_folder_to_upload>`

**WARNING**: if the files you try to upload already exist in the container, azupload WILL overwrite anything without asking, so use carefully!

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
