import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class ProcesadorParalelo {

    static final String PALABRA_BUSCADA = "Quijote";
    static final String SECUENCIA_BUSCADA = "ción";
    static final int REPETICIONES = 5;

    static class HiloProcesador extends Thread {
        String texto;
        int inicio;
        int fin;
        long[] resultado;

        HiloProcesador(String texto, int inicio, int fin) {
            this.texto = texto;
            this.inicio = inicio;
            this.fin = fin;
        }

        @Override
        public void run() {
            resultado = ProcesadorSecuencial.procesarRango(
                texto, inicio, fin, PALABRA_BUSCADA, SECUENCIA_BUSCADA
            );
        }
    }

    static long[] procesarParalelo(String texto, int cantidadHilos)
            throws InterruptedException {

        HiloProcesador[] hilos = new HiloProcesador[cantidadHilos];
        int inicio = 0;

        for (int numeroHilo = 0; numeroHilo < cantidadHilos; numeroHilo++) {
            int fin = (int) ((long) texto.length() * (numeroHilo + 1)
                            / cantidadHilos);

            while (fin < texto.length() && Character.isLetter(texto.charAt(fin))) {
                fin++;
            }

            hilos[numeroHilo] = new HiloProcesador(texto, inicio, fin);
            inicio = fin;
        }

        for (HiloProcesador hilo : hilos) {
            hilo.start();
        }

        for (HiloProcesador hilo : hilos) {
            hilo.join();
        }

        long[] resultadoFinal = new long[3];

        for (HiloProcesador hilo : hilos) {
            resultadoFinal[0] += hilo.resultado[0];
            resultadoFinal[1] += hilo.resultado[1];
            resultadoFinal[2] += hilo.resultado[2];
        }

        return resultadoFinal;
    }

    static long[] ejecutar(String texto, int cantidadHilos)
            throws InterruptedException {
        if (cantidadHilos == 1) {
            return ProcesadorSecuencial.procesar(texto, PALABRA_BUSCADA,
                                                 SECUENCIA_BUSCADA);
        }
        return procesarParalelo(texto, cantidadHilos);
    }

    static double medir(String texto, int cantidadHilos)
            throws InterruptedException {
        long sumaTiempos = 0;

        for (int repeticion = 0; repeticion < REPETICIONES; repeticion++) {
            long inicioTiempo = System.nanoTime();
            ejecutar(texto, cantidadHilos);
            sumaTiempos += System.nanoTime() - inicioTiempo;
        }

        return sumaTiempos / (double) REPETICIONES / 1_000_000;
    }

    static void probarDocumento(String nombre, String texto)
            throws InterruptedException {
        long[] resultado = ejecutar(texto, 1);

        System.out.println("\nDOCUMENTO: " + nombre);
        System.out.println("Total de palabras: " + resultado[0]);
        System.out.println("Apariciones de Quijote: " + resultado[1]);
        System.out.println("Palabras con ción: " + resultado[2]);

        double tiempoUnoHilo = medir(texto, 1);
        double tiempoDosHilos = medir(texto, 2);
        double tiempoCuatroHilos = medir(texto, 4);

        System.out.println("\nHilos | Tiempo promedio (ms) | Speedup");
        System.out.printf(Locale.US, "1     | %.3f | 1.00%n", tiempoUnoHilo);
        System.out.printf(Locale.US, "2     | %.3f | %.2f%n", tiempoDosHilos,
                          tiempoUnoHilo / tiempoDosHilos);
        System.out.printf(Locale.US, "4     | %.3f | %.2f%n", tiempoCuatroHilos,
                          tiempoUnoHilo / tiempoCuatroHilos);
    }

    public static void main(String[] argumentos) throws Exception {
        String textoOriginal = Files.readString(Path.of("datos", "donquijote.txt"));
        String textoAmpliado = (textoOriginal + "\n").repeat(100);

        probarDocumento("ORIGINAL", textoOriginal);
        probarDocumento("AMPLIADO X100", textoAmpliado);
    }
}
