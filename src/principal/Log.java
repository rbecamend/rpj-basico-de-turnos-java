package principal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Log {
    private final List<String> entradas = new ArrayList<>();

    public void registrar(String mensagem) {
        entradas.add(mensagem);
    }

    public void imprimirLog() {
        entradas.forEach(System.out::println);
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(entradas);
    }
}
