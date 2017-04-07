## GBank

### Installation
1. Download the newest release from GitHub
1. For linux installation follow these steps
   1. Run the `linuxInstall.sh` script in the command line with arguments of where it should install
    if this doesn't work try running the command `chmod +x linuxInstall.sh` before running it again.
   1. After the installation completes the program should be copied over to the directory specified.
    You can delete the folder that was downloaded as the source and enjoy the software!
1. For windows installation use a version after v0.2 and follow these steps
   1. Open a new command prompt by typing in `cmd` to the search. Right click on the program and choose
    **Run as Administrator**.
   1. Navigate to the installation directory in the prompt and run `windowsInstall.bat`.
   1. When the installation completes you will have the program installed and can search for it
    in the windows search.
 
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
   1. From the **Edit** menu click the **Add Account** option
   1. Enter your account details.
      1. **Account Name** is the name you would like to give the account, if left blank it will default to the account id.
      1. **Principal Balance** is how much money you have in the account. Put in a negative balance for a loan.
      1. **Interest Rate** is the amount of interest in absolute terms, for instance 5% would be entered as .05
      1. **Compounds per Year** is how often in a year your account compounds, for instance you would enter 12 for compounding monthly.
   1. Click the **Confirm** button and your account will be added.
   1. To log off when you are done open the **File** drop out and choose the **Log Out** option.
1. View account details
   1. Click on the account that you want the details of.
   1. A window will open up showing you the name, balance, interest rate, and how often the account is compounded
   1. To change the name of the account click the **&#9998;** button, enter in the new name of the account in the 
    new field, and click the **&#10003;** button to save the value.
   1. To remove the account just open the **File** menu and choose **Remove**.
1. Transfer Funds
   1. Open the account detail window
   1. From the **Transfer** menu choose either the **Transfer From** or the **Transfer To** option. You also can choose the **Withdraw** 
    or **Deposit** options to perform those options.
   1. In the displayed gui choose the other account you want to transfer from or to. If you selected **Deposit** or **Withdraw** these will
    be chosen for you.
   1. Enter the amount in the **Transfer Amount** field.
   1. Click the **Transfer** button to complete the transfer
   1. A dialog will pop up letting you know how much money was successfully transfered, this will be different than amount requested
    if an overdraw would have occurred, or from depositing too much into a loan.