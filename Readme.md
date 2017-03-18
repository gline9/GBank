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
   1. Type in the log in details for your account.
   1. Press the **Confirm** button
   1. You will now be brought to your account pane
1. Add accounts to your list of accounts
   1. Sign on to your account if you aren't already in it.
   1. Click the **Add Account** button
   1. Enter your account details.
      1. **Account Name** is the name you would like to give the account, if left blank it will default to the account id.
      1. **Principal Balance** is how much money you have in the account. Put in a negative balance for a loan.
      1. **Interest Rate** is the amount of interest in absolute terms, for instance 5% would be entered as .05
      1. **Compounds per Year** is how often in a year your account compounds, for instance you would enter 12 for compounding monthly.
   1. Click the **Confirm** button and your account will be added.
1. View account details
   1. Click on the account that you want the details of.
   1. A window will open up showing you the name, balance, interest rate, and how often the account is compounded
   1. To change the name of the account click the **&#9998;** button, enter in the new name of the account in the 
    new field, and click the **&#10003;** button to save the value.
   1. To remove the account just click the **Remove** button at the bottom of the screen.