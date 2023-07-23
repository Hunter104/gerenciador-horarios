package com.hunter104.io;

import com.hunter104.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExportToCSV {
    public static void exportarGradeParaCSV(Map<Disciplina, Turma> turmas) {
        criarCSVTurmas(turmas);
    }

    public static void exportarTurmasPlanejadas(PlanejadorGradeHoraria planos) {
        String[] headers = {"Disciplina", "Turma", "Professor"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader(headers)
                .build();

        try (final CSVPrinter printer = new CSVPrinter(new FileWriter("Turmas.csv"), csvFormat)) {

            // Arranjadas disciplinas em ordem alfabética para ficar mais organizador
            List<Disciplina> disciplinas = planos.getDisciplinas()
                    .stream()
                    .sorted(Comparator.comparing(Disciplina::getNome))
                    .toList();

            for (Disciplina disciplina : disciplinas) {
                for (Turma turma : disciplina.getTurmas()) {
                    printer.printRecord(disciplina.getNome(), turma.getId(), turma.getProfessor());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void criarCSVHorarios(Map<Disciplina, Turma> turmas) {
        String[] headers = {"Horário", "segunda", "terça", "quarta", "quinta", "sexta", "sábado"};
        DiadaSemana[] dias = DiadaSemana.values();
        Hora[] horas = Hora.values();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader(headers)
                .build();

        try (final CSVPrinter printer = new CSVPrinter(new FileWriter("Turmas.csv"), csvFormat)) {
            // (0, 0) é a prímeira célula sem contar o cabeçalho
            for (Hora hora : horas) {
                String[] dadosLinha = new String[]{};
                dadosLinha[0] = hora.getNome();
                for (int coluna = 1; coluna - 1 < dias.length; coluna++) {
                    DiadaSemana diaAtual = dias[coluna];
                    String disciplinaNesseHorario = turmas
                            .entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().getHorario().temInterseccao(diaAtual, hora))
                            .map(entry -> entry.getKey().getAbreviacao())
                            .findFirst()
                            .orElse("");
                    dadosLinha[coluna] = disciplinaNesseHorario;
                }
                printer.printRecord((Object[]) dadosLinha);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void criarCSVTurmas(Map<Disciplina, Turma> turmas) {
        String[] headers = {"Disciplina", "Turma", "Professor", "Sala"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader(headers)
                .build();

        try (final CSVPrinter printer = new CSVPrinter(new FileWriter("Turmas.csv"), csvFormat)) {
            for (Map.Entry<Disciplina, Turma> entry : turmas.entrySet()) {
                Disciplina disciplina = entry.getKey();
                Turma turma = entry.getValue();
                printer.printRecord(disciplina.getNome(), turma.getId(), turma.getProfessor(), turma.getSalas());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
