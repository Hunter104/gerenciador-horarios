package com.hunter104.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

public class PlanejadorGradeHoraria implements PropertyChangeListener{
    private final Set<Disciplina> disciplinas;
    private final PropertyChangeSupport support;
    private static final int ADICIONAR = 0;
    private static final int REMOVER = 1;
    private Set<ConflitoHorario> conflitos;
    private Map<Disciplina, Turma> turmasEscolhidas;

    public PlanejadorGradeHoraria() {
        disciplinas = new HashSet<>();
        conflitos = new HashSet<>();
        turmasEscolhidas = new HashMap<>();
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void adicionarDisciplina(String codigo, String nome, String abreviacao, int cargaHoraria) {
        adicionarDisciplina(new Disciplina(codigo, nome, abreviacao,cargaHoraria));
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        disciplina.addPropertyChangeListener(this);
        operareNotificar(disciplina, ADICIONAR);
    }

    public void removerDisciplina(String nome) {
        removerDisciplina(getDisciplina(nome));
    }

    public void removerDisciplina(Disciplina disciplina) {
        operareNotificar(disciplina, REMOVER);
    }

    private void operareNotificar(Disciplina disciplina, int operacao) {

        Set<Disciplina> disciplinasAntigas = new HashSet<>(disciplinas);
        Set<ConflitoHorario> conflitosAntigos = new HashSet<>(conflitos);

        switch (operacao) {
            case ADICIONAR -> disciplinas.add(disciplina);
            case REMOVER -> disciplinas.remove(disciplina);
        }

        support.firePropertyChange("disciplinas", disciplinasAntigas, disciplinas);
        support.firePropertyChange("conflitos", conflitosAntigos, conflitos);
    }

    public void escolherUmaTurma(Map<Disciplina, Turma> turmaEscolhida) {
        turmasEscolhidas.putAll(turmaEscolhida);
    }

    /**
     * Verifica se há algum conflito com as turmas escolhidas
     * @return true caso as turmas não tenham nenhum conflito, false caso contrário
     */
    public boolean validarTurmasEscolhidas() {
        return ConflitoHorario.checarPorConflitos(turmasEscolhidas).size() > 0;
    }

    /**
     * Remove turmas em conflito com disciplinas de horário único, já que não há como escolher um horário diferente
     * para a disciplina, a única opção é remover as outras turmas do conflito.
     */
    public void removerTurmasInalcancaveis() {
        Set<Disciplina> disciplinasAntigas = new HashSet<>(disciplinas);
        Set<ConflitoHorario> conflitosAntigos = new HashSet<>(conflitos);

       conflitos.stream()
                .filter(ConflitoHorario::otimizavel)
                .flatMap(conflitoHorario -> conflitoHorario.filtrarTurmasOtimizaveis().entrySet().stream())
                .forEach(disciplinaTurmaEntry -> disciplinaTurmaEntry.getKey().removerTurmas(disciplinaTurmaEntry.getValue()));

        atualizarConflitos();
        if (existemDisciplinasInalcancaveis()) {
            removerTurmasInalcancaveis();
        }
        support.firePropertyChange("disciplinas", disciplinasAntigas, disciplinas);
        support.firePropertyChange("conflitos", conflitosAntigos, conflitos);
    }

    public boolean existemDisciplinasInalcancaveis() {
        return conflitos.stream().anyMatch(ConflitoHorario::otimizavel);
    }

    public void atualizarConflitos() {
        conflitos = ConflitoHorario.checarPorConflitos(disciplinas);
    }

    public Disciplina getDisciplina(String nome) {
        return disciplinas.stream().filter(disciplina -> disciplina.getNome().equals(nome)).findFirst().orElseThrow();
    }

    public List<Disciplina> getDisciplinasOrdemAlfabetica() {
        return disciplinas.stream().sorted(Comparator.comparing(Disciplina::getNome)).toList();
    }

    public int getCargaHorariaTotalHoras() {
        return disciplinas.stream().map(Disciplina::getCargaHoraria).mapToInt(Integer::intValue).sum();
    }

    public int getCargaHorariaTotalCreditos() {
        int cargaHoras = disciplinas.stream().map(Disciplina::getCargaHoraria).mapToInt(Integer::intValue).sum();
        return (cargaHoras/15);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "turmas")) {
            atualizarConflitos();
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

    public Map<Disciplina, Turma> getTurmasEscolhidas() {
        return turmasEscolhidas;
    }

    public void setTurmasEscolhidas(Map<Disciplina, Turma> turmasEscolhidas) {
        this.turmasEscolhidas = turmasEscolhidas;
    }
}
