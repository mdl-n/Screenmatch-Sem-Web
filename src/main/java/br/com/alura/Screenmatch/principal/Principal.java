package br.com.alura.Screenmatch.principal;

import br.com.alura.Screenmatch.model.DadosDaSerie;
import br.com.alura.Screenmatch.model.DadosDoEpisodio;
import br.com.alura.Screenmatch.model.DadosTemporada;
import br.com.alura.Screenmatch.model.Episodio;
import br.com.alura.Screenmatch.service.ConsumoApi;
import br.com.alura.Screenmatch.service.ConverterDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

//        System.out.println("\nTop 5 episodios:");
//        dadosEp.stream()
//                .filter(e->!e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e-> System.out.println("Primeiro filtro (N/A) " + e))
//                .sorted(Comparator.comparing(DadosDoEpisodio::avaliacao).reversed())
//                .peek(e-> System.out.println("Ordenação " + e))
//                .limit(5)
//                .peek(e-> System.out.println("limitação " + e))
//                .map(e->e.titulo().toUpperCase())
//                .peek(e-> System.out.println("mapeamento " + e))
//                .forEach(System.out::println);

        List<Episodio> episodios = dadosTemp.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d))
                ).collect(Collectors.toList());

//        episodios.forEach(System.out::println);
//        System.out.println("Digite o titulo do episodio: ");
//        String buscaEp = scanner.nextLine();
//
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(buscaEp.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado:");
//            System.out.println("Titulo completo do episodio: "+episodioBuscado.get().getTitulo());
//            System.out.println("Temporada: "+episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episodio nao localizado.");
//        }

//        System.out.println("Assistir a partir de qual ano?");
//        int resposta = scanner.nextInt();
//
//        LocalDate dataBusca = LocalDate.of(resposta, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                        ", Episodio: " + e.getTitulo() +
//                        "Data de lancamento: " + e.getDataLancamento().format(formatador)
//                ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e->e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e->e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Media: "+ est.getAverage());
        System.out.println("Melhor episodio: "+ est.getMax());
        System.out.println("Pior episodio: "+ est.getMin());
        System.out.println("Quantiadades de episodios: "+ est.getCount());

    }
}
