package Services;

import Model.PokemonGenSplitterOptions;
import POJO.Pokemon;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.transforms.DoFn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirestoreOutput<In> extends DoFn<In, Void> {
    private static final long serialVersionUID = 2L;
    private transient List<Pokemon> mutations;
    private transient Firestore db;

    public FirestoreOutput(){
    }

    @StartBundle
    public void setupBufferedMutator(StartBundleContext startBundleContext) throws IOException {
        this.mutations = new ArrayList<Pokemon>();
    }

    @ProcessElement
    public void processElement(ProcessContext context) throws Exception {
        Pokemon mutation = (Pokemon) context.element();
        mutations.add(mutation);

        flushBatch(context.getPipelineOptions());
    }

    private void flushBatch(PipelineOptions pipelineOptions) throws Exception {
        PokemonGenSplitterOptions options = pipelineOptions.as(PokemonGenSplitterOptions.class);

        FirestoreOptions firestoreOptions = FirestoreOptions
                .getDefaultInstance().toBuilder()
                .setTimestampsInSnapshotsEnabled(true)
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setProjectId(options.getProjectId())
                .build();

        db = firestoreOptions.getService();

        WriteBatch batch = db.batch();
        for (Pokemon doc : mutations) {
            DocumentReference docRef = db.collection(options.getRootDocument()).document(doc.getName());
            batch.set(docRef, doc);
        }

        ApiFuture<List<WriteResult>> wr = batch.commit();
    }
    @FinishBundle
    public synchronized void finishBundle(FinishBundleContext context) throws Exception {
        flushBatch(context.getPipelineOptions());
        db.close();
    }

}
