package principal;

import personagens.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Game {
    private final List<Hero> herois = new ArrayList<>();
    private final List<Monster> monstros = new ArrayList<>();
    private final Log log = new Log();
    private final int dificuldade;

    public Game(int dificuldade) {
        this.dificuldade = dificuldade;
    }

    public void selecionarHerois() {
        Scanner scanner = new Scanner(System.in);
        List<Hero> opcoes = new ArrayList<>();

        // Gerar 2 opções de cada classe
        for (int i = 0; i < 2; i++) {
            opcoes.add(new Guerreiro());
            opcoes.add(new Mago());
            opcoes.add(new Arqueiro());
            opcoes.add(new Furtivo());
            opcoes.add(new Clerigo());
            opcoes.add(new Paladino());
            opcoes.add(new Barbaro());
        }

        // Embaralhar opções
        Collections.shuffle(opcoes);

        System.out.println("\n===== SELECIONE 3 HERÓIS =====");
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i+1) + ". " + opcoes.get(i));
        }

        Set<Integer> escolhas = new HashSet<>();
        while (herois.size() < 3) {
            System.out.print("\nEscolha o herói #" + (herois.size() + 1) + " (1-" + opcoes.size() + "): ");
            try {
                int escolha = scanner.nextInt();
                if (escolha < 1 || escolha > opcoes.size()) {
                    System.out.println("Opção inválida! Escolha entre 1 e " + opcoes.size());
                } else if (escolhas.contains(escolha)) {
                    System.out.println("Herói já selecionado! Escolha outro.");
                } else {
                    herois.add(opcoes.get(escolha - 1));
                    escolhas.add(escolha);
                    System.out.println("Selecionado: " + opcoes.get(escolha - 1).getNome());
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número.");
                scanner.next(); // Limpa o buffer
            }
        }
    }

    public void gerarMonstros() {
        int base = herois.size();
        int quantidade = base + dificuldade + 1;

        List<Class<? extends Monster>> classes = Arrays.asList(
                Goblin.class, Orc.class, Dragao.class,
                Esqueleto.class, Bruxa.class, Troll.class,
                Demonio.class
        );

        for (int i = 0; i < quantidade; i++) {
            try {
                int index = ThreadLocalRandom.current().nextInt(classes.size());
                Monster monstro = classes.get(index).getDeclaredConstructor().newInstance();
                monstros.add(monstro);
            } catch (Exception e) {
                System.err.println("Erro ao criar monstro: " + e.getMessage());
                monstros.add(new Goblin()); // Fallback
            }
        }
    }

    public void iniciarJogo() {
        gerarMonstros();
        log.registrar("===== INÍCIO DA BATALHA =====");
        log.registrar("Heróis: " + herois.stream().map(Hero::getNome).collect(Collectors.joining(", ")));
        log.registrar("Monstros: " + monstros.stream().map(Monster::getNome).collect(Collectors.joining(", ")));

        int turno = 1;
        while (!jogoAcabou()) {
            log.registrar("\n--- TURNO " + turno + " ---");

            List<Player> todosJogadores = new ArrayList<>();
            todosJogadores.addAll(herois);
            todosJogadores.addAll(monstros);

            Turno turnoAtual = new Turno(todosJogadores, log);
            turnoAtual.executar(herois, monstros);

            // Remover mortos
            herois.removeIf(h -> h.getHp() <= 0);
            monstros.removeIf(m -> m.getHp() <= 0);

            turno++;

            // Pausa entre turnos
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        terminarJogo();
    }

    private boolean jogoAcabou() {
        return herois.isEmpty() || monstros.isEmpty();
    }

    private void terminarJogo() {
        log.registrar("\n=== FIM DE JOGO ===");
        if (herois.isEmpty()) {
            log.registrar("Monstros venceram!");
        } else {
            log.registrar("Heróis venceram!");
        }

        log.registrar("\nRESUMO FINAL:");
        log.registrar("Heróis sobreviventes: " + herois.size());
        log.registrar("Monstros derrotados: " + monstros.size());

        System.out.println("\n===== RESULTADO FINAL =====");
        log.imprimirLog();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== JOGO DE TURNOS - BATALHA ÉPICA =====");

        int dificuldade = 0;
        boolean entradaValida = false;
        while (!entradaValida) {
            try {
                System.out.print("\nSelecione a dificuldade (1-Fácil, 2-Médio, 3-Difícil): ");
                dificuldade = scanner.nextInt();
                if (dificuldade < 1 || dificuldade > 3) {
                    System.out.println("Dificuldade inválida! Escolha 1, 2 ou 3.");
                } else {
                    entradaValida = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número.");
                scanner.next(); // Limpa o buffer
            }
        }

        Game jogo = new Game(dificuldade);
        jogo.selecionarHerois();
        jogo.iniciarJogo();
    }
}
