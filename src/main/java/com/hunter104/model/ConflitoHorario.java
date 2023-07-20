package com.hunter104.model;

import java.util.*;
import java.util.stream.Collectors;

public class ConflitoHorario {
    private final Hora hora;
    private final DiadaSemana dia;
    private final Map<Disciplina, Integer> turmas;
    private final boolean otimizavel;
    private final boolean impossivel;

    private ConflitoHorario(DiadaSemana dia, Hora hora, Map<Disciplina, Integer> turmas) {
        this.hora = hora;
        this.dia = dia;
        this.turmas = turmas;

        List<Disciplina> disciplinasHorarioUnico = filtrarDisciplinasHorarioUnico();
        /* Só é possível remover turmas, quando apenas uma disciplina de horário único estiver em conflito,
        se mais de um horário único estiver em conflito, a grade é inválida.
        Se menos de um horário único estiver em conflito, é possível escolher outra turma
         */
        this.otimizavel = disciplinasHorarioUnico.size() == 1;
        this.impossivel = disciplinasHorarioUnico.size() > 1;
    }


    public static Set<ConflitoHorario> checarPorConflitos(Set<Disciplina> disciplinas) {
        Set<ConflitoHorario> conflitos = new HashSet<>();
        for (DiadaSemana dia : DiadaSemana.values()) {
            for (Hora hora : Hora.values()) {
                ConflitoHorario conflito = ConflitoHorario.checarPorConflito(dia, hora, disciplinas);
                if (conflito != null) {
                    conflitos.add(conflito);
                }
            }
        }
        return conflitos;
    }

    /**
     * Checa por todas as turmas do conjunto de disciplinas que se conflitam em um certo bloco de horário
     *
     * @param dia           dia da semana escolhido para teste
     * @param hora          hora escolhida para teste
     * @param disciplinaSet conjunto de disciplinas escolhidas para o teste
     * @return um objeto ConflitoHorário representando o conflito, null caso não haja conflito
     */
    private static ConflitoHorario checarPorConflito(DiadaSemana dia, Hora hora, Set<Disciplina> disciplinaSet) {
        Map<Disciplina, Integer> turmasIntercedentes = new HashMap<>();
        for (Disciplina disciplina : disciplinaSet) {
            for (Turma turma : disciplina.getTurmas()) {
                Horario horario = turma.getHorario();
                if (horario.temInterseccao(dia, hora)) {
                    turmasIntercedentes.put(disciplina, turma.getId());
                }
            }
        }
        if (turmasIntercedentes.size() > 1) {
            return new ConflitoHorario(dia, hora, turmasIntercedentes);
        } else {
            return null;
        }
    }

    public Map<Disciplina, Integer> filtrarTurmasOtimizaveis() {
        if (otimizavel) {
            Disciplina horarioUnico = filtrarDisciplinasHorarioUnico().iterator().next();
            return turmas.entrySet().stream()
                    .filter(turmaEntry -> !turmaEntry.getKey().equals(horarioUnico))
                    .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
        }
        return null;
    }

    public List<Disciplina> filtrarDisciplinasHorarioUnico() {
        return turmas.keySet().stream().filter(Disciplina::isHorarioUnico).toList();
    }

    @Override
    public String toString() {
        return "ConflitoHorario{" +
                "hora=" + hora +
                ", dia=" + dia +
                ", turmas=" + turmas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConflitoHorario that = (ConflitoHorario) o;
        return hora == that.hora && dia == that.dia && Objects.equals(turmas, that.turmas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hora, dia, turmas);
    }

    public boolean isOtimizavel() {
        return otimizavel;
    }

    public boolean isImpossivel() {
        return impossivel;
    }

    public Hora getHora() {
        return hora;
    }

    public DiadaSemana getDia() {
        return dia;
    }

    public Map<Disciplina, Integer> getTurmas() {
        return turmas;
    }
}
