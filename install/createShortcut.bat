@if (@X)==(@Y) @end /* JScript comment
@echo off
cscript //E:JScript //nologo "%~f0" "%~nx0" %*

exit /b %errorlevel%
@if (@X)==(@Y) @end JScript comment */

var args=WScript.Arguments;

// grab command line arguments
var location = args.Item(1);
var target = args.Item(2);
var icon = args.Item(3);
var workingDirectory = args.Item(4);

// grab the shell
var shell = new ActiveXObject("WScript.Shell");

// create the link file
var link = shell.CreateShortcut(location);

// set link properties
link.TargetPath = "javaw.exe";
link.Arguments = "-jar \"" + target + "\"";
link.IconLocation = icon;
link.WorkingDirectory = workingDirectory;

// save the link
link.Save();