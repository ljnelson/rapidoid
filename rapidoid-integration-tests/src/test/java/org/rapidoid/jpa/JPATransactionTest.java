package org.rapidoid.jpa;

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.http.HttpTestCommons;

import java.util.List;

/*
 * #%L
 * rapidoid-integration-tests
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

@Authors("Nikolche Mihajlovski")
@Since("3.0.0")
public class JPATransactionTest extends HttpTestCommons {

	@Test
	public void testBasicCRUD() {
		JPA.bootstrap(path());

		JPA.transaction(this::insertData);

		JPA.transaction(() -> checkData(1));

		int k = 1000;
		multiThreaded(100, 1000, () -> JPA.transaction(this::insertData));

		JPA.transaction(() -> checkData(k + 1));
	}

	private void checkData(int insertions) {
		eq(JPA.getAllEntities().size(), 2 * insertions);

		List<Book> books = JPA.getAll(Book.class);
		books.forEach(book -> eq(book.getTitle(), "book"));

		List<Movie> movies = JPA.getAll(Movie.class);
		movies.forEach(movie -> eq(movie.getTitle(), "movie"));
	}

	private void insertData() {
		Book b1 = new Book("book");
		Movie m1 = new Movie("movie");

		JPA.insert(b1);
		JPA.insert(m1);
	}

}
