package personagens;

public abstract class Hero extends Player{
    public Hero(String nome, int minHp, int maxHp, int minAtaque, int maxAtaque,
                int minDefesa, int maxDefesa, int minDestreza, int maxDestreza,
                int minVelocidade, int maxVelocidade) {
        super(nome, minHp, maxHp, minAtaque, maxAtaque, minDefesa, maxDefesa,
                minDestreza, maxDestreza, minVelocidade, maxVelocidade);
    }
}
