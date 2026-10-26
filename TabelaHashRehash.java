import java.util.Random;

public class TabelaHashRehash {

    public static void main(String[] args) {
        int tamanho = 100;
        int quantidade = 100;
        int limite = 100001;
        int maxOcupacao = tamanho * 90 / 100;

        Random random = new Random(1090);

        int[] tabela = new int[tamanho];
        boolean[] ocupada = new boolean[tamanho];
        int ocupados = 0;

        int[] valores = new int[quantidade];
        int[] colisoes = new int[quantidade];
        int[] posicoes = new int[quantidade];
        int inseridos = 0;

        int totalColisoes = 0;
        int rejeitados = 0;

        for (int i = 0; i < quantidade; i++) {
            int valor = random.nextInt(limite);

            if (ocupados >= maxOcupacao) {
                rejeitados++;
                System.out.println("Insercao bloqueada (ocupacao de 90% atingida): " + valor);
            } else {
                int pos = valor % tamanho;
                int col = 0;

                while (ocupada[pos]) {
                    col++;
                    pos = (pos + 1) % tamanho;
                }

                tabela[pos] = valor;
                ocupada[pos] = true;
                ocupados++;

                valores[inseridos] = valor;
                colisoes[inseridos] = col;
                posicoes[inseridos] = pos;
                inseridos++;
                totalColisoes += col;
            }
        }

        System.out.println();
        System.out.println("Elementos inseridos: " + inseridos);
        System.out.println("Insercoes bloqueadas pelo rehash: " + rejeitados);
        System.out.println("Total de colisoes na insercao: " + totalColisoes);
        System.out.println();

        int quantidadeTop = 3;
        if (inseridos < quantidadeTop) {
            quantidadeTop = inseridos;
        }
        boolean[] usado = new boolean[inseridos];
        int[] top = new int[quantidadeTop];
        for (int k = 0; k < quantidadeTop; k++) {
            int melhor = -1;
            for (int j = 0; j < inseridos; j++) {
                if (!usado[j] && (melhor == -1 || colisoes[j] > colisoes[melhor])) {
                    melhor = j;
                }
            }
            usado[melhor] = true;
            top[k] = melhor;
        }

        System.out.println("Os " + quantidadeTop + " elementos que mais colidiram:");
        for (int k = 0; k < quantidadeTop; k++) {
            int idx = top[k];
            System.out.println((k + 1) + ") valor " + valores[idx]
                    + " | colisoes: " + colisoes[idx]
                    + " | posicao final: " + posicoes[idx]);
        }
        System.out.println();

        System.out.println("Posicoes livres na matriz:");
        int livres = 0;
        for (int i = 0; i < tamanho; i++) {
            if (!ocupada[i]) {
                System.out.print(i + " ");
                livres++;
            }
        }
        System.out.println();
        System.out.println("Total de posicoes livres: " + livres);
        System.out.println();

        System.out.println("Teste de busca:");
        for (int k = 0; k < quantidadeTop; k++) {
            int alvo = valores[top[k]];
            int pos = alvo % tamanho;
            int passos = 1;

            while (ocupada[pos] && tabela[pos] != alvo && passos < tamanho) {
                pos = (pos + 1) % tamanho;
                passos++;
            }

            if (ocupada[pos] && tabela[pos] == alvo) {
                System.out.println("Valor " + alvo + " ENCONTRADO na posicao " + pos
                        + " (" + passos + " posicoes consultadas)");
            } else {
                System.out.println("Valor " + alvo + " NAO encontrado");
            }
        }
    }
}