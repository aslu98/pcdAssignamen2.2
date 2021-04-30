package part3;

import io.reactivex.rxjava3.core.BackpressureStrategy;
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

    public Master(final View view){
        this.view = view;

    }

    public void started(Input input){
        this.subscription = Observable.just(input)
                            .filter(in -> in.getDir().isDirectory())
                            .flatMap(in -> getExploreObs(in.getDir()))
                            .observeOn(Schedulers.io())
                            .map(PDDocument::load)
                            .filter(d -> d.getCurrentAccessPermission().canExtractContent())
                            .flatMap(this::getStrippers)
                            .map(pair -> pair.getKey().getText(pair.getValue()))
                            .observeOn(Schedulers.computation())
                            .map(chunk -> Arrays.stream(chunk.split(REGEX)).collect(Collectors.toList()))
                            .flatMap(Observable::fromIterable)
                            .toFlowable(BackpressureStrategy.LATEST)
                            .scan(new ConcurrentHashMap<String, Integer>(), (acc, word) ->
                                {
                                    acc.merge(word, 1, Integer::sum);
                                    return acc;
                                })
                            .sample(100, TimeUnit.MILLISECONDS)
                            .filter(m -> !m.isEmpty())
                            .map(m -> m.entrySet().stream()
                                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                    .limit(input.getNumMostFreqWords())
                                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)))
                            .subscribe(view::update);
    }

    public void stopped(){
        this.subscription.dispose();
    }

    private Observable<File> getExploreObs(File inputFile){
        return Observable.fromArray(inputFile.listFiles())
                .flatMap(f -> {
                    if (f.isDirectory()){
                        return getExploreObs(f);
                    } else {
                        return Observable.just(f);
                    }
                })
                .filter(f -> f.getName().endsWith(".pdf"));
    }

    private Observable<Pair<PDFTextStripper, PDDocument>> getStrippers(PDDocument doc) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        List<Pair<PDFTextStripper, PDDocument>> strippers = new LinkedList<>();
        int nPages = doc.getNumberOfPages();
        for (int i = 0; i < nPages; i++) {
            stripper.setStartPage(i);
            stripper.setEndPage(i);
            strippers.add(Pair.of(stripper, doc));
        }
        return Observable.fromIterable(strippers);
    }

    private static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + " ] " + msg);
    }

}
