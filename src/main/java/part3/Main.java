package part3;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		LinkedList<AbstractMap.SimpleEntry<String, Integer>> mostFreq = new LinkedList<>();
		String del = "[\\x{201D}\\x{201C}\\s'\", ?.@;:!-]+";
		AtomicInteger analyzedWords = new AtomicInteger();
		Input input = new Input();

		Observable.just(input)
				.filter(in -> in.getDir().isDirectory())
				.flatMap(in -> getExploreObs(in.getDir()))
				.observeOn(Schedulers.computation())
				.map(PDDocument::load)
				.filter(d -> d.getCurrentAccessPermission().canExtractContent())
				.flatMap(d -> getChunkObs(d))
				.map(chunk -> Arrays.stream(chunk.split(del)).collect(Collectors.toList()))
				.observeOn(Schedulers.single())
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
				.observeOn(Schedulers.computation())
				.filter(m -> !m.isEmpty())
				.map(m -> m.entrySet().stream()
						.sorted(Map.Entry.comparingByValue())
						.limit(input.getNumMostFreqWords())
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)))
				.flatMap(m -> Observable.fromIterable(m.entrySet())
										.map(entry -> entry.getKey() + " -> " + entry.getValue() + " times"))
				.subscribe(Main::log);

		Thread.sleep(100000);
	}

	private static Observable<File> getExploreObs(File inputFile){
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

	private static Observable<String> getChunkObs(PDDocument doc) throws IOException {
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


	static private void log(String msg) {
		System.out.println("[ " + Thread.currentThread().getName() + " ] " + msg);
	}

	/* discoverer ok
	Observable.just(input)
				.filter(in -> in.getDir().isDirectory())
				.flatMap(in -> getExploreObs(in.getDir()))
				.map(f -> f.getPath())
				.subscribe(Main::log);
	 */
}
