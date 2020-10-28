import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

fun main() {
    val folder = File("D:\\CDVNDL\\Instrumente_der_Experimentalphysik_Georg_Christoph_Lichtenbergs")
    folder.walkTopDown().filter { it.isFile }.forEach { xmlFile ->

        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))
        val doc = dBuilder.parse(xmlInput)

        val xpFactory = XPathFactory.newInstance()
        val xPath = xpFactory.newXPath()

        //<item type="T1" count="1">Value1</item>
        val xpath =
            "/lido/administrativeMetadata/resourceWrap/resourceSet/resourceRepresentation/linkResource/text()"

        val itemsTypeT1 = xPath.evaluate(xpath, doc, XPathConstants.NODESET) as NodeList

        for (i in 0..itemsTypeT1.length - 1) {
            val loc = itemsTypeT1.item(i).nodeValue
            if (loc.endsWith("/full/full/0/default.jpg")) {
                val url = URL(loc)
                val fileName = xmlFile.name.replace("xml", "jpg")
                try {
                    java.io.FileOutputStream("D:/${fileName}").use { fos ->
                        fos.write(url.readBytes())
                    }
                } catch(e: Exception) {
                    println(e)
                }
            }
        }
    }
}