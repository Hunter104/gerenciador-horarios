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
        criarCSVHorarios(grade);
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
            Map<Disciplina, Integer> turmas = gradeHoraria.turmasEscolhidas();

            for (Map.Entry<Disciplina, Integer> entrada : turmas.entrySet()) {
                Disciplina disciplina = entrada.getKey();
                int id = entrada.getValue();
                Turma turma = disciplina.getTurma(id);

                printer.printRecord(disciplina.getNome(), id, turma.getProfessor());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void criarCSVHorarios(GradeHoraria gradeHoraria) {
        String[] headers = {"horário", "segunda", "terça", "quarta", "quinta", "sexta", "sábado"};
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader(headers)
                .build();

        try (final CSVPrinter printer = new CSVPrinter(new FileWriter("Horarios.csv"), csvFormat)) {
            Map<Disciplina, Integer> turmas = gradeHoraria.turmasEscolhidas();
            String[] disciplinasPorDia = new String[6];

            // TODO: deixar esse método menos confuso
            for (Hora hora : Hora.values()) {
                int coluna = 0;

                for (DiadaSemana dia : DiadaSemana.values()) {
                    Disciplina disciplinadoDia = descobrirInterseccaoTurma(dia, hora, turmas);
                    if (disciplinadoDia != null) {
                        disciplinasPorDia[coluna] = disciplinadoDia.getNome();
                    } else {
                        disciplinasPorDia[coluna] = "";
                    }
                    coluna++;
                }
                printer.printRecord(
                        hora.getNome(),
                        disciplinasPorDia[0],
                        disciplinasPorDia[1],
                        disciplinasPorDia[2],
                        disciplinasPorDia[3],
                        disciplinasPorDia[4],
                        disciplinasPorDia[5]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: mudar esse nome horrível
    private static Disciplina descobrirInterseccaoTurma(DiadaSemana dia, Hora hora,
                                                        Map<Disciplina, Integer> turmas) {
        for (Map.Entry<Disciplina, Integer> entrada : turmas.entrySet()) {
            Disciplina disciplina = entrada.getKey();
            Turma turma = disciplina.getTurma(entrada.getValue());
            Horario horario = turma.getHorario();

            if (horario.temInterseccao(dia, hora)) {
                return disciplina;
            }
        }
        return null;
    }
}
