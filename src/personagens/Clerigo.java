package personagens;

import enums.ResultadoAtaque;

import java.util.concurrent.ThreadLocalRandom;

public class Clerigo extends Hero{
    public Clerigo() {
        super("Clérigo", 90, 110, 12, 16, 10, 15, 10, 14, 8, 12);
    }

    @Override
    public ResultadoAtaque realizarAtaque(Player alvo) {
        int chance = ThreadLocalRandom.current().nextInt(100);
        if (chance < 15 - alvo.getDestreza()/10) {
            return ResultadoAtaque.ERROU;
        }

        int dano = ataque - (alvo.getDefesa() / 4);
        if (chance > 88) {
            dano = 0; // Cura em vez de atacar
            int cura = randomInRange(10, 20);
            // Encontra o aliado com menor HP
            // Implementação real seria com referência ao time
            alvo.setHp(alvo.getHp() + cura);
            return ResultadoAtaque.CRITICAL_HIT;
        }

        alvo.setHp(alvo.getHp() - dano);
        return ResultadoAtaque.ACERTOU;
    }
}
