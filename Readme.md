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
 
### Usage
1. Add a new Account
  1. Click the **New Account** button
  2. Type in the log in details for your account.
  3. Press the **Confirm** button
  4. You will now be brought to your account pane
2. Add accounts to your list of accounts
  1. Sign on to your account if you aren't already in it.
  2. Click the **Add Account** button
  3. Enter your account details.
    1. **Principal Balance** is how much money you have in the account.
    2. **Interest Rate** is the amount of interest in absolute terms, for instance 5% would be entered as .05
    3. **Compounds per Year** is how often in a year your account compounds, for instance you would enter 12 for compounding monthly.
  4. Click the **Confirm** button and your account will be added.