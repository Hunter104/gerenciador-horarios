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
     * @param turmaPorDisciplina turmas a checar pelos conflitos
     * @return um conjunto de objetosConflito representando os conflitos encontrados
     */
    public static Set<ConflitoHorario> checarPorConflitos(Map<Disciplina, Turma> turmaPorDisciplina) {
        Set<ConflitoHorario> conflitos = new HashSet<>();
        for (DiadaSemana dia : DiadaSemana.values()) {
            for (Hora hora : Hora.values()) {
                ConflitoHorario.checarPorConflito(dia, hora, turmaPorDisciplina).ifPresent(conflitos::add);
            }
        }
        return conflitos;
    }

    /**
     * Checa por todas as turmas do conjunto de disciplinas que se conflitam em um certo bloco de horário
     *
     * @param dia         dia da semana escolhido para teste
     * @param hora        hora escolhida para teste
     * @param disciplinas conjunto de disciplinas escolhidas para o teste
     * @return um objeto ConflitoHorário representando o conflito, null caso não haja conflito
     */
    private static Optional<ConflitoHorario> checarPorConflito(DiadaSemana dia, Hora hora, Collection<Disciplina> disciplinas) {
        Map<Disciplina, Set<Turma>> turmasIntercedentesPorDisciplina = disciplinas.stream()
                .filter(disciplina -> disciplina.temTurmaIntercedente(dia, hora))
                .collect(Collectors.toMap(
                        disciplina -> disciplina,
                        disciplina -> disciplina.filtrarTurmasIntercedentes(dia, hora)
                ));
        return getConflitoHorario(dia, hora, turmasIntercedentesPorDisciplina);
    }

    private static Optional<ConflitoHorario> checarPorConflito(DiadaSemana dia,
                                                               Hora hora, Map<Disciplina, Turma> turmaPorDisciplina) {

        Map<Disciplina, Set<Turma>> turmasIntercedentes = turmaPorDisciplina.entrySet().stream()
                .filter(entry -> entry.getValue().temInterseccao(dia, hora))
                .collect(
                        Collectors.toMap(Map.Entry::getKey, entry -> Collections.singleton(entry.getValue()))
                );

        return getConflitoHorario(dia, hora, turmasIntercedentes);
    }

    private static Optional<ConflitoHorario> getConflitoHorario(DiadaSemana dia
            , Hora hora, Map<Disciplina, Set<Turma>> turmasIntercedentesPorDisciplina) {

        if (turmasIntercedentesPorDisciplina.size() > 1) {
            Set<Disciplina> disciplinasHorarioUnico = turmasIntercedentesPorDisciplina.keySet().stream()
                    .filter(Disciplina::isHorarioUnico)
                    .collect(Collectors.toSet());

            boolean otimizavel = disciplinasHorarioUnico.size() == 1;
            boolean impossivel = disciplinasHorarioUnico.size() > 1;

            ConflitoHorario conflito = new ConflitoHorario(dia, hora, turmasIntercedentesPorDisciplina, otimizavel, impossivel);
            return Optional.of(conflito);
        } else {
            return Optional.empty();
        }
    }

    public Map<Disciplina, Set<Turma>> filtrarTurmasOtimizaveisPorDisciplina() {
        Disciplina horarioUnico = filtrarDisciplinasHorarioUnico().iterator().next();
        return turmas.entrySet().stream()
                .filter(turmaEntry -> !turmaEntry.getKey().equals(horarioUnico))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Set<Disciplina> filtrarDisciplinasHorarioUnico() {
        return turmas.keySet().stream().filter(Disciplina::isHorarioUnico).collect(Collectors.toSet());
    }
}
