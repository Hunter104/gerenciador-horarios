package com.hunter104.model;

import java.util.Map;
import java.util.Set;

/**
 *
 */
public record GradeHoraria(Map<Disciplina, Integer> turmasEscolhidas) {
    public GradeHoraria {
        for (Disciplina disciplina : turmasEscolhidas().keySet()) {
            int tamanhoTurma = disciplina.getTurmas().size();
            if (tamanhoTurma > 1) {
                throw new IllegalArgumentException(
                        "A grade horária tem uma disciplina com mais de uma turma selecionada"
                );
            } else if (tamanhoTurma < 1) {
                throw new IllegalArgumentException(
                        "A grade horária tem uma disciplina sem nenhuma turma selecionada"
                );
            }
        }
    }
}
