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
    private int monstrosDerrotados = 0;

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

        System.out.println("\n===== SELECIONE 4 HERÓIS =====");
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.println((i+1) + ". " + opcoes.get(i));
        }

        Set<Integer> escolhas = new HashSet<>();
        while (herois.size() < 4) {
            System.out.print("\nEscolha o herói #" + (herois.size() + 1) + " (1-" + opcoes.size() + "): ");
            try {
                int escolha = scanner.nextInt();
                if (escolha < 1 || escolha > opcoes.size()) {
                    System.out.println("Opção inválida! Escolha entre 1 e " + opcoes.size());
                } else if (escolhas.contains(escolha)) {
                    System.out.println("Herói já selecionado! Escolha outro.");
                } else {
                    Hero heroiEscolhido = opcoes.get(escolha - 1);
                    herois.add(heroiEscolhido);
                    escolhas.add(escolha);
                    System.out.println("Selecionado: " + heroiEscolhido.getNome() +
                            " | HP: " + heroiEscolhido.getHp() +
                            " | ATK: " + heroiEscolhido.getAtaque());
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite um número.");
                scanner.next();
            }
        }
    }

    public void gerarMonstros() {
        int quantidade = 3 + dificuldade;

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
        log.registrar("Heróis: " + herois.stream()
                .map(h -> h.getNome() + " (HP: " + h.getHp() + ")")
                .collect(Collectors.joining(", ")));

        log.registrar("Monstros: " + monstros.stream()
                .map(m -> m.getNome() + " (HP: " + m.getHp() + ")")
                .collect(Collectors.joining(", ")));

        int turno = 1;
        while (!jogoAcabou()) {
            log.registrar("\n--- TURNO " + turno + " ---");
            System.out.println("\n=============== TURNO " + turno + " ===============");

            List<Player> todosJogadores = new ArrayList<>();
            todosJogadores.addAll(herois);
            todosJogadores.addAll(monstros);

            Turno turnoAtual = new Turno(todosJogadores, log);
            turnoAtual.executar(herois, monstros);

            // Remover mortos e contar monstros derrotados
            herois.removeIf(h -> h.getHp() <= 0);
            monstros.removeIf(m -> {
                if (m.getHp() <= 0) {
                    monstrosDerrotados++;
                    return true;
                }
                return false;
            });

            // Mostrar status resumido
            System.out.println("\nStatus após o turno:");
            System.out.println("Heróis: " + herois.stream()
                    .map(h -> h.getNome() + " (" + h.getHp() + "/" + h.getMaxHp() + ")")
                    .collect(Collectors.joining(", ")));

            System.out.println("Monstros: " + monstros.stream()
                    .map(m -> m.getNome() + " (" + m.getHp() + "/" + m.getMaxHp() + ")")
                    .collect(Collectors.joining(", ")));

            if (jogoAcabou()) {
                break;
            }
            turno++;
        }
        terminarJogo();
    }

    private boolean jogoAcabou() {
        boolean todosMonstrosMortos = monstros.stream().allMatch(m -> m.getHp() <= 0);
        boolean todosHeroisMortos = herois.stream().allMatch(h -> h.getHp() <= 0);
        return todosMonstrosMortos || todosHeroisMortos;
    }

    private void terminarJogo() {
        Scanner scanner = new Scanner(System.in);

        if (herois.isEmpty()) {
            log.registrar("\n=== FIM DE JOGO ===");
            log.registrar("MONSTROS VENCERAM!");
            System.out.println("\n=================================");
            System.out.println("       MONSTROS VENCERAM!");
            System.out.println("=================================");
        } else {
            log.registrar("\n=== FIM DE JOGO ===");
            log.registrar("HERÓIS VENCERAM!");
            System.out.println("\n=================================");
            System.out.println("       HERÓIS VENCERAM!");
            System.out.println("=================================");
        }

        log.registrar("\nRESUMO FINAL:");
        log.registrar("Heróis sobreviventes: " + herois.size());
        log.registrar("Monstros derrotados: " + monstrosDerrotados );

        System.out.println("\nResumo Final:");
        System.out.println("Heróis sobreviventes: " + herois.size());
        System.out.println("Monstros derrotados: " + monstrosDerrotados);

        while (true) {
            System.out.println("\nDeseja visualizar o log completo da batalha?");
            System.out.println("1. Sim");
            System.out.println("2. Não");
            System.out.print("Escolha: ");

            int escolha;
            try {
                escolha = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Entrada inválida! Digite 1 ou 2.");
                scanner.next(); // limpa a entrada inválida
                continue;
            }

            if (escolha == 1) {
                System.out.println("\n===== LOG COMPLETO DA BATALHA =====");
                log.imprimirLog();
                break;
            } else if (escolha == 2) {
                break;
            } else {
                System.out.println("Opção inválida! Digite 1 ou 2.");
            }
        }

        while (true) {
            System.out.println("\nDeseja voltar ao menu principal?");
            System.out.println("1. Sim");
            System.out.println("2. Não");
            System.out.print("Escolha: ");

            int opcao;
            try {
                opcao = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Entrada inválida! Digite 1 ou 2.");
                scanner.next();
                continue;
            }

            if (opcao == 1) {
                Main.main(new String[]{});
                break;
            } else if (opcao == 2) {
                System.out.println("\nObrigada por jogar! Até a próxima.");
                System.exit(0);
            } else {
                System.out.println("Opção inválida! Digite 1 ou 2.");
            }
        }
    }
}
