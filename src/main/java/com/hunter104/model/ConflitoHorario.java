package com.hunter104.model;

import java.util.*;

public class ConflitoHorario {
    private final Hora hora;
    private final DiadaSemana dia;
    private final Map<Disciplina, Integer> turmas;

    private ConflitoHorario(DiadaSemana dia, Hora hora, Map<Disciplina, Integer> turmas) {
        this.hora = hora;
        this.dia = dia;
        this.turmas = turmas;
    }

    /**
     * Checa por todas as turmas do conjunto de disciplinas que se conflitam em um certo bloco de horário
     *
     * @param dia           dia da semana escolhido para teste
     * @param hora          hora escolhida para teste
     * @param disciplinaSet conjunto de disciplinas escolhidas para o teste
     * @return um objeto ConflitoHorário representando o conflito, null caso não haja conflito
     */
    public static ConflitoHorario checarPorConflito(DiadaSemana dia, Hora hora, Set<Disciplina> disciplinaSet) {
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

    /**
     * Checa se apenas as turmas escolhidasse conflitam em um certo bloco de horário
     *
     * @param dia              dia da semana escolhido para teste
     * @param hora             hora escolhida para teste
     * @param turmasEscolhidas turmas escolhidas para o teste
     * @return um objeto ConflitoHorário representando o conflito, null caso não haja conflito
     */
    private static ConflitoHorario checarPorConflito(DiadaSemana dia, Hora hora, Map<Disciplina, Integer> turmasEscolhidas) {
        Map<Disciplina, Integer> turmasIntercedentes = new HashMap<>();
        for (Map.Entry<Disciplina, Integer> parTurma : turmasEscolhidas.entrySet()) {
            Disciplina disciplina = parTurma.getKey();
            int id = parTurma.getValue();

            Turma turmaAtual = disciplina.getTurma(id);
            Horario horario = turmaAtual.getHorario();
            if (horario.temInterseccao(dia, hora)) {
                turmasIntercedentes.put(disciplina, id);
            }
        }
        if (turmasIntercedentes.size() > 1) {
            return new ConflitoHorario(dia, hora, turmasIntercedentes);
        } else {
            return null;
        }
    }

    /**
     * Checa se as turmas escolhidas conflitam entre sí
     *
     * @param turmasEscolhidas turmas escolhidas para teste
     * @return Um conjunto representando todos os conflitos, null caso não haja
     */
    public static Set<ConflitoHorario> checarPorConflito(Map<Disciplina, Integer> turmasEscolhidas) {
        Set<ConflitoHorario> conflitos = new HashSet<>();
        for (DiadaSemana dia : DiadaSemana.values()) {
            for (Hora hora : Hora.values()) {
                ConflitoHorario conflitoPossivel = checarPorConflito(dia, hora, turmasEscolhidas);
                if (conflitoPossivel != null) {
                    conflitos.add(conflitoPossivel);
                }
            }
        }
        if (conflitos.size() > 0) {
            return conflitos;
        } else {
            return null;
        }
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
