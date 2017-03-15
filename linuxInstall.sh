#!/bin/bash
if [ $# != 1 ] then
	echo "Usage ./linuxInstall.sh <Install location>"
	exit
fi

set location=$1
mkdir "$1"/Accounts
touch "$1"/Accounts/userLogins.csv
echo "username, salt, password" > "$1"/Accounts/userLogins.csv