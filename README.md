# Start working with eclipse

Prerequisites:
- an eclipse version (3.5 up)
- a maven installation (2.2.1 up)
- maven eclipse plugin


Open eclipse in a new workspace, say:

    /home/john/java/audiobox


Now open a terminal and move to the workspace and verify that .metadata folder is created

    cd /home/john/java/audiobox
    ls -al

    [84 ~/java/audiobox]
    [keytwo@kahlan $] > ls -al

    drwxr-xr-x  4 john john 4,0K 2010-07-06 19:33 .metadata


It's now time to clone the repos:

    git clone git@github.com:audiobox/AudioBox.fm-JavaLib.git .


If everything goes fine you have now a copy of the master branch, you are ready to start:


    mvn install eclipse:eclipse


Once done, you can now get back to eclipse and import the newly configured maven projects.


# Generate JavaDoc site pages

    mvn javadoc:javadoc -Denv=production