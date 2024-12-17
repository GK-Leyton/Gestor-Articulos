import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.example.segundoparcial.R
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.events.Event
import com.itextpdf.kernel.events.IEventHandler
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.property.HorizontalAlignment
import java.io.ByteArrayOutputStream
import java.io.OutputStream


class PDFGenerator {

    /**
     * Genera un PDF y permite al usuario guardar el archivo en la ubicación deseada.
     * @param context Contexto de la aplicación.
     * @param title Título del PDF.
     * @param columnHeaders Lista con los encabezados de las columnas.
     * @param tableData Lista de listas con los datos para la tabla.
     * @param savePDFLauncher Lanzador para la selección del archivo (ActivityResultLauncher).
     */
    fun createPDF(
        context: Context,
        title: String,
        columnHeaders: List<String>,
        tableData: List<List<String>>,
        savePDFLauncher: ActivityResultLauncher<String>
    ) {
        // Inicia el lanzador para seleccionar dónde guardar el PDF
        savePDFLauncher.launch("reporte.pdf")
    }

    /**
     * Crea el contenido del PDF y lo guarda en el flujo de salida proporcionado.
     * @param outputStream Flujo de salida donde se guarda el archivo.
     * @param title Título del PDF.
     * @param columnHeaders Encabezados de las columnas.
     * @param tableData Datos de la tabla.
     */
    fun savePDFToStream(
        context: Context,
        outputStream: OutputStream,
        title: String,
        columnHeaders: List<String>,
        tableData: List<List<String>>
    ) {
        try {
            val pdfWriter = PdfWriter(outputStream)
            val pdfDocument = PdfDocument(pdfWriter)

            // Create a custom page event handler
            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, object : IEventHandler {
                override fun handleEvent(event: Event) {
                    val docEvent = event as PdfDocumentEvent
                    val page = docEvent.page
                    val pdfCanvas = PdfCanvas(page)

                    // Recreate background image for each page
                    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.paisaje_10)
                    val byteArrayOutputStreamBackground = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStreamBackground)
                    val byteArray = byteArrayOutputStreamBackground.toByteArray()

                    // Create ImageData from byte array
                    val imageData = ImageDataFactory.create(byteArray)

                    // Create Image using ImageData
                    val backgroundImage = Image(imageData)

                    val pageSize = pdfDocument.defaultPageSize
                    val pageWidth = pageSize.width
                    val pageHeight = pageSize.height

                    val backgroundImageWidth = backgroundImage.imageWidth.toFloat()
                    val backgroundImageHeight = backgroundImage.imageHeight.toFloat()

                    val scaleFactorWidth = pageWidth / backgroundImageWidth
                    val scaleFactorHeight = pageHeight / backgroundImageHeight

                    val scaleFactor = maxOf(scaleFactorWidth, scaleFactorHeight)

                    // Scale the image
                    backgroundImage.scaleToFit(
                        backgroundImageWidth * scaleFactor,
                        backgroundImageHeight * scaleFactor
                    )
                    backgroundImage.setFixedPosition(
                        (pageWidth - backgroundImageWidth * scaleFactor) / 2f,
                        (pageHeight - backgroundImageHeight * scaleFactor) / 2f
                    )

                    //Toast.makeText(context, "backgroundImage " + backgroundImage.imageHeight, Toast.LENGTH_SHORT).show()
                    // Add the image to the page
                    pdfCanvas.addImage(imageData, 0f, 0f, 1100f, true, false)


                }
            })

            val document = Document(pdfDocument)

            // Create header table (logo and text)
            val headerTable = Table(2)
            val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_negro)
            val byteArrayOutputStream = ByteArrayOutputStream()
            logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val logoByteArray = byteArrayOutputStream.toByteArray()

            // Create logo image
            val logoImageData = ImageDataFactory.create(logoByteArray)
            val logo = Image(logoImageData)
            logo.setWidth(150f)
            logo.setHeight(100f)
            headerTable.addCell(Cell().add(logo).setBorder(null))

            val text = Paragraph("Las siete capas del conocimiento")
                .setFontSize(23f)
                .setTextAlignment(TextAlignment.LEFT)
                .setBold()
            headerTable.addCell(Cell().add(text).setBorder(null))

            headerTable.setWidth(100f)
            headerTable.setHorizontalAlignment(HorizontalAlignment.CENTER)

            // Add header table to first page before every other content
            document.add(headerTable)
            document.add(Paragraph("\n\n\n"))

            // Add title
            val titleParagraph = Paragraph(title)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20f)
                .setBold()
            document.add(titleParagraph)
            document.add(Paragraph("\n\n"))

            // Create main table
            val table = Table(5)

            // Add header cells
            columnHeaders.forEach { header ->
                table.addHeaderCell(
                    Cell().add(Paragraph(header))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setFontColor(ColorConstants.BLACK)
                        .setFontSize(20f)
                        .setBorder(SolidBorder(ColorConstants.BLACK, 2f))
                )
            }

            // Add table data
            tableData.forEach { rowData ->
                rowData.forEach { cellData ->
                    table.addCell(
                        Cell().add(Paragraph(cellData))
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBold()
                            .setFontColor(ColorConstants.BLACK)
                            .setFontSize(20f)
                            .setBorder(SolidBorder(ColorConstants.BLACK, 2f))
                    )
                }
            }

            table.setBorder(SolidBorder(ColorConstants.BLACK, 3f))
            table.setHorizontalAlignment(HorizontalAlignment.CENTER)

            // Add table to document
            document.add(table)

            // Close the document
            document.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }





    /**
     * Comparte el archivo PDF a través de otras aplicaciones.
     * @param context Contexto de la aplicación.
     * @param uri URI del archivo PDF a compartir.
     */
    fun sharePDF(context: Context, uri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Asegura que se pueda leer el archivo
        }
        context.startActivity(Intent.createChooser(shareIntent, "Compartir PDF"))
    }
}
