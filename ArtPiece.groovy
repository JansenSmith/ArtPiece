
def piece = "mechEng"

// Load an STL file from a git repo
// Loading a local file also works here
File pieceSTL = ScriptingEngine.fileFromGit(
	"https://github.com/JansenSmith/ArtPiece.git",
	"null");
// Load the .CSG from the disk and cache it in memory
CSG servo  = Vitamins.get(servoFile);

CSG desc =  (CSG)ScriptingEngine.gitScriptRun(
                                "https://github.com/JansenSmith/ArtText.git", // git location of the library
	                              "ArtText.groovy" , // file to load
	                              null// no parameters (see next tutorial)
                        )
println "Loading description CSG via factory"


CSG sig =  (CSG)ScriptingEngine.gitScriptRun(
                                "https://github.com/JansenSmith/JMS.git", // git location of the library
	                              "JMS.groovy" , // file to load
	                              null// no parameters (see next tutorial)
                        )
println "Loading signature CSG via factory"

return [desc, sig]

//CSG pieceSTL = 