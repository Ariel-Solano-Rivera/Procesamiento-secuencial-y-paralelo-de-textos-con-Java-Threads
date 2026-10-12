# Actividad Práctica 01: Procesamiento secuencial y paralelo de textos

## Descripción

Programa en Java que analiza el libro *Don Quijote de la Mancha* en formato TXT. El mismo texto se procesa de dos formas: primero de manera secuencial y después con 2 y 4 hilos usando directamente la clase `Thread`.

El programa obtiene:

1. Total de palabras del documento.
2. Apariciones de la palabra `Quijote`.
3. Palabras que contienen la secuencia `ción`.


## Archivos del proyecto

```text
ActividadParalela/
├── datos/
│   └── donquijote.txt
├── informe/
│   ├── index.html
├── src/
│   ├── ProcesadorSecuencial.java
│   └── ProcesadorParalelo.java
└── README.md
```

## Requisitos

- JDK 21 o una versión compatible de Java.
- El archivo `datos/donquijote.txt` debe estar presente.

## Compilación y ejecución

Abrir PowerShell en la carpeta raíz del proyecto y ejecutar:

```powershell
javac -encoding UTF-8 src\ProcesadorSecuencial.java src\ProcesadorParalelo.java
java -cp src ProcesadorParalelo
```

El programa muestra los conteos del texto y una tabla con el tiempo promedio y la aceleración para 1, 2 y 4 hilos.

## Cómo funciona

### Procesamiento secuencial

`ProcesadorSecuencial.java` recorre el texto carácter por carácter. Considera una palabra como una secuencia de letras, por lo que ignora espacios y signos de puntuación. Esta versión usa un único flujo de ejecución y devuelve los tres conteos en el arreglo `long[]`.

### Procesamiento paralelo

`ProcesadorParalelo.java` divide el texto en bloques. Si un bloque termina en medio de una palabra, su final avanza hasta completar esa palabra. Después crea un objeto `HiloProcesador` para cada bloque. Cada hilo guarda sus conteos en un arreglo `long[]` y, al final, el programa suma las tres posiciones de todos los arreglos.

- `start()` inicia los hilos.
- `join()` espera a que todos terminen.
- Al finalizar, se suman los resultados de cada hilo.

## Resultados obtenidos

| Documento | Hilos | Tiempo promedio (ms) | Aceleración |
|---|---:|---:|---:|
| Original | 1 (secuencial) | 28.642 | 1.00 |
| Original | 2 | 21.826 | 1.31 |
| Original | 4 | 12.738 | 2.25 |
| Ampliado ×100 | 1 (secuencial) | 3150.305 | 1.00 |
| Ampliado ×100 | 2 | 1938.659 | 1.62 |
| Ampliado ×100 | 4 | 949.345 | 3.32 |

Los tiempos pueden variar según el equipo y las tareas que estén ejecutándose en el sistema.

## Equipo utilizado

- Procesador: AMD Ryzen 7 7730U with Radeon Graphics.
- Núcleos físicos: 8.
- Procesadores lógicos: 16.
- Memoria RAM: 15,4 GB.
- Sistema operativo: Windows.


