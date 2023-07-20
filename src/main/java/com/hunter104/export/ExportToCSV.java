package com.hunter104.export;

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
    public static void exportarGradeParaCSV(GradeHoraria grade) {
        criarCSVTurmas(grade);
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

    public static void criarCSVTurmas(GradeHoraria gradeHoraria) {
        String[] headers = {"Disciplina", "Turma", "Professor"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader(headers)
                .build();

        try (final CSVPrinter printer = new CSVPrinter(new FileWriter("Turmas.csv"), csvFormat)) {
            Set<Disciplina> disciplinas = gradeHoraria.disciplinas();
            List<Disciplina> disciplinasOrdenadas = disciplinas
                    .stream()
                    .sorted(Comparator.comparing(Disciplina::getNome))
                    .toList();
            for (Disciplina disciplina : disciplinasOrdenadas) {
                Turma turma = disciplina.getTurmas().iterator().next();
                printer.printRecord(disciplina.getNome(), turma.getId(), turma.getProfessor());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
