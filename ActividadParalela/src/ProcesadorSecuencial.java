public class ProcesadorSecuencial {

    public static long[] procesar(String texto, String palabraBuscada,
                                  String secuenciaBuscada) {
        return procesarRango(texto, 0, texto.length(), palabraBuscada,
                             secuenciaBuscada);
    }

    public static long[] procesarRango(String texto, int inicio, int fin,
                                       String palabraBuscada,
                                       String secuenciaBuscada) {
        long totalPalabras = 0;
        long aparicionesQuijote = 0;
        long palabrasConCion = 0;
        int posicion = inicio;

        while (posicion < fin) {
            while (posicion < fin && !Character.isLetter(texto.charAt(posicion))) {
                posicion++;
            }

            int inicioPalabra = posicion;

            while (posicion < fin && Character.isLetter(texto.charAt(posicion))) {
                posicion++;
            }

            if (inicioPalabra == posicion) {
                continue;
            }

            totalPalabras++;
            int longitudPalabra = posicion - inicioPalabra;

            if (longitudPalabra == palabraBuscada.length() &&
                texto.regionMatches(true, inicioPalabra, palabraBuscada, 0,
                                    palabraBuscada.length())) {
                aparicionesQuijote++;
            }

            for (int indice = inicioPalabra;
                 indice <= posicion - secuenciaBuscada.length(); indice++) {
                if (texto.regionMatches(true, indice, secuenciaBuscada, 0,
                                       secuenciaBuscada.length())) {
                    palabrasConCion++;
                    break;
                }
            }
        }

        return new long[] {totalPalabras, aparicionesQuijote, palabrasConCion};
    }
}
