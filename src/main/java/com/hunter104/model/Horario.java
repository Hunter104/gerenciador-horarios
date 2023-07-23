package com.hunter104.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record Horario(Set<DiadaSemana> dias, Set<Hora> horas, String horarioCodificado) {
    public static Horario criarFromCodigo(String horarioCodificado) {
        Pattern pattern = Pattern.compile("^([2-7]+)([MNT])([1-5]+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(horarioCodificado);
        if (matcher.find()) {

            String codigoDias = matcher.group(1);
            String codigoTurno = matcher.group(2);
            String codigoHorario = matcher.group(3);

            Set<DiadaSemana> dias = Arrays.stream(DiadaSemana.values())
                    .filter(turno -> codigoDias.contains(String.valueOf(turno.getCodigo())))
                    .collect(Collectors.toSet());

            Turno turnoAtual = Arrays.stream(Turno.values())
                    .filter(turnoPossivel -> turnoPossivel.getCodigo().equals(codigoTurno))
                    .findFirst()
                    .orElseThrow();

            Set<Hora> horas = Arrays.stream(Hora.values())
                    .filter(horarios -> horarios.getTurno().equals(turnoAtual) && codigoHorario.contains(horarios.getCodigo()))
                    .collect(Collectors.toSet());

            boolean nenhumDia = dias.size() == 0;
            boolean nenhumaHora = horas.size() == 0;

            if (nenhumDia || nenhumaHora) {
                throw new IllegalArgumentException("código de horário tem formato inválido");
            }
            return new Horario(dias, horas, horarioCodificado);
        } else {
            throw new IllegalArgumentException("código de horário tem formato inválido");
        }
    }
    public boolean temInterseccao(DiadaSemana dia, Hora hora) {
        return dias.contains(dia) && horas.contains(hora);
    }

    public boolean temInterseccao(Horario horario) {
        Set<DiadaSemana> diasIntercedentes = new HashSet<>(horario.dias());
        Set<Hora> horasIntercedentes = new HashSet<>(horario.horas());
        diasIntercedentes.retainAll(this.dias);
        horasIntercedentes.retainAll(this.horas);
        return diasIntercedentes.size() > 0 && horasIntercedentes.size() > 0;
    }
}
