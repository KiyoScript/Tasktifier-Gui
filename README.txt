Tasktifier GUI

PRE-REQUIREMENTS:
- JDK 21
- XAMPP

PRE-REQUIREMENTS INSTALLATION:
- JDK 21
1. Download JDK 21 from the links below:
WINDOWS -> (https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.exe)
LINUX -> (https://download.oracle.com/java/21/latest/jdk-21_linux-aarch64_bin.tar.gz)
2. Follow the instructions provided by the installer.
3. After the installation is complete, delete the downloaded file to recover disk space.

FOR WINDOWS:
4. Type "system environment variables" on Windows search bar and press "Enter".
5. Select "Environment variables..." and click "Path" in the User Variables and System Variables.
6. Click Edit.
7. Add the location of the JDK bin directory to the PATH statement. Here's the usual PATH link of JDK bin for Windows (C:\Program Files\Java\jdk-21\bin). You can add another PATH (C:\Program Files\Common Files\Oracle\Java\javapath) to ensure JDK is added to PATH.
8. Click OK to successively close each window.

FOR LINUX:
4. Open command prompt for Linux.
5. Open the .bashrc file.
6. Add the following line to the file. Replace the JDK directory with the name of your java installation directory. Here's the usual PATH link of JDK bin for Linux (export PATH=/usr/java/<JDK Directory>/bin:$PATH).
7. Save the file and exit.
8. Use the source command to force Linux to reload the .bashrc file which normally is read only when you log in each time. (source .bashrc)

- XAMPP
1. Download XAMPP from the links below:
WINDOWS -> (https://sourceforge.net/projects/xampp/files/XAMPP%20Windows/8.2.12/xampp-windows-x64-8.2.12-0-VS16-installer.exe)
LINUX -> (https://sourceforge.net/projects/xampp/files/XAMPP%20Linux/8.2.12/xampp-linux-x64-8.2.12-0-installer.run)
2. Follow the instructions provided by the installer.
3. After the installation is complete, delete the downloaded file to recover disk space.

INSTRUCTIONS:
1. Open "XAMPP Control Panel" with "Run as Administrator".
2. Start "Apache" and "MySQL" modules.
3. Type `localhost/phpmyadmin` in your favorite browser.
4. Create a database called `tasktifier_db`.
5. Choose `tasktifier_db`, press "Import" and select "tasktifier_db.sql" from `C:\<your-path>\Tasktifier-Gui\db`.
6. After that, select `Go` from "Import" interface and you're good to go.

7. Type `CMD` in this directory `C:\<your-path>\Tasktifier-Gui\dist>`.
8. Type `java -jar "Tasktifier.jar` and press "Enter".
9. Sign up first to create an account by selecting `Create an Account!` button.
10. After sign-up, log-in your account.
11. Inside the application, you can create a task, set how many minutes to snooze your task, and select your favorite music when reminding a task. Music only supports `.wav` and `.mp3` files.
12. When you've completed the task, you can select `Done` button. If you accidentally added the task with incorrect information, you can select `Delete` button.
13. When the task reaches beyond due date, the task will automatically set to `FAILED`.
14. You can minimize the application by pressing `-`, hide the application from the system by pressing `x`, or log-out your account by pressing `↪`.

15. Tasks that are in due date will be displayed in `Today` interface including how many tasks are in today's tasks. 
16. You can see your previous tasks in `Details` interface whether the task was `COMPLETED` or `FAILED`.

17. That's it. Have fun using this system. ^_^

ADMIN ACCESS!!!
- For admin access, here's the email and password. ACCESS IT AT YOUR OWN RISK!:
EMAIL -> admin@tasktifier.com
PASSWORD -> admin123
