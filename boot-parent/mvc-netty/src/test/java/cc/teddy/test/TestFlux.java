package cc.teddy.test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class TestFlux {

	protected void print(Object obj) {
		System.out.print(obj);
		;
	}

	protected void println(Object obj) {
		System.out.println(obj);
		;
	}

	@Test
	public void testJust() {
		Flux.just("Hello", "World").subscribe(this::println);
	}

	@Test
	public void testFromArray() {
		Flux.fromArray(new Integer[] { 1, 2, 3 }).subscribe(this::print);
	}

	@Test
	public void testEmpty() {
		Flux.empty().subscribe(this::print);
	}

	@Test
	public void testRange() {
		Flux.range(2, 10).subscribe(this::println);
	}

	@Test
	public void testInterval() throws Exception {
		AtomicInteger count = new AtomicInteger(0);
		AtomicBoolean startLock = new AtomicBoolean(false);
		Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).subscribe(l -> {
			if (!startLock.get()) {
				synchronized (startLock) {
					if (!startLock.get()) {
						System.out.println("Not start yet!");
						return;
					}
				}
			}
			this.println(l);
			count.incrementAndGet();
			if (count.intValue() > 10) {
				synchronized (startLock) {
					startLock.notifyAll();
				}
			}
		});
		Thread.sleep(2000);
		synchronized (startLock) {
			startLock.set(true);
			startLock.wait();
		}
		println("----------- End ---------");
	}

	@Test
	public void testGenerate() {
		Flux.generate(sink -> {
			sink.next("Hello");
			sink.complete();
		}).subscribe(this::println);
		Random random = new Random();
		Flux.generate(ArrayList::new, (list, sink) -> {
			int value = random.nextInt(100);
			list.add(value);
			sink.next(value);
			if (list.size() >= 10) {
				sink.complete();
			}
			return list;
		}).subscribe(this::println);
	}
	
	@Test
	public void testCreate() {
		Flux.create(sink -> {
			for (int i = 0; i < 10; i++) {
				sink.next(i);
			}
			sink.complete();
		}).subscribe(this::println);
	}
	
	@Test
	public void testBuffer() {
		Flux.range(1, 100).bufferUntil(i -> i % 3 == 0).buffer(20).subscribe(this::println);
	}
	
	@Test
	public void testFilter() {
		Flux.range(1, 100).filter(i -> i % 3 == 0).subscribe(this::println);
	}
	
	@Test
	public void testWindow() {
		Flux.range(1, 100).window(20).subscribe(flux -> flux.subscribe(this::println));
	}
	
	@Test
	public void testZip() {
		Flux.just("a", "b").zipWith(Flux.just("d", "e")).subscribe(this::println);
	}
	
	@Test
	public void testTake() {
		Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(this::println);
	}
	
	@Test
	public void testReduce() {
		Flux.range(2, 99).reduce(1, (x, y) -> x + y).subscribe(this::println);
	}
	
	@Test
	public void testMerge() {
		Flux.merge(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
			.toStream().forEach(this::println);
		this.println("------------------------------------------------");
		Flux.mergeSequential(Flux.interval(Duration.ofMillis(0), Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
			.toStream().forEach(this::println);
	}
	
	@Test
	public void testFlatMap() {
		Flux.just(5, 10).flatMap(x -> Flux.interval(Duration.ofMillis(x * 10), Duration.ofMillis(100)).take(x))
		    .toStream()
		    .forEach(this::println);
		this.println("------------------------------------------------");
		Flux.just(5, 10).flatMapSequential(x -> Flux.interval(Duration.ofMillis(x * 10), Duration.ofMillis(100)).take(x))
		    .toStream()
		    .forEach(this::println);
	}
	
	@Test
	public void testConcatMap() {
		Flux.just(5, 10).concatMap(x -> Flux.interval(Duration.ofMillis(x * 10), Duration.ofMillis(100)).take(x))
		    .toStream()
		    .forEach(this::println);
	}
	
	@Test
	public void testCombineLatest() {
		Flux.combineLatest(Arrays::toString, Flux.interval(Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(50), Duration.ofMillis(100)).take(5))
		    .toStream().forEach(this::println);
	}
	
	@Test
	public void testScheduler() {
		Flux.create(sink -> {
			sink.next(Thread.currentThread().getName()).complete();
		})
		.publishOn(Schedulers.single())
		.map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
		.publishOn(Schedulers.elastic())
		.map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
		.subscribeOn(Schedulers.parallel())
		.toStream()
		.forEach(this::println);
	}
	
	@Test
	public void test冷与热序列() throws Exception {
		Flux<Long> source1 = Flux.interval(Duration.ofMillis(1000)).take(10);
		Flux<Long> source2 = Flux.interval(Duration.ofMillis(1000)).take(20).publish().autoConnect();
		source1.subscribe();
		source2.subscribe();
		Thread.sleep(5000);
		source1.toStream().forEach(this::println);
		this.println("------------------------------------------------");
		source2.toStream().forEach(this::println);
	}
}
