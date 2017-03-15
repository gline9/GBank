#!/bin/bash
if [ $# -ne 1 ]
then
	echo "Usage ./linuxInstall.sh <Install location>"
	exit 0
fi

location=$1
mkdir -p $location
mkdir $location/Accounts
touch $location/Accounts/userLogins.csv
echo "username, salt, password" > $location/Accounts/userLogins.csv

exit 0