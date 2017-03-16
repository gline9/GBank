## GBank

### Installation
1. Download the newest release from GitHub
2. Run the `linuxInstall.sh` script in the command line with arguments of where it should install
 if this doesn't work try running the command `chmod +x linuxInstall.sh` before running it again.
3. After the installation completes the program should be copied over to the directory specified.
 You can delete the folder that was downloaded as the source and enjoy the software!
 
### Compilation
* **Required Libraries**
  * `GCore`
  * `GFiles`
  * `GEncode`
  * `GMath`
* Required libraries can be found in the `GLibrary` repository.
* To compile, compile the libraries with the source code into one file. The main method for the program
 is located in `gbank.start.GBankStart.java`.