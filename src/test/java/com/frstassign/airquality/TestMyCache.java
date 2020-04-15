package com.frstassign.airquality;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestMyCache {
	private MyCache cache;

	@BeforeEach
	void initCache(){
		this.cache = new MyCache();
	}

	@Test
	void inCacheAfterSearch() {
		Region r = new Region("region1");
		HashMap<String, Double> hm = new HashMap<>();
		hm.put("s",2.0);
		r.setAirQual(hm);
		this.cache.addToCache(r);

		assertEquals(r, this.cache.searchInCache("region1"));
	}

	@Test
	void checkMaxSize(){

		this.cache.addToCache( new Region("r1"));
		this.cache.addToCache( new Region("r2"));
		this.cache.addToCache( new Region("r3"));
		this.cache.addToCache( new Region("r4"));
		this.cache.addToCache( new Region("r5"));

		assertEquals(3, this.cache.getCache().keySet().size());

	}

	@Test
	void checkRemoved(){
		Region r1 = new Region("r1");
		this.cache.addToCache( r1 );
		this.cache.addToCache( new Region("r2"));
		this.cache.addToCache( new Region("r3"));
		this.cache.addToCache( new Region("r4"));

		assertNull(this.cache.searchInCache("r1"));
	}

	@Test
	void sendToEnd(){
		Region r1 = new Region("r1");
		Region r2 = new Region("r2");
		this.cache.addToCache(r1);
		this.cache.addToCache(r2);
		this.cache.addToCache(new Region("r3"));
		this.cache.sendToEnd("r1");	//keep r1 in queue
		this.cache.addToCache(new Region("r4"));

		assertEquals(r1, this.cache.searchInCache("r1"));
		assertNull(this.cache.searchInCache("r2"));
	}

	@Test
	void cacheStatistics(){
		this.cache.addToCache(new Region("r1"));
		this.cache.addToCache(new Region("r3"));
		this.cache.addToCache(new Region("r2"));

		this.cache.searchInCache("r7");
		this.cache.searchInCache("r6");
		this.cache.searchInCache("r2");
		this.cache.searchInCache("r4");

		assertEquals(4, this.cache.getNrequests());
		assertEquals(1, this.cache.getHits());
		assertEquals(3, this.cache.getMisses());

	}

	@Test
	void toStringTest(){
		Map<String, Region> test = new HashMap<>();
		Map<String, Double> aq = new HashMap<>();
		aq.put("aqi", 2.0);

		Region r1 = new Region("test1");
		r1.setAirQual(aq);
		Region r2 = new Region("test2");
		
		test.put("test1", r1);
		test.put("test2", r2);

		this.cache.addToCache(r1);
		this.cache.addToCache(r2);

		assertEquals(this.cache.toString(),test.toString());
		
	}

}
