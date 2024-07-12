package br.com.alura.Screenmatch;

import br.com.alura.Screenmatch.model.DadosDaSerie;
import br.com.alura.Screenmatch.service.ConsumoApi;
import br.com.alura.Screenmatch.service.ConverterDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=a5f2ab93");
		System.out.println(json);
		ConverterDados conversor = new ConverterDados();

		DadosDaSerie dados = conversor.obterDados(json, DadosDaSerie.class);
		System.out.println(dados);

	}
}
