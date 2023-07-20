package com.hunter104.model;

import java.beans.PropertyChangeSupport;
import java.io.PipedOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PlanejadorGradeHoraria {
    private final Set<Disciplina> disciplinas;
    private Set<ConflitoHorario> conflitos;
    private final PropertyChangeSupport support;

    public PlanejadorGradeHoraria() {
        disciplinas = new HashSet<>();
        conflitos = new HashSet<>();
        support = new PropertyChangeSupport(this);
    }

    public void adicionarDisciplina(String nome, int cargaHoraria) {
        Disciplina disciplina = new Disciplina(nome, cargaHoraria);

        Set<Disciplina> disciplinasAntigas = new HashSet<>(disciplinas);
        Set<ConflitoHorario> conflitosAntigos = new HashSet<>(conflitos);

        disciplinas.add(disciplina);
        atualizarConflitos();

        support.firePropertyChange("disciplinas", disciplinasAntigas, disciplinas);
        support.firePropertyChange("conflitos", conflitosAntigos, conflitos);
    }

    public void removerDisciplina(String nome) {
        Set<Disciplina> disciplinasAntigas = new HashSet<>(disciplinas);
        Set<ConflitoHorario> conflitosAntigos = new HashSet<>(conflitos);

        disciplinas.removeIf(disciplina -> disciplina.getNome().equals(nome));
        atualizarConflitos();

        support.firePropertyChange("disciplinas", disciplinasAntigas, disciplinas);
        support.firePropertyChange("conflitos", conflitosAntigos, conflitos);
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
            if (conflito.isOtimizavel()) {
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
        Set<Disciplina> disciplinasAntigas = new HashSet<>(disciplinas);
        Set<ConflitoHorario> conflitosAntigos = new HashSet<>(conflitos);

        for (ConflitoHorario conflito : conflitos) {
            if (conflito.isOtimizavel()) {
                Map<Disciplina, Integer> turmasOtimizaveis = conflito.filtrarTurmasOtimizaveis();
                for (Map.Entry<Disciplina, Integer> turmaEntry : turmasOtimizaveis.entrySet()) {
                    Disciplina disciplina = turmaEntry.getKey();
                    disciplina.removerTurma(turmaEntry.getValue());
                }
            }
        }

        // Se depois das alterações algumas turmas se tornarem inalcançáveis, chamar a função novamente.
        atualizarConflitos();
        if (existemDisciplinasInalcancaveis()) {
            removerTurmasInalcancaveis();
        }

        support.firePropertyChange("disciplinas", disciplinasAntigas, disciplinas);
        support.firePropertyChange("conflitos", conflitosAntigos, conflitos);
    }

    /**
     * Tenta criar uma grade horária com as turmas escolhidas,
     * disciplinas com turmas únicas são opcionais
     *
     * @param turmasEscolhidas turmas escolhidas para montar a grade
     * @return Um objeto representando a grade horária
     */
    public GradeHoraria criarGradeHoraria(Map<Disciplina, Integer> turmasEscolhidas) {
        Set<Disciplina> disciplinasEscolhidas = criarDisciplinasComTurmas(turmasEscolhidas);
        return new GradeHoraria(disciplinasEscolhidas);
    }

    /**
     * Cria uma cópia das disciplinas contendo apenas as turmas escolhidas
     * @param turmasEscolhidas turmas escolhidas para copiar
     * @return um conjunto com uma cópia das disciplinas contendo apenas as turmas escolhidas
     */
    private Set<Disciplina> criarDisciplinasComTurmas(Map<Disciplina, Integer> turmasEscolhidas) {
        Set<Disciplina> disciplinas = new HashSet<>();
        for (Map.Entry<Disciplina, Integer> turmaEntry : turmasEscolhidas.entrySet()) {
            Disciplina disciplinaAtual = turmaEntry.getKey();
            Turma turmaAtual = disciplinaAtual.getTurma(turmaEntry.getValue());
            Disciplina disciplinaCopiada = new Disciplina(
                    disciplinaAtual.getNome(),
                    disciplinaAtual.getAbreviacao(),
                    disciplinaAtual.getCargaHoraria());
            disciplinaCopiada.adicionarTurma(
                    turmaAtual.getId(),
                    turmaAtual.getProfessor(),
                    turmaAtual.getHorario()
            );
            disciplinas.add(disciplinaCopiada);
        }
        return disciplinas;
    }

    public void atualizarConflitos() {
        conflitos = ConflitoHorario.checarPorConflitos(disciplinas);
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
