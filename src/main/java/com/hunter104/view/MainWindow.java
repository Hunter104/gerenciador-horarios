package com.hunter104.view;

import com.hunter104.model.ConflitoHorario;
import com.hunter104.model.Disciplina;
import com.hunter104.model.PlanodeGrade;
import com.hunter104.model.Turma;
import com.sun.net.httpserver.Headers;

import javax.swing.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import static com.hunter104.Main.criarPlanejadorComDados;

public class MainWindow implements PropertyChangeListener {
    private final DisciplinasTableModel crudDisciplinasModel;
    private final TurmasTableModel crudTurmasModel;
    private final PlanodeGrade planejador;
    private final ConflitosTableModel conflitosTableModel;
    private final TurmasEspecificasTableModel turmasConflitoTableModel;
    private final TurmasEspecificasTableModel turmasPossiveisTableModel;
    private final TurmasEspecificasTableModel turmasEscolhidasTableModel;
    private JTabbedPane Main;
    private JButton adicionarDisciplinaButton;
    private JButton removerDisciplinaButton;
    private JTable turmasCrudTable;
    private JButton adicionarTurmaButton;
    private JButton removerTurmaButton;
    private JButton otimizarButton;
    private JButton visualizarConflitoButton;
    private JTable horarioConflitoTable;
    private JTable turmasConflitoTable;
    private JTable disciplinasCrudTable;
    private JTable turmasPossiveisTable;
    private JTable turmasEscolhidasTable;
    private JButton escolherTurmaButton;
    private JButton removerTurmaEscolhida;
    private JButton exportarDadosButton;
    private JLabel chHorasLabel;
    private JLabel disciplinasTituloLabel;
    private JLabel turmasTituloLabel;
    private JLabel conflitoEscolhidoLabel;
    private JPanel visualizarHorarioConflitoPanel;
    private JPanel VisualizarTurmaConflitoPanel;
    private JPanel controlPanel;
    private JLabel otimizavelLabel;

