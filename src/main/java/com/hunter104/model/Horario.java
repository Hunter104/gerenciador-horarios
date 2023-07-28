package com.hunter104.model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record Horario(Set<DiadaSemana> dias, Set<Hora> horas, String horarioCodificado) {
    public Horario {
        if (dias.size() == 0) {
            throw new IllegalArgumentException("Nenhum dia selecionado para criação de horário");
        }
        if (horas.size() == 0) {
            throw new IllegalArgumentException("Nenhuma hora selecionada para criação de horário");
        }
    }

    public static Horario criarFromCodigo(String horarioCodificado) {
        Pattern pattern = Pattern.compile("^([2-7]+)([MNT])([1-5]+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(horarioCodificado);

        if (matcher.find()) {

            Set<DiadaSemana> dias = parseDias(matcher.group(1));

            Turno turno = parseTurno(matcher.group(2));
            Set<Hora> horas = parseHoras(matcher.group(3), turno);

            return new Horario(dias, horas, horarioCodificado);
        } else {
            throw new IllegalArgumentException("código de horário tem formato inválido");
        }
    }

    private static Set<Hora> parseHoras(String codigoHoras, Turno turno) {
        return Arrays.stream(Hora.values())
                .filter(horarios -> horarios.turno().equals(turno) && codigoHoras.contains(horarios.codigo()))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Hora.class)));
    }

    private static Set<DiadaSemana> parseDias(String codigoDias) {
        return Arrays.stream(DiadaSemana.values())
                .filter(turno -> codigoDias.contains(String.valueOf(turno.codigo())))
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(DiadaSemana.class)));
    }

    private static Turno parseTurno(String codigoTurno) {
        return Arrays.stream(Turno.values())
                .filter(turnoPossivel -> turnoPossivel.codigo().equals(codigoTurno))
                .findFirst()
                .orElseThrow();
    }

    public boolean temInterseccao(DiadaSemana dia, Hora hora) {
        return dias.contains(dia) && horas.contains(hora);
    }

    public boolean temInterseccao(Horario horario) {
        boolean diasIntercedem = conjuntosIntercedem(horario.dias(), dias);
        boolean horasIntercedem = conjuntosIntercedem(horario.horas(), horas);

        return diasIntercedem && horasIntercedem;
    }

    private <T extends Enum<T>> boolean conjuntosIntercedem(Collection<T> primeiroConjunto, Collection<T> segundoConjunto) {
        Set<T> conjuntosIntercedentes = EnumSet.copyOf(primeiroConjunto);
        conjuntosIntercedentes.retainAll(segundoConjunto);
        return conjuntosIntercedentes.size() > 0;
    }
}
