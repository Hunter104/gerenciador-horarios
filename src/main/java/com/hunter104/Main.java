package com.hunter104;

import com.hunter104.io.ExportToCSV;
import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanejadorGradeHoraria;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static PlanejadorGradeHoraria criarPlanejadorComDados() {
        PlanejadorGradeHoraria planejador = new PlanejadorGradeHoraria();

        planejador.adicionarDisciplina("FGA0108","MATEMÁTICA DISCRETA 2","MD2", 60);
        planejador.adicionarTurma("MATEMÁTICA DISCRETA 2",
                1, "MATHEUS BERNARDINI DE SOUZA", "26T23");


        planejador.adicionarDisciplina("FGA0138","MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE","MDS", 60);
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                1, "HILMER RODRIGUES NER", "35T45");
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                2, "GEORGE MARSICANO CORREA", "35T23");
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                3, "CARLA SILVA ROCHA AGUIAR", "46M34");
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                4, "RICARDO AJAX DIAS KOSLOSKI", "24T23");

        planejador.adicionarDisciplina("FGA0150", "PROJETO INTEGRADOR DE ENGENHARIA 1","PI", 60);
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                4, "DIOGO CAETANO GARCIA", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                5, "RAFAEL RODRIGUES DA SILVA", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                6, "ARTUR ELIAS DE MORAIS BERTOLDI", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                7, "JULIANA PETROCCHI RODRIGUES", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                8, "RICARDO AJAX DIAS KOSLOSKI", "24T45");


        planejador.adicionarDisciplina("FGA0142", "FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES","FAC", 60);
        planejador.adicionarTurma("FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES",
                1, "TIAGO ALVES DA FONSECA", "24M34");
        planejador.adicionarTurma("FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES",
                2, "JOHN LENON CARDOSO GARDENGHI", "46T45");


        planejador.adicionarDisciplina("FGA0147", "ESTRUTURAS DE DADOS E ALGORITMOS","EDA", 60);
        planejador.adicionarTurma("ESTRUTURAS DE DADOS E ALGORITMOS",
                1, "NILTON CORREIA DA SILVA", "35M34");
        planejador.adicionarTurma("ESTRUTURAS DE DADOS E ALGORITMOS",
                2, "ROSE YURI SHIMIZU", "35T23");
        planejador.adicionarTurma("ESTRUTURAS DE DADOS E ALGORITMOS",
                3, "ROSE YURI SHIMIZU", "35M34");

        planejador.adicionarDisciplina("FGA0184","GESTAO DE PRODUCAO E QUALIDADE","GPQ", 60);
        planejador.adicionarTurma("GESTAO DE PRODUCAO E QUALIDADE",
                1, "REJANE MARIA DA COSTA FIGUEIREIDO", "35T23");
        planejador.adicionarTurma("GESTAO DE PRODUCAO E QUALIDADE",
                2, "MARIO DE OLIVEIRA ANDRADE", "35T23");

        planejador.removerTurmasInalcancaveis();

        Map<Disciplina, Integer> turmasEscolhidas = new HashMap<>();
        Disciplina eda = planejador.getDisciplina("ESTRUTURAS DE DADOS E ALGORITMOS");
        Disciplina fac = planejador.getDisciplina("FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES");
        Disciplina gpq = planejador.getDisciplina("GESTAO DE PRODUCAO E QUALIDADE");
        Disciplina md2 = planejador.getDisciplina("MATEMÁTICA DISCRETA 2");
        Disciplina mds = planejador.getDisciplina("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE");
        Disciplina pi = planejador.getDisciplina("PROJETO INTEGRADOR DE ENGENHARIA 1");
        eda.setAbreviacao("EDA");
        fac.setAbreviacao("FAC");
        gpq.setAbreviacao("GPQ");
        md2.setAbreviacao("MD2");
        mds.setAbreviacao("MDS");
        pi.setAbreviacao("PI");

        return planejador;
    }

    public static void main(String[] args) {

        PlanejadorGradeHoraria planejador = new PlanejadorGradeHoraria();

        planejador.adicionarDisciplina("MATEMÁTICA DISCRETA 2", 60);
        planejador.adicionarTurma("MATEMÁTICA DISCRETA 2",
                1, "MATHEUS BERNARDINI DE SOUZA", "26T23");


        planejador.adicionarDisciplina("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE", 60);
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                1, "HILMER RODRIGUES NER", "35T45");
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                2, "GEORGE MARSICANO CORREA", "35T23");
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                3, "CARLA SILVA ROCHA AGUIAR", "46M34");
        planejador.adicionarTurma("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE",
                4, "RICARDO AJAX DIAS KOSLOSKI", "24T23");

        planejador.adicionarDisciplina("PROJETO INTEGRADOR DE ENGENHARIA 1", 60);
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                4, "DIOGO CAETANO GARCIA", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                5, "RAFAEL RODRIGUES DA SILVA", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                6, "ARTUR ELIAS DE MORAIS BERTOLDI", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                7, "JULIANA PETROCCHI RODRIGUES", "24T45");
        planejador.adicionarTurma("PROJETO INTEGRADOR DE ENGENHARIA 1",
                8, "RICARDO AJAX DIAS KOSLOSKI", "24T45");


        planejador.adicionarDisciplina("FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES", 60);
        planejador.adicionarTurma("FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES",
                1, "TIAGO ALVES DA FONSECA", "24M34");
        planejador.adicionarTurma("FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES",
                2, "JOHN LENON CARDOSO GARDENGHI", "46T45");


        planejador.adicionarDisciplina("ESTRUTURAS DE DADOS E ALGORITMOS", 60);
        planejador.adicionarTurma("ESTRUTURAS DE DADOS E ALGORITMOS",
                1, "NILTON CORREIA DA SILVA", "35M34");
        planejador.adicionarTurma("ESTRUTURAS DE DADOS E ALGORITMOS",
                2, "ROSE YURI SHIMIZU", "35T23");
        planejador.adicionarTurma("ESTRUTURAS DE DADOS E ALGORITMOS",
                3, "ROSE YURI SHIMIZU", "35M34");

        planejador.adicionarDisciplina("GESTAO DE PRODUCAO E QUALIDADE", 60);
        planejador.adicionarTurma("GESTAO DE PRODUCAO E QUALIDADE",
                1, "REJANE MARIA DA COSTA FIGUEIREIDO", "35T23");
        planejador.adicionarTurma("GESTAO DE PRODUCAO E QUALIDADE",
                2, "MARIO DE OLIVEIRA ANDRADE", "35T23");

        planejador.removerTurmasInalcancaveis();

        Map<Disciplina, Integer> turmasEscolhidas = new HashMap<>();
        Disciplina eda = planejador.getDisciplina("ESTRUTURAS DE DADOS E ALGORITMOS");
        Disciplina fac = planejador.getDisciplina("FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES");
        Disciplina gpq = planejador.getDisciplina("GESTAO DE PRODUCAO E QUALIDADE");
        Disciplina md2 = planejador.getDisciplina("MATEMÁTICA DISCRETA 2");
        Disciplina mds = planejador.getDisciplina("MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE");
        Disciplina pi = planejador.getDisciplina("PROJETO INTEGRADOR DE ENGENHARIA 1");
        eda.setAbreviacao("EDA");
        fac.setAbreviacao("FAC");
        gpq.setAbreviacao("GPQ");
        md2.setAbreviacao("MD2");
        mds.setAbreviacao("MDS");
        pi.setAbreviacao("PI");
        turmasEscolhidas.put(eda, 3);
        turmasEscolhidas.put(fac, 1);
        turmasEscolhidas.put(gpq, 2);
        turmasEscolhidas.put(md2, 1);
        turmasEscolhidas.put(mds, 1);
        turmasEscolhidas.put(pi, 5);

        ExportToCSV.exportarGradeParaCSV(planejador.criarGradeHoraria(turmasEscolhidas));
    }
}