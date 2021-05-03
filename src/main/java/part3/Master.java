package part3;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Master implements InputListener{

    private final static String REGEX = "[\\x{201D}\\x{201C}\\s'\", ?.@;:!-]+";
    private final View view;
    private Disposable subscription;
    private int words;
    private int docs;

    public Master(final View view){
        this.view = view;
    }

    public void started(Input input){
        words = 0;
        docs =  0;

        Observable<Pair<Map<String, Integer>, Pair<Integer, Integer>>> obs = Observable.just(input)
                .observeOn(Schedulers.single())
                .filter(in -> in.getDir().isDirectory())
                .flatMap(in -> Observable.fromArray(in.getDir().listFiles()))
                .flatMap(this::exploreDir)
                .filter(f -> f.getName().endsWith(".pdf"))

                .observeOn(Schedulers.io())
                .map(PDDocument::load)
                .doOnNext(w -> docs += 1)
                .filter(d -> d.getCurrentAccessPermission().canExtractContent())
                .flatMap(this::getStrippers)
                .map(pair -> Pair.of(pair.getLeft().getText(pair.getRight().getLeft()), pair.getRight()))
                .doOnNext(pair -> { if (pair.getRight().getRight()) { pair.getRight().getLeft().close();}})
                .map(Pair::getKey)

                .observeOn(Schedulers.computation())
                .map(chunk -> Arrays.stream(chunk.split(REGEX)).collect(Collectors.toList()))
                .flatMap(Observable::fromIterable)
                .filter(word -> !input.getWordsToDiscard().contains(word))
                .doOnNext(w -> words += 1)
                .scan(new ConcurrentHashMap<String, Integer>(), (acc, word) ->
                    {
                        acc.merge(word, 1, Integer::sum);
                        return acc;
                    })

                .buffer(100, TimeUnit.MILLISECONDS)
                .filter(l -> !l.isEmpty())
                .map(list -> list.get(list.size() - 1))
                .map(m -> m.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(input.getNumMostFreqWords())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)))
                .map(m -> Pair.of(m, Pair.of(docs, words)));

        this.subscription = obs.subscribe(view::update);


    }

    public void stopped(){
        this.subscription.dispose();
    }

    private Observable<File> exploreDir(File f){
        return f.isDirectory() ? Observable.fromArray(f.listFiles()).flatMap(this::exploreDir) : Observable.just(f);
    }

    private Observable<Pair<PDFTextStripper, Pair<PDDocument, Boolean>>> getStrippers(PDDocument doc) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        List<Pair<PDFTextStripper, Pair<PDDocument, Boolean>>> strippers = new LinkedList<>();
        boolean lastStripper;
        int nPages = doc.getNumberOfPages();
        for (int i = 0; i < nPages; i++) {
            stripper.setStartPage(i);
            stripper.setEndPage(i);
            lastStripper = i == nPages - 1;
            strippers.add(Pair.of(stripper, Pair.of(doc, lastStripper)));
        }
        return Observable.fromIterable(strippers);
    }
}
