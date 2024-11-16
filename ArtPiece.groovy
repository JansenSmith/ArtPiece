import eu.mihosoft.vrl.v3d.*

//def name = "mechEng"
def name = "boynton"


// Load an STL file from a git repo
// Loading a local file also works here
File pieceSTL
println "Loading piece STL from repo based on piece name: "+name
switch (name) {
    case "mechEng":
		pieceSTL = ScriptingEngine.fileFromGit(
			"https://github.com/JansenSmith/ArtPiece.git",
			"source_stl/WorcesterFreeInstitute_MechanicalEngineers_zoomed_Front_250x141.stl");
        break
    case "boynton":
        pieceSTL = ScriptingEngine.fileFromGit(
			"https://github.com/JansenSmith/ArtPiece.git",
			"source_stl/WorcesterFreeInstitute_BoyntonHall_Front_250x147.stl");
        break
    default:
        println "Unknown option: $name"
        break
}


// Load the .CSG from the disk and cache it in memory
CSG piece  = Vitamins.get(pieceSTL);
println "The original piece STL is "+piece.totalZ+"mm in height"

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
desc = desc.toZMin()
desc = desc.mirrorx().movex(piece.totalX)

println "Moving signature into position"
sig = sig.toZMin().movex(piece.totalX)
sig = sig.mirrorx().movex(piece.totalX)

println "Combine description and signature geometries"
CSG combin = sig.dumbUnion(desc)

println "Creating a base that contains the texts"
def solid_space = 0.08
def base = new Cube(piece.totalX,piece.totalY,combin.totalZ + solid_space).toCSG()
				.toXMin().toYMin().toZMin()
base = base.difference(combin)//.movez(solid_space))
println "The base is "+base.totalZ+"mm in height"

println "Adding the base to the piece"
piece = piece.dumbUnion(base.toZMax())
				.toZMin()
println "The resultant piece is "+piece.totalZ+"mm in height"

//println "Removing description and signature geometries from the piece"
//piece = piece.difference(combin)
//piece = piece.difference(sig)

println "Setting CSG attributes"
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
			
base = base.setColor(javafx.scene.paint.Color.DARKGRAY)
			.setName(name+"_base_dumbunion")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

def ret = [combin]

println "Done!"

return ret

