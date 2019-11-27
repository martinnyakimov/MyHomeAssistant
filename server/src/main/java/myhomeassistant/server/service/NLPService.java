package myhomeassistant.server.service;

import opennlp.tools.doccat.*;
import opennlp.tools.lemmatizer.LemmatizerME;
import opennlp.tools.lemmatizer.LemmatizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class NLPService {

    private static String MODELS_DIRECTORY = System.getProperty("user.dir") + "/models";

    static DoccatModel deserializeModel() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(MODELS_DIRECTORY + "/trained-expressions.bin");
        return new DoccatModel(fileInputStream);
    }

    static void trainCategorizerModel() throws IOException {
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(
                new File(MODELS_DIRECTORY + "/expressions.txt"));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[]{new BagOfWordsFeatureGenerator()});

        TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 0);

        DocumentCategorizerME.train("en", sampleStream, params, factory)
                .serialize(new File(MODELS_DIRECTORY + "/trained-expressions.bin"));
    }

    static String detectCategory(DoccatModel model, String[] finalTokens) {
        DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
        double[] probabilitiesOfOutcomes = myCategorizer.categorize(finalTokens);

        return myCategorizer.getBestCategory(probabilitiesOfOutcomes);

    }

    static String[] breakSentences(String data) throws IOException {
        try (InputStream modelIn = new FileInputStream(MODELS_DIRECTORY + "/en-sent.bin")) {
            SentenceDetectorME myCategorizer = new SentenceDetectorME(new SentenceModel(modelIn));
            return myCategorizer.sentDetect(data);
        }
    }

    static String[] tokenizeSentence(String sentence) throws IOException {
        try (InputStream modelIn = new FileInputStream(MODELS_DIRECTORY + "/en-token.bin")) {
            TokenizerME myCategorizer = new TokenizerME(new TokenizerModel(modelIn));
            return myCategorizer.tokenize(sentence);
        }
    }

    // TODO: add option to use lemmatize tokens (add message to use ONLY on powerful hardware)
    static String[] detectPOSTags(String[] tokens) throws IOException {
        try (InputStream modelIn = new FileInputStream(MODELS_DIRECTORY + "/en-pos-maxent.bin")) {
            POSTaggerME myCategorizer = new POSTaggerME(new POSModel(modelIn));
            return myCategorizer.tag(tokens);
        }
    }

    static String[] lemmatizeTokens(String[] tokens, String[] posTags) throws IOException {
        try (InputStream modelIn = new FileInputStream(MODELS_DIRECTORY + "/en-lemmatizer.bin")) {
            LemmatizerME myCategorizer = new LemmatizerME(new LemmatizerModel(modelIn));
            return myCategorizer.lemmatize(tokens, posTags);
        }
    }
}
