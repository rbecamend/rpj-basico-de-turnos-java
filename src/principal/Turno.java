package principal;

import enums.ResultadoAtaque;
import personagens.Hero;
import personagens.Monster;
import personagens.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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
        for (Player jogador : jogadores) {
            if (jogador.getHp() <= 0) continue;

            if (jogador instanceof Hero hero) {
                if (monstros.isEmpty()) continue;
                Player alvo = monstros.get(new Random().nextInt(monstros.size()));
                if (alvo.getHp() <= 0) continue;

                ResultadoAtaque resultado = hero.realizarAtaque(alvo);
                log.registrar(gerarMensagemAtaque(hero, alvo, resultado));
            }
            else if (jogador instanceof Monster monstro) {
                if (herois.isEmpty()) continue;
                Player alvo = monstro.escolherAlvo(herois);
                if (alvo != null && alvo.getHp() > 0) {
                    ResultadoAtaque resultado = monstro.realizarAtaque(alvo);
                    log.registrar(gerarMensagemAtaque(monstro, alvo, resultado));
                }
            }
        }
    }

    private String gerarMensagemAtaque(Player atacante, Player alvo, ResultadoAtaque resultado) {
        String mensagem = atacante.getNome() + " ataca " + alvo.getNome();
        return switch (resultado) {
            case ERROU -> mensagem + " e errou!";
            case ACERTOU -> mensagem + " e causou dano! HP restante: " + alvo.getHp();
            case CRITICAL_HIT -> mensagem + " com CRÍTICO! HP restante: " + alvo.getHp();
            default -> mensagem;
        };
    }
}
