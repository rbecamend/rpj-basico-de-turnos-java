package personagens;

import enums.ResultadoAtaque;

import java.util.concurrent.ThreadLocalRandom;

public class Barbaro extends Hero{
    public Barbaro() {
        super("Bárbaro", 130, 160, 18, 25, 8, 14, 6, 10, 9, 13);
    }

    @Override
    public ResultadoAtaque realizarAtaque(Player alvo) {
        int chance = ThreadLocalRandom.current().nextInt(100);
        if (chance < 25 - alvo.getDestreza()/8) {
            return ResultadoAtaque.ERROU;
        }

        int dano = ataque - (alvo.getDefesa() / 4);
        if (chance > 70) {
            dano *= 2;
            // Toma dano proporcional ao ataque
            setHp(getHp() - (int)(dano * 0.2));
            alvo.setHp(alvo.getHp() - dano);
            return ResultadoAtaque.CRITICAL_HIT;
        }

        alvo.setHp(alvo.getHp() - dano);
        return ResultadoAtaque.ACERTOU;
    }
}
