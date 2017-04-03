@echo off
if "%~1"NEQ "" goto :usage

set location="%ProgramFiles%"
set datalocation="%AppData%"
set shortcutlocation="%appdata%\Microsoft\Windows\Start Menu\Programs"

mkdir %location%
mkdir %datalocation%
mkdir %datalocation%\Accounts
echo.>%datalocation%\Accounts\userLogins.csv
echo username^, salt^, password 1>%datalocation%\Accounts\userLogins.csv

REM copy over resources to the correct location
robocopy resources %location%\resources /s /e 1>NUL

REM copy over program to the correct location
copy GBank.jar %location%\GBank.jar 1>NUL

echo Installation complete!

goto :done

:usage
echo Usage^: don't use any command line args.

:done