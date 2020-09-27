package Model;

import org.apache.beam.sdk.options.PipelineOptions;

public interface PokemonGenSplitterOptions extends PipelineOptions {
    void setProjectId(String projectId);
    String getProjectId();

    void setRootDocument(String rootDocument);
    String getRootDocument();

    void setInputFile(String inputFile);
    String getInputFile();
}
