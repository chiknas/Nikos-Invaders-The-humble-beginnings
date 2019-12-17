;This file will be executed next to the application bundle image
;I.e. current directory will contain folder NikosInvaders with application files
[Setup]
AppId={{org.springframework.boot.loader}}
AppName=NikosInvaders
AppVersion=1.0
AppVerName=NikosInvaders 1.0
AppPublisher=NikosKyknas
AppComments=Invaders.exe
AppCopyright=Copyright (C) 2019
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={localappdata}\NikosInvaders
DisableStartupPrompt=Yes
DisableDirPage=No
DisableProgramGroupPage=Yes
DisableReadyPage=No
DisableFinishedPage=No
DisableWelcomePage=No
DefaultGroupName=NikosKyknas
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=NikosInvaders-1.0
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=NikosInvaders\NikosInvaders.ico
UninstallDisplayIcon={app}\NikosInvaders.ico
UninstallDisplayName=NikosInvaders
WizardImageStretch=No
WizardSmallImageFile=NikosInvaders-setup-icon.bmp
ArchitecturesInstallIn64BitMode=x64


[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "NikosInvaders\NikosInvaders.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "NikosInvaders\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\NikosInvaders"; Filename: "{app}\NikosInvaders.exe"; IconFilename: "{app}\NikosInvaders.ico"; Check: returnTrue()
Name: "{commondesktop}\NikosInvaders"; Filename: "{app}\NikosInvaders.exe";  IconFilename: "{app}\NikosInvaders.ico"; Check: returnTrue()


[Run]
Filename: "{app}\NikosInvaders.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\NikosInvaders.exe"; Description: "{cm:LaunchProgram,NikosInvaders}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\NikosInvaders.exe"; Parameters: "-install -svcName ""NikosInvaders"" -svcDesc ""NikosInvaders"" -mainExe ""NikosInvaders.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\NikosInvaders.exe "; Parameters: "-uninstall -svcName NikosInvaders -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
