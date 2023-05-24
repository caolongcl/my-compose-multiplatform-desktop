import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.graphics.withSave
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.loadXmlImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.xml.sax.InputSource

@Composable
@Preview
fun App() {
//    var text by remember { mutableStateOf("Hello, World!") }
    var count by remember { mutableStateOf(0) }

    val density =
        LocalDensity.current // to calculate the intrinsic size of vector images (SVG, XML)

    val sample = remember {
        useResource("sample.png", ::loadImageBitmap)
    }
    val ideaLogo = remember {
        useResource("idea-logo.svg") { loadSvgPainter(it, density) }
    }
    val composeLogo = rememberVectorPainter(
        remember {
            useResource("compose-logo.xml") { loadXmlImageVector(InputSource(it), density) }
        }
    )

    MaterialTheme {
        Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    ++count
                }) {
                Text(if (count == 0) "Hello, Desktop!" else "Clicked ${count}!")
            }
            Button(modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    count = 0;
                }) {
                Text("Reset")
            }
            Image(
                painter = painterResource("car1.jpeg"),
                contentDescription = "sample",
                modifier = Modifier.wrapContentSize()
            )

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawIntoCanvas { canvas ->
                    canvas.withSave {
                        canvas.drawImage(sample, Offset.Zero, Paint())
                        canvas.translate(sample.width.toFloat(), 0f)
                        with(ideaLogo) {
                            draw(ideaLogo.intrinsicSize)
                        }
                        canvas.translate(ideaLogo.intrinsicSize.width, 0f)
                        with(composeLogo) {
                            draw(Size(100f, 100f))
                        }
                    }
                }
            }
        }
    }
}

fun main() = application {
    val icon = painterResource("icon.png")
    Tray(
        icon = icon,
        menu = {
            Item("Quit App", onClick = ::exitApplication)
        }
    )
    Window(
        onCloseRequest = ::exitApplication,
//        icon = painterResource("car1.jpeg")
        icon = BitmapPainter(useResource("icon.png", ::loadImageBitmap))
    ) {
        App()
    }
}
