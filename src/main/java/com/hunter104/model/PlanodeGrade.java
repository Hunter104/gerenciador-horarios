package com.hunter104.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlanodeGrade implements PropertyChangeListener {
    private enum Operacao {ADICIONAR, REMOVER}

    ;
    private final Set<Disciplina> disciplinas;
    private final PropertyChangeSupport support;
    private Set<ConflitoHorario> conflitos;
    private Map<Disciplina, Turma> turmasEscolhidas;

    public PlanodeGrade() {
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
        adicionarDisciplina(new Disciplina(codigo, nome, abreviacao, cargaHoraria));
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        disciplina.addPropertyChangeListener(this);
        operareNotificar(disciplina, Operacao.ADICIONAR);
    }

    public void removerDisciplina(String nome) {
        removerDisciplina(getDisciplina(nome));
    }

    public void removerDisciplina(Disciplina disciplina) {
        operareNotificar(disciplina, Operacao.REMOVER);
    }

    private void operareNotificar(Disciplina disciplina, Operacao operacao) {

        Set<Disciplina> disciplinasAntigas = new HashSet<>(disciplinas);
        Set<ConflitoHorario> conflitosAntigos = new HashSet<>(conflitos);
        Map<Disciplina, Turma> turmasEscolhidasAntigas = new HashMap<>(turmasEscolhidas);

        switch (operacao) {
            case ADICIONAR -> disciplinas.add(disciplina);
            case REMOVER -> disciplinas.remove(disciplina);
        }
        atualizarConflitos();
        atualizarTurmasEscolhidas();

        support.firePropertyChange("disciplinas", disciplinasAntigas, disciplinas);
        support.firePropertyChange("conflitos", conflitosAntigos, conflitos);
        support.firePropertyChange("turmasEscolhidas", turmasEscolhidasAntigas, turmasEscolhidas);
    }

    public void escolherUmaTurma(Map.Entry<Disciplina, Turma> turmaEscolhida) {
        Map<Disciplina, Turma> turmasEscolhidasAntigas = new HashMap<>(turmasEscolhidas);

        turmasEscolhidas.put(turmaEscolhida.getKey(), turmaEscolhida.getValue());

        support.firePropertyChange("turmasEscolhidas", turmasEscolhidasAntigas, turmasEscolhidas);
    }

    // TODO: remover nome horrível

    /**
     * Remove turmas em conflito com disciplinas de horário único, já que não há como escolher um horário diferente
     * para a disciplina, a única opção é remover as outras turmas do conflito.
     */
    public void removerTurmasInalcancaveis() {

        Set<Disciplina> disciplinasAntigas = new HashSet<>(disciplinas);
        Set<ConflitoHorario> conflitosAntigos = new HashSet<>(conflitos);

        while (existemDisciplinasInalcancaveis()) {
            conflitos.stream()
                    .flatMap(conflito -> conflito.filtrarTurmasOtimizaveisPorDisciplina().stream())
                    .flatMap(turmaPorDisciplina -> turmaPorDisciplina.entrySet().stream())
                    .forEach(this::removerTurmasPorEntrada);

            atualizarConflitos();
            atualizarTurmasEscolhidas();
        }

        support.firePropertyChange("disciplinas", disciplinasAntigas, disciplinas);
        support.firePropertyChange("conflitos", conflitosAntigos, conflitos);
    }

    private void removerTurmasPorEntrada(Map.Entry<Disciplina, Set<Turma>> parTurmasDisciplina) {
        Disciplina disciplina = parTurmasDisciplina.getKey();
        Set<Turma> turmas = parTurmasDisciplina.getValue();
        disciplina.removerTurmas(turmas);
    }

    public Map<Disciplina, Set<Turma>> getTurmasEscolhiveis() {
        Set<Disciplina> disciplinasEscolhidas = turmasEscolhidas.keySet();
        return disciplinas.stream()
                .filter(Predicate.not(disciplinasEscolhidas::contains))   // Filtrar disciplinas que não foram escolhidas
                .collect(Collectors.toMap(disciplina -> disciplina, this::filtrarTurmasSemConflito));
    }

    private Set<Turma> filtrarTurmasSemConflito(Disciplina disciplina) {
        return disciplina.getTurmas()
                .stream()
                .filter(turma -> !turma.conflitaComTurmas(turmasEscolhidas.values()))
                .collect(Collectors.toSet());
    }

    private void atualizarTurmasEscolhidas() {
        Set<Turma> todasAsTurmas = disciplinas
                .stream()
                .flatMap(disciplina -> disciplina.getTurmas().stream())
                .collect(Collectors.toSet());
        turmasEscolhidas = turmasEscolhidas.entrySet().stream()
                .filter(entry -> disciplinas.contains(entry.getKey()) && todasAsTurmas.contains(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Verifica se há algum conflito com as turmas escolhidas
     *
     * @return true caso as turmas não tenham nenhum conflito, false caso contrário
     */
    public boolean validarTurmasEscolhidas() {
        return ConflitoHorario.checarPorConflitos(turmasEscolhidas).size() > 0;
    }

    public boolean existemDisciplinasInalcancaveis() {
        return conflitos.stream().anyMatch(ConflitoHorario::isOtimizavel);
    }

    public void atualizarConflitos() {
        conflitos = ConflitoHorario.checarPorConflitos(disciplinas);
    }

    public Disciplina getDisciplina(String nome) {
        return disciplinas.stream().filter(disciplina -> disciplina.getNome().equals(nome)).findFirst().orElseThrow();
    }

    public int getCargaHorariaTotalHoras() {
        return disciplinas.stream().map(Disciplina::getCargaHoraria).mapToInt(Integer::intValue).sum();
    }

    public int getCargaHorariaTotalCreditos() {
        int cargaHoras = disciplinas.stream().map(Disciplina::getCargaHoraria).mapToInt(Integer::intValue).sum();
        return (cargaHoras / 15);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "turmas")) {
            atualizarConflitos();
            atualizarTurmasEscolhidas();
        }
    }

    @Override
    public String toString() {
        return "GradeHoraria{" +
                "disciplinas=" + disciplinas +
                ", conflitos=" + conflitos +
                '}';
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

    public Map<Disciplina, Set<Turma>> getTurmasEscolhidasEmSet() {
        return turmasEscolhidas
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Set.of(entry.getValue())));
    }
}
