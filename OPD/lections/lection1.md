# Lection 1
## Unix
### Root directory
/dev - devices  
/bin - binary exxecutable files  
/etc - system settings  
/home - all users`s data  

### Paths
1. Absolute (relative to root dir)
2. Relative

. - link current dir  
.. - link to parent dir  

**Example**: cd /.. - switch to /

### Base commands
* man - check manual
* cd - change directory
    * "cd -" - go to root
    * cd - go to /home
* echo - display line of text
    * "-e" enable backslash escapes
* mkdir - create directory
* touch - change last file access time (create file if not exists)
* chmod - change file access rights
* ln - create link
    * symbolic "-s" - create file containing path to target
    * hard - create copy of file pointing to same data on disk
* cat - display file
* ls - list of directory files
    * "ls -1" - micro list view with only names 
* cp - copy file
* mv - move (rename) file
    * "-r" - recursive move
* pwd - display current directory
* rm - remove file
    * "-f" - does not display error message
    * "-r" - recursive remove
* rmdir - remove directory
* tail - display last part of file
* head - display first part of file
* less - display content of file in few pages
* more - display content of file one screen (or page) at a time
* sort - sort file rows
* wc - display lines, chars, bytes count
    * "-b" - bytes
    * "-c" - chars
    * "-l" - lines
* grep - display lines that match pattern
    * "-i" - dont check register
    * "-v" - inverse match
### RegExp
"*" - every count of symbols  
"+" - one symbol  
"." - random symbol  
"/." - dot symbol  
"^" - start of string  
{1,3} - range from 1 to 3  
{1,} - range from 1 to inf  
{3} - only 3 times  

### File modes (chmod)
Group -> users  

Modes: read(r), write(w), execute(x)  
each mode can be set for group and user  

If you ls dir, files will have such permission info:  
``` -rwxrwxrwx ```
or 
``` drwxrwxrwx ```  

'd' stand for directory  
Permissions order: for user, group, everyone else

you can numbers to set rigths. Example:
740 means `-rwxr-----` 

The user who owns it (u), other users in the file's group (g), other users not in the file's  group  (o),  or all  users (a).  If none of these are given, the effect is as if (a) were given, but bits that are set in the umask are not affected

For dir (x) mode means that you can/cannot cd to this dir. However you can 'ls' it.



**Example:** `chmod a+x exec_file` 

### File descriptors
0 for standard input, 1 for standard output, and 2 for standard error.  

### Output redirection
">" - overwrite file  
">>" - append data to file  
"<< EOF" - cat prints input data until you type EOF  
cat <<< 'text' - will just display 'text'  

2>&1 - redirects data from std.error to std.output  
'$' symbol needs to specify that '1' is descriptor, not a file  
