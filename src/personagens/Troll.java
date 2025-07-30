package personagens;

import enums.ResultadoAtaque;

import java.util.concurrent.ThreadLocalRandom;

public class Troll extends Monster{
    public Troll() {
        super("Troll", 160, 200, 16, 22, 12, 18, 4, 8, 4, 7);
    }

    @Override
    public ResultadoAtaque realizarAtaque(Player alvo) {
        int chance = ThreadLocalRandom.current().nextInt(100);
        if (chance < 35 - alvo.getDestreza()/8) {
            return ResultadoAtaque.ERROU;
        }

        int dano = ataque - (alvo.getDefesa() / 3);
        // Regeneração
        setHp(getHp() + randomInRange(5, 10));
        alvo.setHp(alvo.getHp() - dano);
        return ResultadoAtaque.ACERTOU;
    }
}
