call "%VS120COMNTOOLS%\VsDevCmd.bat"
msbuild azupload.sln /p:Configuration=Release
