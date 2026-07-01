package ReproductorNuevo;

import java.io.File;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;


public class LectorMP3 {
    public Cancion leer(File archivo) {
        Cancion cancion = null;

        try {
            Mp3File mp3file = new Mp3File(archivo);
        
            String titulo = "";
            String artista = "";
            String album = "";
            String year = null;
            int anio = 1900;

            if(mp3file.hasId3v2Tag()) {
                ID3v2 tag = mp3file.getId3v2Tag();
                titulo = tag.getTitle();
                artista = tag.getArtist();
                album = tag.getAlbum();
                year = tag.getYear();
            }
            else if(mp3file.hasId3v1Tag()) {
                ID3v1 tag = mp3file.getId3v1Tag();

                titulo = tag.getTitle();
                artista = tag.getArtist();
                album = tag.getAlbum();
                year = tag.getYear();
            }

            //Elimina el carater '?' de Artista y Titulo, que aparece en algunos archivos MP3 es un caracter invisible BOM 
            if (titulo != null && titulo.startsWith("\uFEFF")) {
                titulo = titulo.substring(1);
            }
            if (artista != null && artista.startsWith("\uFEFF")) {
                artista = artista.substring(1);
            }

            // Si no se pudo obtener el título o el artista, se asignan valores predeterminados
            // Esto es útil para evitar problemas al mostrar la información de la canción en la interfaz gráfica
            if(titulo == null || titulo.trim().isEmpty()) {
                titulo = archivo.getName();
            }

            if(artista == null || artista.trim().isEmpty()) {
                artista = "Desconocido";
            }

            if(album == null || album.trim().isEmpty()) {
                album = "Desconocido";
            }

            if(year != null && !year.trim().isEmpty()) {
                //quita espacios en blanco al inicio y fin
                year = year.trim();
                // 2. Quitamos el carácter invisible BOM si lo tiene
                if (year.startsWith("\uFEFF")) {
                    year = year.substring(1);
                }
                if (year.length() >= 4) {
                year = year.substring(0, 4);
                }
                try {
                    anio = Integer.parseInt(year);
                }
                catch(NumberFormatException e) {
                    System.out.println("Error al convertir el año: " + e.getMessage());
                }
            }
            long duracion = mp3file.getLengthInSeconds();

            cancion = new Cancion(titulo, artista, album, archivo.getAbsolutePath(), anio, duracion);
        }
        catch(Exception e) {
            System.out.println("Error al leer el archivo MP3: " + e.getMessage());
        }
        return cancion;
    }

}
