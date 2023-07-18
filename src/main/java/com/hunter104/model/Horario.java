package com.hunter104.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Horario {
    private Set<Hora> horas;
    private Set<DiadaSemana> dias;

    public Horario(String horarioCodificado) {

        Pattern pattern = Pattern.compile("(\\d+)(\\w)(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(horarioCodificado);

        if (matcher.find()) {

            String codigoDias = matcher.group(1);
            String codigoTurno = matcher.group(2);
            String codigoHorario = matcher.group(3);

            dias = Arrays.stream(DiadaSemana.values())
                    .filter(turno -> codigoDias.contains(String.valueOf(turno.getCodigo())))
                    .collect(Collectors.toSet());

            Turno turnoAtual = Arrays.stream(Turno.values())
                    .filter(turnoPossivel -> turnoPossivel.getCodigo().equals(codigoTurno))
                    .findFirst()
                    .orElseThrow();

            horas = Arrays.stream(Hora.values())
                    .filter(horarios -> horarios.getTurno().equals(turnoAtual) && codigoHorario.contains(horarios.getCodigo()))
                    .collect(Collectors.toSet());
        }
    }

    public Horario(Set<DiadaSemana> dias, Set<Hora> horas) {
        this.dias = dias;
        this.horas = horas;
    }

    public boolean temInterseccao(DiadaSemana dia, Hora hora) {
        return dias.contains(dia) && horas.contains(hora);
    }

    @Override
    public String toString() {
        return "Horario{" +
                "horas=" + horas +
                ", dias=" + dias +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horario horario = (Horario) o;
        return Objects.equals(horas, horario.horas) && Objects.equals(dias, horario.dias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(horas, dias);
    }

    public Set<Hora> getHoras() {
        return horas;
    }

    public Set<DiadaSemana> getDias() {
        return dias;
    }
}
