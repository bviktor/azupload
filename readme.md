# azupload

Command-line tool for fast, parallel mass/bulk uploading to your Azure storage

# Prerequisites

 * [Visual Studio 2013](http://www.visualstudio.com/downloads/download-visual-studio-vs) (you'll need Windows Desktop **and** Web)
 * [Azure SDK](http://www.windowsazure.com/en-us/downloads/) for VS 2013

# Usage

Open App.config and fix your connection string accordingly. Then you can run azupload.exe with:

`azupload.exe <azure_storage_container> <local_folder_to_upload>`

**WARNING**: if the files you try to upload already exist in the container, azupload WILL overwrite anything without asking, so use carefully!
