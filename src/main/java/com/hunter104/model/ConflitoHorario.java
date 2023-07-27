package com.hunter104.model;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public record ConflitoHorario(DiadaSemana dia, Hora hora, Map<Disciplina, Set<Turma>> turmas) {
    /**
     * Checa todos os conflitos que todas as turmas das disciplinas escolhidas tem entre sí
     *
     * @param disciplinas disciplinas a checar pelos conflitos
     * @return um conjunto de objetosConflito representando os conflitos encontrados
     */
    public static Set<ConflitoHorario> checarPorConflitos(Set<Disciplina> disciplinas) {
        return checarPorConflitos((dia, hora) -> checarPorConflito(dia, hora, disciplinas));
    }

    /**
     * Checa todos os conflitos que todas as turmas escolhidas tem entre sí
     *
     * @param turmaPorDisciplina turmas a checar pelos conflitos
     * @return um conjunto de objetosConflito representando os conflitos encontrados
     */
    public static Set<ConflitoHorario> checarPorConflitos(Map<Disciplina, Turma> turmaPorDisciplina) {
        return checarPorConflitos((dia, hora) -> checarPorConflito(dia, hora, turmaPorDisciplina));
    }

    private static Set<ConflitoHorario> checarPorConflitos(
            BiFunction<DiadaSemana, Hora, Optional<ConflitoHorario>> checador) {
        Set<ConflitoHorario> conflitos = new HashSet<>();
        for (DiadaSemana dia : DiadaSemana.values()) {
            for (Hora hora : Hora.values()) {
                checador.apply(dia, hora).ifPresent(conflitos::add);
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
            ConflitoHorario conflito = new ConflitoHorario(dia, hora, turmasIntercedentesPorDisciplina);
            return Optional.of(conflito);
        } else {
            return Optional.empty();
        }
    }

    public boolean isOtimizavel() {
        return turmas.keySet().stream()
                .filter(Disciplina::isHorarioUnico)
                .count() == 1;
    }

    public boolean isImpossivel() {
        return turmas.keySet().stream()
                .filter(Disciplina::isHorarioUnico)
                .count() > 1;
    }

    public Optional<Map<Disciplina, Set<Turma>>> filtrarTurmasOtimizaveisPorDisciplina() {
        if (isOtimizavel()) {
            Disciplina horarioUnico = filtrarDisciplinasHorarioUnico().iterator().next();
            return Optional.of(
                    turmas.entrySet().stream()
                            .filter(turmaEntry -> !turmaEntry.getKey().equals(horarioUnico))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );
        } else {
            return Optional.empty();
        }

    }

    public Set<Disciplina> filtrarDisciplinasHorarioUnico() {
        return turmas.keySet().stream().filter(Disciplina::isHorarioUnico).collect(Collectors.toSet());
    }
}
