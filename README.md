# Log to File
For Burp Suite Enterprise Edition

Please note that extensions are written by third party users of Burp, and PortSwigger makes no warranty about their quality or usefulness for any particular purpose.

---
This extension will generate an .txt file, containing all requests issued, and (optionally) responses received. The file will be stored on the Scanning Machine that performed the scan.

The filename of the scan will be the start time of the scan, in the format `logging-file-yyyy-MM-dd-HH-mm`.

## Limitations
- File will be stored on Scanning Machine.
- Previous reports will not be removed from the folder, so ensure that you are regularly cleaning up old files.
- This extension assumes you are using a Standard install. This has not been tested on a Cloud deployment.

## Usage
1. Download this repository, and check the `folderPath` variable in `Extension.java` is pointed to a location where you have write permission **on the Scanning Machine**. Configure your `logResponses` variables to `true` or `false`, according to whether you would like to log responses or not.
2. Build the extension using `./gradlew build`.
3. [Load the extension into Burp Enterprise](https://portswigger.net/burp/documentation/enterprise/working/scans/extensions), and add the extension to your Site Details page.
4. Run a scan as normal.
5. Retrieve your file from your Scanning Machine - it will be located according to the `folderPath` that is set. If in doubt, the file location and name will be output in the Scan log and the event log.

## Troubleshooting
If the logging file has not been generated, check your scan log for any exceptions - Scan > Logging > Scan debug pack > Download. The file name is `scan-XX.log` where `XX` is your scan ID number.

If you have received a `FileNotFoundException (Permission denied)`, then make sure that you are writing your report to a location where you have write permission.

### Using Gradle
- If you do not have Gradle already installed, follow the installation instructions [here](https://gradle.org/install/).
- Once Gradle is installed, run `./gradlew fatJar` from the installation directory using the command line.

If no changes to the code are required, a prebuilt JAR file is available under [Releases](https://github.com/Hannah-PortSwigger/LogToFile/releases). It is preferable to compile your own JAR file.
