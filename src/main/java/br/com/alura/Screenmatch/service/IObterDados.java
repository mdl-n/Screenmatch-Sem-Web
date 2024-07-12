package br.com.alura.Screenmatch.service;

public interface IObterDados {

    <T> T obterDados (String json, Class<T> classe);

}
