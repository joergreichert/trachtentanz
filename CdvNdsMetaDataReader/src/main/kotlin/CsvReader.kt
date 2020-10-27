import com.fasterxml.jackson.module.kotlin.*
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.File
import java.io.Reader
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

fun readAll(reader: Reader): List<Array<String?>?>? {
    val parser = CSVParserBuilder()
        .withSeparator(';')
        .build()
    return CSVReaderBuilder(reader)
        .withSkipLines(1)
        .withCSVParser(parser)
        .build().use {
            it.readAll()
        }
}

data class PhotoDesc(
    val num1: String?,
    val num2: String?,
    val bez: String?,
    val titel: String?,
    val von: String?,
    val bis: String?,
    val beschr: String?
)

fun main() {
    val file = File("D:/CDVNDL/Uniformierte_Zivilisten/Metadaten/Ziviluniformen_Braunschweigisches_Landesmuseum.csv")
    println(file.exists())
    val reader: Reader = Files.newBufferedReader(Paths.get(file.toURI()), StandardCharsets.ISO_8859_1)
    val content = readAll(reader) ?: throw IllegalArgumentException()
    val descs = mutableListOf<PhotoDesc>()
    for (entry in content.map { it }) {
        entry?.map {
            descs.add(PhotoDesc(
                entry[0],
                entry[1],
                entry[2],
                entry[3],
                entry[4],
                entry[5],
                entry[6]
            ))
        }
    }
    val objectMapper = jacksonObjectMapper()
    objectMapper.writeValue(File("D:/data.json"), descs)
}