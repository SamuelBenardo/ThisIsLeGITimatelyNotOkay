# ThisIsLeGITimatelyNotOkay

In this project we are trying to recreate a version of GitHub using our programming skills.

Github.java: Basis for all functions
GithubTester.java: all testers for the class

GITHUB FUNCTIONS:

initializeDirs()
    Creates the main directories required for Github. This includes: HEAD, objects, index, and git itself.
    it first creates a list of all files using the method createDirList, then turns each into it's perspective file-type (IF IT DOES NOT ALREADY EXIST) by through looping or if statements (I forgot objects was a directory at first, so the formatting is a little weird)
    ends by printing out if the main git repository was sucessfully created

createDirList()
    uses a for loop to link the "./git/" structure to all internal files. this is to make creation easier, since the path is already defined.

createBLOBfile(File f)
    takes given file and turns it into a blob located in ./git/objects. The name of this file is that of the hash, and the contents are transferred into the blob-file using file writer

fileWriter(String toWrite, File f) 
    uses buffered writer to transcribe toWrite into f

isEmpty(File f)
    checks if the file is empty -- might not work for encoded files (BE WARNED!!)

readFile(File f) 
    Reads given File & returns contents as a string

hashFile(String contents) 
    takes the string and converts it into sha1

byteToHex(final byte[] hash)
    i have no clue what this does just trust the process

updateIndex(String sha1, String fileName)
    adds the index in the format sha1 + " " + filename.
    each new line should be a new index but currently THIS DOES NOT WORK


TESTER:

testDirExistence() 
    goes through each core file for Github & tells you if they exist. 
    returns the result if all the necessary files existed.

resetDirectories()
    removes ./git and all internal files.

deleteAllFiles(File dir)
    recursively deletes internal files of given dir

dirTest(int numCycles)
    adds and resets the directioes numCycles amt of times, confirms if each individual test worked

doesFileBLOB(File f)
    creates a blob file in the "objects" directory given the input, then checks if it exists
    removes the blob right after, returns true if blobbed and false if not
    this function DOES NOT update the index.
    if blob already exists returns an empty string.

resetBlob()
    resets BOTH the index file and the object directory (aka clears them out)

testingIndexing()
    creates 4 test files each with pre-written contents and a list of KNOWN SHA-1 codes.
    first creates all the blob files & updates index
    Then goes through each file
        1. checking if the blob file was creater
        2. checking if the index was updated properly (including both SHA-1 and file name)

deleteTestFiles()
    deletes all files in main git directory that were not initiated at it's creation

resetToMyStandardsAKABareMinimum()
    resets EVERYTHING (blobs, indexing, & deletes unecessary files in git folder)









