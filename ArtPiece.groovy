import eu.mihosoft.vrl.v3d.*

//def name = "mechEng"
//def name = "boynton"
//def name = "pandemonium"
def name = "trotting"
ArrayList<Object> desc_params = new ArrayList<Object>();
desc_params.add(name) //add 
ArrayList<Object> borders_params = new ArrayList<Object>()
borders_params.add(name)

def border_width = 7
def border_thickness = 4


println "Clearing the Vitamins cache to make sure current geometry is being used (only run this operation when the STL has changed)"
Vitamins.clear()

// Load an STL file from a git repo
// Loading a local file also works here
File pieceSTL
println "Loading piece STL from repo based on piece name: "+name
switch (name) {
    case "mechEng":
		pieceSTL = ScriptingEngine.fileFromGit(
			"https://github.com/JansenSmith/ArtPiece.git",
			"source_stl/WorcesterFreeInstitute_mechEng_Front_250x141.stl");
        break
    case "boynton":
        pieceSTL = ScriptingEngine.fileFromGit(
			"https://github.com/JansenSmith/ArtPiece.git",
			"source_stl/WorcesterFreeInstitute_BoyntonHall_Front_250x147.stl");
        break
    case "pandemonium":
		pieceSTL = ScriptingEngine.fileFromGit(
			"https://github.com/JansenSmith/ArtPiece.git",
			"source_stl/Pandemonium_Front_236x156.stl");
        break
    default:
        println "Unknown option: $name"
        break
}


// Load the .CSG from the disk and cache it in memory
CSG piece  = Vitamins.get(pieceSTL);
println "The original piece STL is "+piece.totalZ+"mm in Z thickness"

CSG desc
switch(name) {
	case ["mechEng", "boynton"]:
		println "Loading description CSG via factory"
		desc =  (CSG)ScriptingEngine.gitScriptRun(
		                                "https://github.com/JansenSmith/ArtText.git", // git location of the library
			                              "ArtText.groovy" , // file to load
			                              desc_params // send the factory the name param
		                        )
		break
	case "pandemonium":
    	break
	default:
        println "Unknown option: $name"
        break
}

println "Loading signature CSG via factory"
CSG sig =  (CSG)ScriptingEngine.gitScriptRun(
                                "https://github.com/JansenSmith/JMS.git", // git location of the library
	                              "JMS.groovy" , // file to load
	                              null// no parameters (see next tutorial)
                        )


println "Moving piece into position"
piece = piece.toXMin().toYMin().toZMin()

borders_params.add(piece.totalX)
borders_params.add(piece.totalY)
borders_params.add(border_width)
borders_params.add(border_thickness)
CSG borders
switch(name) {
	case ["mechEng", "boynton"]:
		break
	case "pandemonium":
		println "Loading borders CSG via factory"
		borders =  (CSG)ScriptingEngine.gitScriptRun(
										"https://github.com/JansenSmith/ArtBorders.git", // git location of the library
										  "ArtBorders.groovy" , // file to load
										  borders_params // send the factory the name param
								)
		piece = piece.dumbUnion(borders)
		piece = piece.toXMin().toYMin()
		break
	default:
		println "Unknown option: $name"
		break
}

switch(name) {
	case ["mechEng", "boynton"]:
		println "Moving description into position"
		desc = desc.toZMin()
		desc = desc.mirrorx().movex(piece.totalX)
		break
	case "pandemonium":
		break
	default:
		println "Unknown option: $name"
		break
}


println "Moving signature into position"
switch(name) {
	case ["mechEng", "boynton"]:
		sig = sig.toZMin().movex(piece.totalX)
		sig = sig.mirrorx().movex(piece.totalX)
		break
	case "pandemonium":
		sig = sig.toZMin().movex(piece.totalX).movex(-5)
		break
	default:
		println "Unknown option: $name"
		break
}


CSG addenda
switch(name) {
	case ["mechEng", "boynton"]:
		println "Combine description and signature geometries"
		addenda = sig.union(desc)
		break
	case "pandemonium":
		addenda = sig
		break
	default:
		println "Unknown option: $name"
		break
}

//println "Creating a base that contains the sig (debug)"
//def solid_space = 0.08
//def base = new Cube(piece.totalX,piece.totalY,addenda.totalZ + solid_space).toCSG()
//				.toXMin().toYMin().toZMin()
//base = base.difference(sig)//.movez(solid_space))
//println "The base is "+base.totalZ+"mm in height"

println "Creating a base that contains the addenda"
def solid_space = 0.08
def base = new Cube(piece.totalX,piece.totalY,addenda.totalZ + solid_space).toCSG()
				.toXMin().toYMin().toZMin()
base = base.difference(addenda)//.movez(solid_space))
println "The base is "+base.totalZ+"mm in Z thickness"

println "Adding the base to the piece"
piece = piece.dumbUnion(base.toZMax())
				.toZMin()
println "The resultant piece is "+piece.totalX+"mm in X width, "+piece.totalY+"mm in Y height, "+piece.totalZ+"mm in Z thickness, "

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

if (desc) {
	desc = desc.setColor(javafx.scene.paint.Color.DARKRED)
				.setName(name+"_desc")
				.addAssemblyStep(0, new Transform())
				.setManufacturing({ toMfg ->
					return toMfg
							//.rotx(180)// fix the orientation
							//.toZMin()//move it down to the flat surface
				})
}

sig = sig.setColor(javafx.scene.paint.Color.DARKRED)
			.setName(name+"_sig")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

addenda = addenda.setColor(javafx.scene.paint.Color.DARKRED)
			.setName(name+"_addenda")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})
			
base = base.setColor(javafx.scene.paint.Color.DARKGRAY)
			.setName(name+"_base")
			.addAssemblyStep(0, new Transform())
			.setManufacturing({ toMfg ->
				return toMfg
						//.rotx(180)// fix the orientation
						//.toZMin()//move it down to the flat surface
			})

def ret = [piece, addenda] // options: 

println "Done!"

return ret

