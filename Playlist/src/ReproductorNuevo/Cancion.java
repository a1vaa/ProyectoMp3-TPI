package ReproductorNuevo;

public class Cancion {
    private String titulo;
    private String artista;
    private String album;
    private String ruta;
    private int anio;
    private long duracionSegundos;

    public Cancion(String titulo, String artista, String album, String ruta, int anio, long duracion) {
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.ruta = ruta;
        this.anio = anio;
        this.duracionSegundos = duracion;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getArtista() {
        return this.artista;
    }

    public String getRuta() {
        return this.ruta;
    }

    public int getAnio() {
        return this.anio;
    }

    public String getAlbum() {
        return this.album;
    }

    public long getDuracion() {
        return this.duracionSegundos;
    }
    
    public String toString() {
        return this.titulo + " | " + this.artista + " | " + this.anio;
    }

    public boolean equals(Cancion c){
        return this.titulo==c.getTitulo()&&this.artista==c.getArtista();
    }
}
