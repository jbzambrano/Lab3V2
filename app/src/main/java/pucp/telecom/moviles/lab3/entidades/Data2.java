package pucp.telecom.moviles.lab3.entidades;

public class Data2 {


    private double longitud;
    private double latitud;
    private double[] medicion;

    public Data2(double longitud, double latitud, double[] medicion, int tiempo) {
        this.longitud = longitud;
        this.latitud = latitud;
        this.medicion = medicion;
        this.tiempo = tiempo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    private int tiempo;

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }


    public double[] getMedicion() {
        return medicion;
    }

    public void setMedicion(double[] medicion) {
        this.medicion = medicion;

    }
}
