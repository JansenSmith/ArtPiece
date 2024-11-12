
def name = "mechEng"


println "Loading piece STL from repo"
// Load an STL file from a git repo
// Loading a local file also works here
File pieceSTL = ScriptingEngine.fileFromGit(
	"https://github.com/JansenSmith/ArtPiece.git",
	"source_stl/WorcesterFreeInstitute_MechanicalEngineers_zoomed_Front_250x141.stl");
// Load the .CSG from the disk and cache it in memory
CSG piece  = Vitamins.get(pieceSTL);

println "Loading description CSG via factory"
CSG desc =  (CSG)ScriptingEngine.gitScriptRun(
                                "https://github.com/JansenSmith/ArtText.git", // git location of the library
	                              "ArtText.groovy" , // file to load
	                              null// no parameters (see next tutorial)
                        )


println "Loading signature CSG via factory"
CSG sig =  (CSG)ScriptingEngine.gitScriptRun(
                                "https://github.com/JansenSmith/JMS.git", // git location of the library
	                              "JMS.groovy" , // file to load
	                              null// no parameters (see next tutorial)
                        )

return [piece, desc, sig]

//CSG pieceSTL = 