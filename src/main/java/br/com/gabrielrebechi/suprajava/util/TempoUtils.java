package br.com.gabrielrebechi.suprajava.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TempoUtils {

    private static final DateTimeFormatter FORMATADOR_PADRAO = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Converte string (como "2", "2:30", "2:30:15") para decimal (ex: 2.5)
    public static BigDecimal tempoParaDecimal(String tempoStr) {
        LocalTime tempo = LocalTime.parse(normalizar(tempoStr));
        int horas = tempo.getHour();
        int minutos = tempo.getMinute();
        int segundos = tempo.getSecond();

        BigDecimal totalMinutos = BigDecimal.valueOf(horas * 60 + minutos + segundos / 60.0);
        return totalMinutos.divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);
    }

    // Converte decimal (ex: 2.5) para string no formato "HH:mm:ss"
    public static String decimalParaTempo(BigDecimal decimal) {
        int horas = decimal.intValue();
        BigDecimal minutosFracionarios = decimal.subtract(BigDecimal.valueOf(horas)).multiply(BigDecimal.valueOf(60));
        int minutos = minutosFracionarios.intValue();
        int segundos = minutosFracionarios.subtract(BigDecimal.valueOf(minutos))
                .multiply(BigDecimal.valueOf(60))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();

        LocalTime tempo = LocalTime.of(horas % 24, minutos % 60, segundos % 60);
        return tempo.format(FORMATADOR_PADRAO);
    }

    // Garante que o tempo tenha formato completo "HH:mm:ss"
    private static String normalizar(String tempoStr) {
        String[] partes = tempoStr.split(":");
        if (partes.length == 1) return tempoStr + ":00:00";
        if (partes.length == 2) return tempoStr + ":00";
        return tempoStr;
    }
}

