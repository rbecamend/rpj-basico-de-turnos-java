package personagens;

import enums.ResultadoAtaque;

import java.util.concurrent.ThreadLocalRandom;

public class Paladino extends Hero{
    public Paladino() {
        super("Paladino", 110, 140, 16, 22, 15, 20, 8, 12, 6, 10);
    }

    @Override
    public ResultadoAtaque realizarAtaque(Player alvo) {
        int chance = ThreadLocalRandom.current().nextInt(100);
        if (chance < 10 - alvo.getDestreza()/10) {
            return ResultadoAtaque.ERROU;
        }

        int dano = ataque - (alvo.getDefesa() / 3);
        if (chance > 85) {
            dano *= (int) 1.5;
            // Auto-cura proporcional ao dano
            setHp(getHp() + (int)(dano * 0.3));
            alvo.setHp(alvo.getHp() - dano);
            return ResultadoAtaque.CRITICAL_HIT;
        }

        alvo.setHp(alvo.getHp() - dano);
        return ResultadoAtaque.ACERTOU;
    }
}
