@echo off
if "%~1"=="" goto :usage
if "%~2" NEQ "" goto :usage

set location=%1

mkdir %location%
mkdir %location%\Accounts
echo.>%location%\Accounts\userLogins.csv
echo username^, salt^, password 1>%location%\Accounts\userLogins.csv

REM copy over resources to the correct location
robocopy resources %location%\resources /s /e 1>NUL

REM copy over program to the correct location
copy GBank.jar %location%\GBank.jar 1>NUL

echo Installation complete!

goto :done

:usage
echo Usage windowsInstall.sh ^<Install location^>

:done