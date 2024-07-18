package br.com.alura.Screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosDoEpisodio(@JsonAlias("Title") String titulo,
                              @JsonAlias("Episode") int numero_episodio) {
}
