package Model;

import POJO.Pokemon;
import Services.FirestoreOutput;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.PCollection;

public class Main {

    static class PokemonFactory extends SimpleFunction<String, Pokemon>{
        @Override
        public Pokemon apply(String input) {
            return new Pokemon(input);
        }
    }

    static class headersFilter implements SerializableFunction<String, Boolean> {
        @Override
        public Boolean apply(String input) {
            return !input
                    .equals("Name,Type 1,Type 2,Total,HP,Attack,Defense,Sp. Atk,Sp. Def,Speed,Generation,Legendary");
        }
    }

    public static void main(String[] args) {
        PokemonGenSplitterOptions options = PipelineOptionsFactory
                .fromArgs(args)
                .withValidation()
                .as(PokemonGenSplitterOptions.class);

        Pipeline p = Pipeline.create(options);

        PCollection<String> fileContent = p.apply(
                "Read CSV pokemon file", TextIO
                        .read()
                        .from(options.getInputFile())
        );

        PCollection<String> headerlessContent = fileContent.apply(
                "Remove Header from CSV", Filter.by(new headersFilter())
        );

        PCollection<Pokemon> pokemons = headerlessContent.apply(
                "Transform in serializable class", MapElements.via(new PokemonFactory())
        );

        pokemons.apply(
                "Write to Firestore", ParDo.of(new FirestoreOutput<Pokemon>())
        );

        p.run();
    }
}
