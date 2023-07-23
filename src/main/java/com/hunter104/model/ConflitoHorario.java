package com.hunter104.model;

import java.util.*;
import java.util.stream.Collectors;

public record ConflitoHorario(DiadaSemana dia, Hora hora, Map<Disciplina, Set<Turma>> turmas, boolean otimizavel,
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
                ConflitoHorario.checarPorConflito(dia, hora, disciplinas).ifPresent(conflitos::add);
            }
        }
        return conflitos;
    }


    /**
     * Checa todos os conflitos que todas as turmas escolhidas tem entre sí
     *
     * @param turmas turmas a checar pelos conflitos
     * @return um conjunto de objetosConflito representando os conflitos encontrados
     */
    public static Set<ConflitoHorario> checarPorConflitos(Map<Disciplina, Turma> turmas) {
        Set<ConflitoHorario> conflitos = new HashSet<>();
        for (DiadaSemana dia : DiadaSemana.values()) {
            for (Hora hora : Hora.values()) {
                ConflitoHorario.checarPorConflito(dia, hora, turmas).ifPresent(conflitos::add);
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
    private static Optional<ConflitoHorario> checarPorConflito(DiadaSemana dia, Hora hora, Set<Disciplina> disciplinaSet) {
        // TODO: achar uma alternativa para esse mapa que só armazena uma turma por disicplina
        Map<Disciplina, Set<Turma>> turmasIntercedentes =
                disciplinaSet.stream()
                .collect(
                        Collectors.toMap(
                                disciplina -> disciplina,
                                disciplina -> disciplina
                                .getTurmas()
                                .stream()
                                .filter(turma -> turma.getHorario().temInterseccao(dia, hora))
                                .collect(Collectors.toSet()))
                ).entrySet()
                        .stream()
                        .filter(entry -> !entry.getValue().isEmpty())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getConflitoHorario(dia, hora, turmasIntercedentes);
    }

    private static Optional<ConflitoHorario> getConflitoHorario(DiadaSemana dia, Hora hora, Map<Disciplina, Set<Turma>> turmasIntercedentes) {
        if (turmasIntercedentes.size() > 1) {
            Set<Disciplina> disciplinasHorarioUnico = turmasIntercedentes
                    .keySet()
                    .stream()
                    .filter(Disciplina::isHorarioUnico)
                    .collect(Collectors.toSet());
            boolean otimizavel = disciplinasHorarioUnico.size() == 1;
            boolean impossivel = disciplinasHorarioUnico.size() > 1;
            ConflitoHorario conflito =  new ConflitoHorario(dia, hora, turmasIntercedentes, otimizavel, impossivel);
            return Optional.of(conflito);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<ConflitoHorario> checarPorConflito(DiadaSemana dia, Hora hora, Map<Disciplina, Turma> turmas) {
        // TODO: achar uma alternativa para esse mapa que só armazena uma turma por disicplina
        Map<Disciplina, Set<Turma>> turmasIntercedentes =
                turmas.entrySet().stream()
                        .filter(entry -> entry.getValue().getHorario().temInterseccao(dia, hora))
                        .collect(Collectors.toMap(Map.Entry::getKey, entry -> Collections.singleton(entry.getValue())));

        return getConflitoHorario(dia, hora, turmasIntercedentes);
    }

    public Map<Disciplina, Set<Turma>> filtrarTurmasOtimizaveis() {
        Disciplina horarioUnico = filtrarDisciplinasHorarioUnico().iterator().next();
        return turmas.entrySet().stream()
                .filter(turmaEntry -> !turmaEntry.getKey().equals(horarioUnico))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Disciplina> filtrarDisciplinasHorarioUnico() {
        return turmas.keySet().stream().filter(Disciplina::isHorarioUnico).toList();
    }
}
