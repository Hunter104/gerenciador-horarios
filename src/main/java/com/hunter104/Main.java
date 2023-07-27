package com.hunter104;

import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanodeGrade;

public class Main {

    public static PlanodeGrade criarPlanejadorComDados() {
        PlanodeGrade planejador = new PlanodeGrade();

        Disciplina md2 = new Disciplina("FGA0108", "MATEMÁTICA DISCRETA 2", "MD2", 60);
        planejador.adicionarDisciplina(md2);
        md2.adicionarTurma(1, "MATHEUS BERNARDINI DE SOUZA", "S1/S2", "26T23");


        Disciplina mds = new Disciplina("FGA0138", "MÉTODOS DE DESENVOLVIMENTO DE SOFTWARE", "MDS", 60);
        planejador.adicionarDisciplina(mds);
        mds.adicionarTurma(1, "HILMER RODRIGUES NER", "I7", "35T45");
        mds.adicionarTurma(2, "GEORGE MARSICANO CORREA", "I6", "35T23");
        mds.adicionarTurma(3, "CARLA SILVA ROCHA AGUIAR", "I6", "46M34");
        mds.adicionarTurma(4, "RICARDO AJAX DIAS KOSLOSKI", "S7", "24T23");

        Disciplina pi = new Disciplina("FGA0150", "PROJETO INTEGRADOR DE ENGENHARIA 1", "PI", 60);
        planejador.adicionarDisciplina(pi);
        pi.adicionarTurma(4, "DIOGO CAETANO GARCIA", "S2/I1", "24T45");
        pi.adicionarTurma(5, "RAFAEL RODRIGUES DA SILVA", "S2/S6", "24T45");
        pi.adicionarTurma(6, "ARTUR ELIAS DE MORAIS BERTOLDI", "S2/S5", "24T45");
        pi.adicionarTurma(7, "JULIANA PETROCCHI RODRIGUES", "S2", "24T45");
        pi.adicionarTurma(8, "RICARDO AJAX DIAS KOSLOSKI", "S2/I4", "24T45");

        Disciplina fac = new Disciplina("FGA0142", "FUNDAMENTOS DE ARQUITETURA DE COMPUTADORES", "FAC", 60);
        planejador.adicionarDisciplina(fac);
        fac.adicionarTurma(1, "TIAGO ALVES DA FONSECA", "S6", "24M34");
        fac.adicionarTurma(2, "JOHN LENON CARDOSO GARDENGHI", "S3/S2", "46T45");


        Disciplina eda = new Disciplina("FGA0147", "ESTRUTURA DE DADOS E ALGORITMOS", "EDA", 60);
        planejador.adicionarDisciplina(eda);
        eda.adicionarTurma(1, "NILTON CORREIA DA SILVA", "LAB MOCAP", "35M34");
        eda.adicionarTurma(2, "ROSE YURI SHIMIZU", "S1", "35T23");
        eda.adicionarTurma(3, "ROSE YURI SHIMIZU", "I9", "35M34");

        Disciplina gpq = new Disciplina("FGA0184", "GESTAO DE PRODUCAO E QUALIDADE", "GPQ", 60);
        planejador.adicionarDisciplina(gpq);
        gpq.adicionarTurma(1, "REJANE MARIA DA COSTA FIGUEIREIDO", "I9", "35T23");
        gpq.adicionarTurma(2, "MARIO DE OLIVEIRA ANDRADE", "S3", "35T23");

//        planejador.removerTurmasInalcancaveis();
        return planejador;
    }

}