    public MainWindow() {

        // Modelo
        planejador = criarPlanejadorComDados();
        planejador.addPropertyChangeListener(this);
        planejador.getDisciplinas().forEach(disciplina -> disciplina.addPropertyChangeListener(this));

        // Tabelas
        crudDisciplinasModel = new DisciplinasTableModel(planejador.getDisciplinasOrdemAlfabetica());
        disciplinasCrudTable.setModel(crudDisciplinasModel);

        crudTurmasModel = new TurmasTableModel(planejador.getDisciplinasOrdemAlfabetica());
        turmasCrudTable.setModel(crudTurmasModel);

        conflitosTableModel = new ConflitosTableModel(planejador.getConflitos());
        horarioConflitoTable.setModel(conflitosTableModel);

        turmasConflitoTableModel = new TurmasEspecificasTableModel(new HashMap<>());
        turmasConflitoTable.setModel(turmasConflitoTableModel);

        turmasPossiveisTableModel = new TurmasEspecificasTableModel(planejador.getTurmasEscolhiveis());
        turmasPossiveisTable.setModel(turmasPossiveisTableModel);

        turmasEscolhidasTableModel = new TurmasEspecificasTableModel(planejador.getTurmasEscolhidasSet());
        turmasEscolhidasTable.setModel(turmasEscolhidasTableModel);

        // Botões
        adicionarDisciplinaButton.addActionListener(e -> adicionarDisciplina());
        removerDisciplinaButton.addActionListener(e -> removerDisicplina());

        adicionarTurmaButton.addActionListener(e -> adicionarTurma());
        removerTurmaButton.addActionListener(e -> removerTurma());

        visualizarConflitoButton.addActionListener(e -> visualizarConflito());
        otimizarButton.addActionListener(e -> planejador.removerTurmasInalcancaveis());

        escolherTurmaButton.addActionListener(e -> escolherTurma());

        // Labels
        chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));

        setLabelHeader(disciplinasTituloLabel, Header.H1);
        setLabelHeader(turmasTituloLabel, Header.H1);
    }

    private void setLabelHeader(JLabel label, Header header) {
        label.putClientProperty("FlatLaf.styleClass", header.code);
    }

    private void setLabelStyle(JLabel label, FontStyle style) {
        label.putClientProperty("FlatLaf.styleClass", style.code);
    }

    private void setLabelSize(JLabel label, FontSize size) {
        label.putClientProperty("FlatLaf.styleClass", size.code);
    }

    private void adicionarTurma() {
        mostrarDialogo(new AdicionarTurma(planejador), "Cadastrar nova turma");
    }

    private void adicionarDisciplina() {
        mostrarDialogo(new AdicionarDisciplina(planejador), "Cadastrar nova disciplina");
    }

    private void removerDisicplina() {
        int linhaEscolhida = disciplinasCrudTable.getSelectedRow();
        Disciplina disciplinaSelecionada = crudDisciplinasModel.getDisciplina(linhaEscolhida);
        planejador.removerDisciplina(disciplinaSelecionada);
    }

    private void escolherTurma() {
        int row = turmasPossiveisTable.getSelectedRow();
        Optional<Map.Entry<Disciplina, Turma>> turmaEscolhida = turmasPossiveisTableModel.getElemento(row);
        turmaEscolhida.ifPresent(planejador::escolherUmaTurma);
    }

    private void removerTurma() {
        int row = turmasCrudTable.getSelectedRow();
        Optional<Turma> turmaEscolhida = crudTurmasModel.getTurma(row);
        Optional<Disciplina> disciplinaEscolhida = crudTurmasModel.getDisciplina(row);
        turmaEscolhida.ifPresent(turma ->
                disciplinaEscolhida.ifPresent(disciplina -> disciplina.removerTurma(turma))
        );
    }

    private void visualizarConflito() {
        int row = horarioConflitoTable.getSelectedRow();
        int column = horarioConflitoTable.getSelectedColumn();
        conflitosTableModel.getConflito(row, column).ifPresent(conflito -> {
            turmasConflitoTableModel.setTurmas(conflito.turmas());
            conflitoEscolhidoLabel.setText(formatarConflito(conflito));
            otimizavelLabel.setText(conflito.isOtimizavel() ? "Sim" : "Não");
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Planejador de disciplinas");
        frame.setContentPane(new MainWindow().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private String formatarConflito(ConflitoHorario conflito) {
        return String.format("%s - %s", conflito.hora().nome(), conflito.dia().nome());
    }

    private void mostrarDialogo(JDialog dialog, String titulo) {
        dialog.setTitle(titulo);
        dialog.pack();
        dialog.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "disciplinas" -> {
                planejador.getDisciplinas().forEach(disciplina -> disciplina.addPropertyChangeListener(this));

                List<Disciplina> novasDisciplinas = planejador.getDisciplinasOrdemAlfabetica();

                crudDisciplinasModel.setLinhas(novasDisciplinas);
                crudTurmasModel.setDisciplinas(novasDisciplinas);

                chHorasLabel.setText(String.valueOf(planejador.getCargaHorariaTotalHoras()));

                turmasPossiveisTableModel.setTurmas(planejador.getTurmasEscolhiveis());
                turmasEscolhidasTableModel.setTurmas(planejador.getTurmasEscolhidasSet());
            }
            case "turmas" -> {
                crudTurmasModel.atualizarDados();
                turmasPossiveisTableModel.setTurmas(planejador.getTurmasEscolhiveis());
                turmasEscolhidasTableModel.setTurmas(planejador.getTurmasEscolhidasSet());
            }
            case "conflitos" -> {
                conflitosTableModel.setConflitos(planejador.getConflitos());
                turmasConflitoTableModel.setTurmas(new HashMap<>());
                conflitoEscolhidoLabel.setText("Nenhum");
            }
            case "turmasEscolhidas" -> {
                turmasPossiveisTableModel.setTurmas(planejador.getTurmasEscolhiveis());
                turmasEscolhidasTableModel.setTurmas(planejador.getTurmasEscolhidasSet());
            }
        }
    }

    private enum Header {
        H00("h00"),
        H0("h0"),
        H1("h1"),
        H2("h2"),
        H3("h3"),
        H4("h4");
        private final String code;

        Header(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }

    private enum FontSize {
        LARGE("large"),
        DEFAULT("default"),
        MEDIUM("medium"),
        SMALL("small"),
        Mini("mini");
        private final String code;

        FontSize(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }

    private enum FontStyle {
        MONOSPACED("monospaced"),
        LIGHT("light"),
        SEMIBOLD("semibold");
        private final String code;

        FontStyle(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }
    }

}



