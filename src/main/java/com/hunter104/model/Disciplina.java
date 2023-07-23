package com.hunter104.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.stream.Collectors;

public class Disciplina {
    private static final int ADICIONAR = 0;
    private static final int REMOVER = 1;
    private final PropertyChangeSupport support;
    private String nome;
    private int cargaHoraria;
    private Set<Turma> turmas;
    private String abreviacao;
    private String codigo;

    public Disciplina(String codigo, String nome, String abreviacao, int cargaHoraria) {
        this.codigo = codigo;
        this.abreviacao = abreviacao;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.turmas = new HashSet<>();
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void adicionarTurma(int id, String professor, String salas, String horarioCodificado) {
        Horario horario = Horario.criarFromCodigo(horarioCodificado);
        adicionarTurma(id, professor, salas, horario);
    }

    public void adicionarTurma(int id, String professor, String salas, Horario horario) {
        Turma turma = new Turma(id, professor, salas, horario);
        adicionarTurma(turma);
    }

    public void adicionarTurma(Turma turma) {
        operareNotificar(turma, ADICIONAR);
    }

    public void removerTurma(int id) {
        removerTurma(getTurma(id));
    }

    public void removerTurmas(Collection<Turma> turmas) {
        turmas.forEach(this::removerTurma);
    }

    public void removerTurma(Turma turma) {
        operareNotificar(turma, REMOVER);
    }

    private void operareNotificar(Turma turma, int operacao) {
        Set<Turma> turmasAntigas = new HashSet<>(turmas);
        switch (operacao) {
            case ADICIONAR -> turmas.add(turma);
            case REMOVER -> turmas.remove(turma);
        }
        support.firePropertyChange("turmas", turmasAntigas, turmas);
    }

    public Turma getTurma(int id) {
        return turmas.stream().filter(turma -> turma.getId() == id).findFirst().orElseThrow();
    }

    public boolean isTurmaUnica() {
        return turmas.size() == 1;
    }

    public boolean isHorarioUnico() {
        if (isTurmaUnica()) {
            return true;
        }

        List<Turma> turmasList = turmas.stream().toList();

        // Comparar todas as combinações de turmas nessa disciplina
        for (int i = 0; i < turmasList.size() - 1; i++) {
            for (int j = i + 1; j < turmasList.size(); j++) {
                if (!Objects.equals(turmasList.get(i).getHorario(), turmasList.get(j).getHorario())) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Turma> getTurmasPorId() {
        return turmas.stream().sorted(Comparator.comparing(Turma::getId)).toList();
    }

    @Override
    public String toString() {
        return "Disciplina{" +
                "nome='" + nome + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                ", turmas=" + turmas +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Set<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(Set<Turma> turmas) {
        this.turmas = turmas;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
