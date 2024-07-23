package br.com.alura.Screenmatch.principal;

import br.com.alura.Screenmatch.model.DadosDaSerie;
import br.com.alura.Screenmatch.model.DadosDoEpisodio;
import br.com.alura.Screenmatch.model.DadosTemporada;
import br.com.alura.Screenmatch.service.ConsumoApi;
import br.com.alura.Screenmatch.service.ConverterDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    public final String ENDERECO="http://www.omdbapi.com/?t=";
    public final String API_KEY="&apikey=a5f2ab93";
    Scanner scanner = new Scanner(System.in);

    public void exibirMenu(){
        System.out.println("Informe o nome da serie que deseja buscar:");
        String busca = scanner.nextLine();
        ConsumoApi consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados(ENDERECO + busca.replace(" ", "+") + API_KEY);
        ConverterDados conversor = new ConverterDados();

        DadosDaSerie dadosSerie = conversor.obterDados(json, DadosDaSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> dadosTemp = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas() ; i++) {
            json = consumoApi.obterDados(ENDERECO + busca.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada conversao = conversor.obterDados(json, DadosTemporada.class);
            dadosTemp.add(conversao);
        }
        dadosTemp.forEach(System.out::println);
        dadosTemp.forEach(t->t.episodios().forEach(e-> System.out.println(e.titulo())));

        List<DadosDoEpisodio> dadosEp = dadosTemp.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 5 episodios:");
        dadosEp.stream()
                .filter(e->!e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosDoEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);
    }
}
