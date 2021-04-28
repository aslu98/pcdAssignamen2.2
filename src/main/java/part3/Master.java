package part3;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
                            .flatMap(d -> getChunkObs(d))
                            .map(chunk -> Arrays.stream(chunk.split(REGEX)).collect(Collectors.toList()))
                            .scan(new HashMap<String, Integer>(), (acc, current) -> {
                                for (String word : current) {
                                    Integer nf = acc.get(word);
                                    if (nf == null) {
                                        acc.put(word, 1);
                                    } else {
                                        acc.put(word, nf + 1);
                                    }
                                }
                                return acc;
                            })
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

    private Observable<String> getChunkObs(PDDocument doc) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        List<String> chunks = new LinkedList<>();
        int nPages = doc.getNumberOfPages();
        for (int i = 0; i < nPages; i++) {
            stripper.setStartPage(i);
            stripper.setEndPage(i);
            String chunk = stripper.getText(doc);
            chunks.add(chunk);
        }
        doc.close();
        return Observable.fromIterable(chunks);
    }

    private static void log(String msg) {
        System.out.println("[ " + Thread.currentThread().getName() + " ] " + msg);
    }

}
