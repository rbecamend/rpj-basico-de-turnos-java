package principal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== DUNGEON DA PERDIÇÃO =====");
            System.out.println("História: Quatro heróis caíram em uma dungeon escura");
            System.out.println("durante uma expedição na Floresta dos Sussurros.");
            System.out.println("Agora, cercados por criaturas sombrias, eles devem");
            System.out.println("lutar para sobreviver e encontrar a saída.");
            System.out.println("===============================");
            System.out.println("1. Iniciar Jogo");
            System.out.println("2. Sair");
            System.out.print("Escolha: ");

            int escolha;
            try {
                escolha = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Entrada inválida! Digite 1 ou 2.");
                scanner.next();
                continue;
            }

            switch (escolha) {
                case 1:
                    iniciarJogo();
                    break;
                case 2:
                    System.out.println("\nAté a próxima aventura!");
                    System.exit(0);
            }
        }
    }

    private static void iniciarJogo() {
        Scanner scanner = new Scanner(System.in);
        int dificuldade;

        while (true) {
            System.out.print("\nSelecione a dificuldade (1-Fácil, 2-Médio, 3-Difícil): ");
            try {
                dificuldade = scanner.nextInt();
                if (dificuldade >= 1 && dificuldade <= 3) break;
                System.out.println("Dificuldade inválida! Escolha 1, 2 ou 3.");
            } catch (Exception e) {
                System.out.println("Entrada inválida! Digite um número.");
                scanner.next();
            }
        }

        Game jogo = new Game(dificuldade);
        jogo.selecionarHerois();
        jogo.iniciarJogo();
    }
}
