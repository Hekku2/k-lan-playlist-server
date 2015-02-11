ECHO Copying jar
copy ..\target\lanplaylistserver-0.0.1-SNAPSHOT.jar
rename lanplaylistserver-0.0.1-SNAPSHOT.jar lanplayer.jar

ECHO Copying resources
mkdir resources
xcopy ..\src\main\resources\net\kokkeli\resources resources /S

ECHO Copying settings
mkdir settings
xcopy ..\settings\publish.dat settings

ECHO Copying libraries
mkdir lib
xcopy ..\lib lib

ECHO Copying tools
mkdir tools
xcopy ..\tools tools

ECHO Copying db
mkdir db
xcopy ..\db\basic_structure.sql db
xcopy ..\db\create_database.bat db
xcopy ..\db\insert_start_data.bat db
xcopy ..\db\sqlite3.exe db
xcopy ..\db\start_data.sql db

ECHO Creating database
cd db
create_database.bat
insert_start_data.bat
cd..
