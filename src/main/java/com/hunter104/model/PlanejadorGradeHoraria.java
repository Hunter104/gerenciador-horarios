package com.hunter104.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PlanejadorGradeHoraria {
    private final Set<Disciplina> disciplinas;
    private final Set<ConflitoHorario> conflitos;

    public PlanejadorGradeHoraria() {
        disciplinas = new HashSet<>();
        conflitos = new HashSet<>();
    }

    public void adicionarDisciplina(String nome, int cargaHoraria) {
        Disciplina disciplina = new Disciplina(nome, cargaHoraria);
        disciplinas.add(disciplina);
        atualizarConflitos();
    }

    public void removerDisciplina(String nome) {
        disciplinas.removeIf(disciplina -> disciplina.getNome().equals(nome));
        atualizarConflitos();
    }

    public Disciplina getDisciplina(String nome) {
        return disciplinas.stream().filter(disciplina -> disciplina.getNome().equals(nome)).findFirst().orElseThrow();
    }

    public void adicionarTurma(String nomeDisciplina, int id, String professor, String horarioCodificado) {
        Turma turma = new Turma(id, professor, horarioCodificado);
        getDisciplina(nomeDisciplina).adicionarTurma(turma);
        atualizarConflitos();
    }

    public void removerTurma(String nomeDisciplina, int id) {
        getDisciplina(nomeDisciplina).removerTurma(id);
        atualizarConflitos();
    }

    public Turma getTurma(String nomeDisciplina, int id) {
        return getDisciplina(nomeDisciplina).getTurma(id);
    }

    public boolean existemDisciplinasInalcancaveis() {
        for (ConflitoHorario conflito : conflitos) {
            Map<Disciplina, Integer> conflitoTurmas = conflito.getTurmas();
            Set<Disciplina> disciplinasHorarioUnico =
                    conflitoTurmas.keySet()
                            .stream().
                            filter(Disciplina::isHorarioUnico)
                            .collect(Collectors.toSet());

            /* Só é possível remover turmas, quando apenas uma disciplina de horário único estiver em conflito,
            se mais de um horário único estiver em conflito, a grade é inválida.
            Se menos de um horário único estiver em conflito, é possível escolher outra turma
             */
            if (disciplinasHorarioUnico.size() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove turmas em conflito com disciplinas de horário único, já que não há como escolher um horário diferente
     * para a disciplina, a única opção é remover as outras turmas do conflito.
     */
    public void removerTurmasInalcancaveis() {
        for (ConflitoHorario conflito : conflitos) {
            Map<Disciplina, Integer> conflitoTurmas = conflito.getTurmas();
            Set<Disciplina> disciplinasHorarioUnico =
                    conflitoTurmas.keySet()
                            .stream().
                            filter(Disciplina::isHorarioUnico)
                            .collect(Collectors.toSet());

            /* Só é possível remover turmas, quando apenas uma disciplina de horário único estiver em conflito,
            se mais de um horário único estiver em conflito, a grade é inválida.
            Se menos de um horário único estiver em conflito, é possível escolher outra turma
             */
            if (disciplinasHorarioUnico.size() == 1) {
                Disciplina horarioUnico = disciplinasHorarioUnico.iterator().next();

                for (Map.Entry<Disciplina, Integer> entry : conflitoTurmas.entrySet()) {
                    Disciplina disciplina = entry.getKey();
                    int id = entry.getValue();

                    if (!Objects.equals(disciplina, horarioUnico)) {
                        disciplina.removerTurma(id);
                    }
                }
            }
        }

        // Se depois das alterações algumas turmas se tornarem inalcançáveis, chamar a função novamente.
        atualizarConflitos();
        if (existemDisciplinasInalcancaveis()) {
            removerTurmasInalcancaveis();
        }
    }

    /**
     * Tenta criar uma grade horária com as turmas escolhidas,
     * disciplinas com turmas únicas são opcionais
     *
     * @param turmasEscolhidas turmas escolhidas para montar a grade
     * @return Um objeto representando a grade horária
     */
    public GradeHoraria criarGradeHoraria(Map<Disciplina, Integer> turmasEscolhidas) {

        // Validar grade horária
        Set<ConflitoHorario> conflitosPossiveis = ConflitoHorario.checarPorConflito(turmasEscolhidas);
        if (conflitosPossiveis == null) {
            return new GradeHoraria(turmasEscolhidas);
        } else {
            throw new IllegalArgumentException("Algumas das turmas escolhidas tem horários conflitantes");
        }
    }

    public void atualizarConflitos() {
        for (DiadaSemana dia : DiadaSemana.values()) {
            for (Hora hora : Hora.values()) {
                ConflitoHorario conflito = ConflitoHorario.checarPorConflito(dia, hora, disciplinas);
                if (conflito != null) {
                    conflitos.add(conflito);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "GradeHoraria{" +
                "disciplinas=" + disciplinas +
                ", conflitos=" + conflitos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanejadorGradeHoraria that = (PlanejadorGradeHoraria) o;
        return Objects.equals(disciplinas, that.disciplinas) && Objects.equals(conflitos, that.conflitos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disciplinas, conflitos);
    }

    public Set<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public Set<ConflitoHorario> getConflitos() {
        return conflitos;
    }
}