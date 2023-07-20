package com.hunter104.model;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public record GradeHoraria(Set<Disciplina> disciplinas) {
    public GradeHoraria {
        Set<ConflitoHorario> conflitosPossiveis = ConflitoHorario.checarPorConflitos(disciplinas);
        boolean umaTurmaSomente = disciplinas.stream().allMatch(Disciplina::isTurmaUnica);
        if (conflitosPossiveis != null) {
            throw new IllegalArgumentException("Algumas das turmas escolhidas tem horários conflitantes");
        }
        if (!umaTurmaSomente) {
            throw new IllegalArgumentException("Uma ou mais disciplinas contém mais de uma turma selecionada");
        }
    }
}
