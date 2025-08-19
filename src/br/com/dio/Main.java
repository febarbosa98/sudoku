package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
            final var position = Stream.of(args)
                    .collect(toMap(
                            k -> k.split(";")[0],
                            v -> v.split(";")[1]
                    ));
            var option = -1;
            while (true){
                System.out.println("selecione uma da opcoes a seguir");
                System.out.println("1 - Iniciar um novo jogo");
                System.out.println("2 - Colocar um novo numero");
                System.out.println("3 - Remover um numero");
                System.out.println("4 - Visualizar Jogo atual");
                System.out.println("5 - Verificar status do Jogo");
                System.out.println("6 - Limpar Jogo");
                System.out.println("7 - Finalizar Jogo");
                System.out.println("8 - Sair");
                option = scanner.nextInt();

                switch (option){
                    case 1 -> startGame(position);
                    case 2 -> inputNumber();
                    case 3 -> removeNumber();
                    case 4 -> showCurrentGame();
                    case 5 -> showGameStatus();
                    case 6 -> clearGame();
                    case 7 -> finishGame();
                    case 8 -> System.exit(0);
                    default -> System.out.println("Opção invalida, selecione uma da opcoes do menu");
                }
            }
        }

    private static void startGame(final Map<String, String> position) {
        if (nonNull(board)){
            System.out.println("O jogo já foi iniciado");
            return;
        }
        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++){
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++){
                var positionConfig = position.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        board = new Board(spaces);
        System.out.println("o jogo está pronto para comecar");
    }

    private static void inputNumber() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Informe a coluna que em que o numero sera inserido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o numero sera inserido");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o numero que vai entrar na posição [%s,%s]\n", col, row);
        var value = runUntilGetValidNumber(1, 9);
        if (!board.changeValue(col,row,value)){
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", col,row);
        }

    }

    private static void removeNumber() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Informe a coluna que em que o numero sera removido");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha que em que o numero sera removido");
        var row = runUntilGetValidNumber(0, 8);
        if (!board.clearValue(col,row)){
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", col,row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        var args = new Object[81];
        var argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col: board.getSpaces()){
                args[argPos ++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Seu jgo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.printf("o jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());
        if (board.hasErrors()){
            System.out.println("O jogo contem erros");
        }else {
            System.out.println("O jogo não contem erros");
        }
    }

    private static void clearGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu progresso?");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
            System.out.println("informe 'sim' ou 'não'");
            confirm = scanner.next();
        }
        if (confirm.equalsIgnoreCase("sim")){
            board.reset();
        }
    }

    private static void finishGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        if (board.gameIsFinished()){
            System.out.println("Parabens voce concluiu o jogo");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()){
            System.out.println("Seu jogo contem erros, verifique seu board e ajuste");
        } else {
            System.out.println("Voce ainda precisa preencher algum espaço");
        }
    }


    private static int runUntilGetValidNumber(final int min, final int max){
        var current = scanner.nextInt();
        while (current < min || current > max){
            System.out.printf("Informe um numero entre %s e %s", min, max);
            current = scanner.nextInt();
        }
        return current;
    }

}
