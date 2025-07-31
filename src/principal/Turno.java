package principal;

import enums.ResultadoAtaque;
import personagens.Hero;
import personagens.Monster;
import personagens.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Turno {
    private final List<Player> jogadores;
    private final Log log;

    public Turno(List<Player> jogadores, Log log) {
        this.jogadores = new ArrayList<>(jogadores);
        this.log = log;
        ordenarPorVelocidade();
    }

    private void ordenarPorVelocidade() {
        jogadores.sort(Comparator.comparingInt(Player::getVelocidade).reversed());
    }

    public void executar(List<Hero> herois, List<Monster> monstros) {
        Scanner scanner = new Scanner(System.in);

        for (Player jogador : jogadores) {
            if (jogoAcabou(herois, monstros)) {
                return;
            }

            if (jogador.getHp() <= 0) {
                log.registrar(jogador.getNome() + " está incapacitado e não pode agir!");
                continue;
            }

            if (jogador instanceof Hero hero) {
                if (monstros.isEmpty()) continue;

                System.out.println("\n----- Turno de " + hero.getNome() + " -----");
                System.out.println("Escolha um monstro para atacar:");

                // Exibir os monstros restantes
                for (int i = 0; i < monstros.size(); i++) {
                    Monster monstro = monstros.get(i);
                    if (monstro.getHp() > 0) {
                        System.out.println((i + 1) + ". " + monstro.getNome() +
                                " - HP: " + monstro.getHp() + "/" + monstro.getMaxHp());
                    }
                }

                Monster alvo = null;
                while (alvo == null) {
                    System.out.print("Digite o número do monstro: ");
                    try {
                        int escolha = scanner.nextInt() - 1;
                        if (escolha >= 0 && escolha < monstros.size()) {
                            alvo = monstros.get(escolha);
                            if (alvo.getHp() <= 0) {
                                System.out.println("Este monstro já está derrotado!");
                                alvo = null;
                            }
                        } else {
                            System.out.println("Número inválido!");
                        }
                    } catch (Exception e) {
                        System.out.println("Entrada inválida! Digite um número.");
                        scanner.next(); // Limpar buffer
                    }
                }

                System.out.println("Alvo selecionado: " + alvo.getNome());
                ResultadoAtaque resultado = hero.realizarAtaque(alvo);

                String mensagem = gerarMensagemAtaque(hero, alvo, resultado);
                log.registrar(mensagem);
                System.out.println(">>> " + mensagem);

            } else if (jogador instanceof Monster monstro) {
                if (herois.isEmpty()) continue;

                log.registrar("\n----- Turno de " + monstro.getNome() + " -----");
                Player alvo = monstro.escolherAlvo(herois);

                if (alvo != null && alvo.getHp() > 0) {
                    ResultadoAtaque resultado = monstro.realizarAtaque(alvo);
                    String mensagem = gerarMensagemAtaque(monstro, alvo, resultado);
                    log.registrar(mensagem);
                    System.out.println(">>> " + mensagem);
                }
            }

            // Pausa para leitura
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private String gerarMensagemAtaque(Player atacante, Player alvo, ResultadoAtaque resultado) {
        String mensagem = atacante.getNome() + " ataca " + alvo.getNome() + ": ";
        int hpAtual = alvo.getHp();
        int hpMax = alvo.getMaxHp();

        return switch (resultado) {
            case ERROU -> mensagem + "ERROU!";
            case ACERTOU -> mensagem + "ACERTOU! HP: " + hpAtual + "/" + hpMax;
            case CRITICAL_HIT -> mensagem + "CRÍTICO! HP: " + hpAtual + "/" + hpMax;
            default -> mensagem + "Ação realizada";
        };
    }

    private boolean jogoAcabou(List<Hero> herois, List<Monster> monstros) { //verifica se ainda existem personagens vivos, não apenas se as listas estão vazias
        return !existeHeroiVivo(herois) || !existeMonstroVivo(monstros);
    }

    private boolean existeHeroiVivo(List<Hero> herois) {
        return herois.stream().anyMatch(h -> h.getHp() > 0);
    }

    private boolean existeMonstroVivo(List<Monster> monstros) {
        return monstros.stream().anyMatch(m -> m.getHp() > 0);
    }
}
