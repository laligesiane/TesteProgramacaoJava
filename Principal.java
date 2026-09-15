import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Principal {

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat FORMATADOR_NUMERO = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
    private static final Collator COLLATOR_PT_BR = Collator.getInstance(new Locale("pt", "BR"));

    static {
        FORMATADOR_NUMERO.setMinimumFractionDigits(2);
        FORMATADOR_NUMERO.setMaximumFractionDigits(2);
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        List<Funcionario> funcionarios = new ArrayList<>();

        System.out.println("=== 3.1 - Inserir todos os funcionários ===");
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));
        funcionarios.forEach(Principal::imprimirFuncionario);

        System.out.println("\n=== 3.2 - Remover o funcionário \"João\" ===");
        funcionarios.removeIf(f -> f.getNome().equals("João"));
        funcionarios.forEach(Principal::imprimirFuncionario);

        System.out.println("\n=== 3.3 - Imprimir todos os funcionários ===");
        funcionarios.forEach(Principal::imprimirFuncionario);

        System.out.println("\n=== 3.4 - Aumento de 10% no salário ===");
        for (Funcionario funcionario : funcionarios) {
            BigDecimal novoSalario = funcionario.getSalario()
                    .multiply(new BigDecimal("1.10"))
                    .setScale(2, RoundingMode.HALF_UP);
            funcionario.setSalario(novoSalario);
        }
        funcionarios.forEach(Principal::imprimirFuncionario);

        System.out.println("\n=== 3.5 - Agrupar funcionários por função ===");
        Map<String, List<Funcionario>> agrupadosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao, () -> new TreeMap<>(COLLATOR_PT_BR),
                        Collectors.toList()));
        System.out.println("Agrupamento concluído: " + agrupadosPorFuncao.keySet());

        System.out.println("\n=== 3.6 - Imprimir funcionários agrupados por função ===");
        for (Map.Entry<String, List<Funcionario>> entrada : agrupadosPorFuncao.entrySet()) {
            System.out.println("Função: " + entrada.getKey());
            entrada.getValue().forEach(Principal::imprimirFuncionario);
        }

        System.out.println("\n=== 3.8 - Aniversariantes dos meses 10 e 12 ===");
        for (int mes : new int[] { 10, 12 }) {
            String nomeMes = LocalDate.of(2000, mes, 1).getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
            System.out.println("Mês " + mes + " (" + nomeMes + "):");
            List<Funcionario> aniversariantes = funcionarios.stream()
                    .filter(f -> f.getDataNascimento().getMonthValue() == mes)
                    .collect(Collectors.toList());
            if (aniversariantes.isEmpty()) {
                System.out.println("Nenhum aniversariante em " + nomeMes + ".");
            } else {
                aniversariantes.forEach(Principal::imprimirFuncionario);
            }
        }

        System.out.println("\n=== 3.9 - Funcionário com a maior idade ===");
        Funcionario maisVelho = funcionarios.stream()
                .max(Comparator.comparing(Funcionario::getIdade)
                        .thenComparing(Funcionario::getNome, COLLATOR_PT_BR))
                .orElseThrow();
        System.out.println("Nome: " + maisVelho.getNome() + " | Idade: " + maisVelho.getIdade());

        System.out.println("\n=== 3.10 - Lista de funcionários em ordem alfabética ===");
        List<Funcionario> ordemAlfabetica = funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome, COLLATOR_PT_BR))
                .collect(Collectors.toList());
        ordemAlfabetica.forEach(Principal::imprimirFuncionario);

        System.out.println("\n=== 3.11 - Total dos salários dos funcionários ===");
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total: " + FORMATADOR_NUMERO.format(totalSalarios));

        System.out.println("\n=== 3.12 - Quantidade de salários mínimos por funcionário ===");
        for (Funcionario funcionario : funcionarios) {
            BigDecimal quantidadeSalariosMinimos = funcionario.getSalario()
                    .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.println(funcionario.getNome() + ": " + FORMATADOR_NUMERO.format(quantidadeSalariosMinimos)
                    + " salários mínimos");
        }
    }

    private static void imprimirFuncionario(Funcionario funcionario) {
        System.out.println("Nome: " + funcionario.getNome()
                + " | Data Nascimento: " + funcionario.getDataNascimento().format(FORMATADOR_DATA)
                + " | Salário: " + FORMATADOR_NUMERO.format(funcionario.getSalario())
                + " | Função: " + funcionario.getFuncao());
    }
}
