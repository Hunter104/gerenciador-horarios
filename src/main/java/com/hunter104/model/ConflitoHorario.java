package com.hunter104.model;

import java.util.*;
import java.util.stream.Collectors;

public record ConflitoHorario(DiadaSemana dia, Hora hora, Map<Disciplina, Turma> turmas, boolean otimizavel,
                              boolean impossivel) {
    /**
     * Checa todos os conflitos que todas as turmas das disciplinas escolhidas tem entre sí
     *
     * @param disciplinas disciplinas a checar pelos conflitos
     * @return um conjunto de objetosConflito representando os conflitos encontrados
     */
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
        Map<Disciplina, Turma> turmasIntercedentes = new HashMap<>();
        for (Disciplina disciplina : disciplinaSet) {
            for (Turma turma : disciplina.getTurmas()) {
                Horario horario = turma.getHorario();
                if (horario.temInterseccao(dia, hora)) {
                    turmasIntercedentes.put(disciplina, turma);
                }
            }
        }
        if (turmasIntercedentes.size() > 1) {
            List<Disciplina> disciplinasHorarioUnico = disciplinaSet.stream().filter(Disciplina::isHorarioUnico).toList();
            boolean otimizavel = disciplinasHorarioUnico.size() == 1;
            boolean impossivel = disciplinasHorarioUnico.size() > 1;
            return new ConflitoHorario(dia, hora, turmasIntercedentes, otimizavel, impossivel);
        } else {
            return null;
        }
    }

    public Map<Disciplina, Turma> filtrarTurmasOtimizaveis() {
        Disciplina horarioUnico = filtrarDisciplinasHorarioUnico().iterator().next();
        return turmas.entrySet().stream()
                .filter(turmaEntry -> !turmaEntry.getKey().equals(horarioUnico))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Disciplina> filtrarDisciplinasHorarioUnico() {
        return turmas.keySet().stream().filter(Disciplina::isHorarioUnico).toList();
    }
}
