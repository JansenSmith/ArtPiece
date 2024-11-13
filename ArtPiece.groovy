import eu.mihosoft.vrl.v3d.*

def name = "mechEng"

//if (name.equals("mechEng")){
//	println "YES"
//} else{
//	println "NO"
//}
//return name

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
//return desc

println "Loading signature CSG via factory"
CSG sig =  (CSG)ScriptingEngine.gitScriptRun(
                                "https://github.com/JansenSmith/JMS.git", // git location of the library
	                              "JMS.groovy" , // file to load
	                              null// no parameters (see next tutorial)
                        )


println "Moving piece into position"
piece = piece.toXMin().toYMin().toZMin()

println "Moving description into position"
desc = desc.mirrorx().movex(piece.totalX)

println "Moving signature into position"
sig = sig.mirrorx()

println "Combine description and signature geometries"
CSG combin = sig.union(desc)

//println "Removing description and signature geometries from the piece"
//piece = piece.difference(combin)
//piece = piece.difference(sig)

println "Done!"
piece = piece.setColor(javafx.scene.paint.Color.DARKGRAY)
			.setName(name+"_piece")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

desc = desc.setColor(javafx.scene.paint.Color.DARKRED)
			.setName(name+"_desc")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

sig = sig.setColor(javafx.scene.paint.Color.DARKRED)
			.setName(name+"_sig")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

combin = combin.setColor(javafx.scene.paint.Color.DARKRED)
			.setName(name+"_addenda")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

def ret = combin

return ret

