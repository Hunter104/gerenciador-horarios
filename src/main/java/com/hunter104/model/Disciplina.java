package com.hunter104.model;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Disciplina {
    private final String nome;
    private final int cargaHoraria;
    private final Set<Turma> turmas;
    private String abreviacao;


    public Disciplina(String nome, int cargaHoraria) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.turmas = new HashSet<>();
    }

    public Disciplina(String nome, String abreviacao, int cargaHoraria) {
        this.abreviacao = abreviacao;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.turmas = new HashSet<>();
    }

    public void adicionarTurma(int id, String professor, String horarioCodificado) {
        Turma turma = new Turma(id, professor, horarioCodificado);
        turmas.add(turma);
    }

    public void adicionarTurma(Turma turma) {
        turmas.add(turma);
    }

    public void removerTurma(int id) {
        turmas.removeIf(turma -> turma.getId() == id);
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

    @Override
    public String toString() {
        return "Disciplina{" +
                "nome='" + nome + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                ", turmas=" + turmas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return cargaHoraria == that.cargaHoraria && Objects.equals(nome, that.nome) && Objects.equals(turmas, that.turmas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cargaHoraria, turmas);
    }

    public String getNome() {
        return nome;
    }

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public Set<Turma> getTurmas() {
        return turmas;
    }
}
