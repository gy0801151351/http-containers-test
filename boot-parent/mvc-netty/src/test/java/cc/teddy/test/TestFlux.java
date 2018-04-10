package cc.teddy.test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import reactor.core.publisher.Flux;

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

}
