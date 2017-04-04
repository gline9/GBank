@echo off
if "%1" NEQ "" goto :usage

set "location=%ProgramFiles%\GBank"
set "datalocation=%AppData%\GBank"
set "shortcutlocation= %appdata%\Microsoft\Windows\Start Menu\Programs"

mkdir "%location%"
mkdir "%datalocation%"
mkdir "%datalocation%\Accounts"

echo.>"%datalocation%\Accounts\userLogins.csv"
echo username^, salt^, password 1>"%datalocation%\Accounts\userLogins.csv"

echo.>"%location%\config.cfg"
echo Data Root^=%datalocation% 1> "%location%\config.cfg"

REM copy over resources to the correct location
robocopy resources "%location%\resources" /s /e 1>NUL

REM copy over program to the correct location
copy GBank.jar "%location%\GBank.jar" 1>NUL

REM create a shortcut in the appropriate directory
REM call shortcutJS.bat -linkfile "%ProgramData%\Microsoft\Windows\Start Menu\Programs\GBank.lnk" -target "javaw.exe" -linkarguments -jar -linkarguments ""%location%\GBank.jar"" -iconlocation "%location%\resources\icon.ico" -workingdirectory "%location%"
call createShortcut.bat "%ProgramData%\Microsoft\Windows\Start Menu\Programs\GBank.lnk" "%location%\GBank.jar" "%location%\resources\icon.ico" "%location%"
echo Installation complete!

goto :done

:usage
echo Usage^: don't use any command line args.

:done