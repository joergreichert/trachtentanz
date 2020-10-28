import au.edu.apsr.mtk.base.Div
import au.edu.apsr.mtk.base.METS
import au.edu.apsr.mtk.base.METSWrapper
import au.edu.apsr.mtk.base.StructMap
import au.edu.apsr.mtk.ch.METSReader
import java.io.File
import java.io.FileInputStream
import java.net.URL


fun main() {
    val folder = File("D:\\CDVNDL\\Historische_Kleidung_und_Trachten\\Metadaten")
    folder.walkTopDown().filter { it.isFile }.forEach { file ->
        FileInputStream(file).use {
            val mr = METSReader()
            mr.mapToDOM(it)

            val mw = METSWrapper(mr.metsDocument)
            //mw.validate()

            val mets = mw.metsObject

            println("Package Type of ${mets.type} using profile: ${mets.profile}")

            mets.metsHdr.apply {
                println("Package create date: $createDate")
                println("Package last modified date: $lastModDate")
                for (agent in agents) {
                    println("Agent ${agent.name} has role ${agent.role}")
                }

                mets.fileSec.apply {
                    for (fg in fileGrps) {
                        println("FileGrp of use ${fg.use}")
                        for (f in fg.files) {
                            for (loc in f.fLocats) {
                                val url = URL(loc.href)
                                java.io.FileOutputStream("D:/${file.name.replace(".xml", ".jpg")}").use { fos ->
                                    fos.write(url.readBytes())
                                }
                            }
                        }
                    }
                }

                val sms: List<StructMap> = mets.structMaps

                println("Package has " + sms.size + " structMap(s)")

                // let's look at the first StructMap

                // let's look at the first StructMap
                val sm = sms[0]
                showDivInfo(mets, sm.divs)
            }
        }
    }
}

fun showDivInfo(mets: METS, divs: List<Div>) {
    for (div in divs) {
        println("Div type of ${div.type} with DMDID ${div.dmdID} contains metadata of type ${mets.getDmdSec(div.dmdID).mdType}")
        showDivInfo(mets, div.divs);
    }
}
