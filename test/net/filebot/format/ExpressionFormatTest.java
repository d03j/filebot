
package net.filebot.format;

import static org.junit.Assert.*;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import java.util.Locale;

import org.junit.Test;

import net.filebot.WebServices;
import net.filebot.web.Episode;
import net.filebot.web.Movie;
import net.filebot.web.SeriesInfo;
import net.filebot.web.SimpleDate;
import net.filebot.web.SortOrder;

public class ExpressionFormatTest {

	@Test
	public void compile() throws Exception {
		ExpressionFormat format = new TestScriptFormat("");

		Object[] expression = format.compile("name: {name}, number: {number}");

		assertTrue(expression[0] instanceof String);
		assertTrue(expression[1] instanceof CompiledScript);
		assertTrue(expression[2] instanceof String);
		assertTrue(expression[3] instanceof CompiledScript);
	}

	@Test
	public void format() throws Exception {
		assertEquals("X5-452", new TestScriptFormat("X5-{value}").format("452"));

		// padding
		assertEquals("[007]", new TestScriptFormat("[{value.pad(3)}]").format("7"));
		assertEquals("[xx7]", new TestScriptFormat("[{value.pad(3, 'x')}]").format("7"));

		// case
		assertEquals("ALL_CAPS", new TestScriptFormat("{value.upper()}").format("all_caps"));
		assertEquals("lower_case", new TestScriptFormat("{value.lower()}").format("LOWER_CASE"));

		// normalize
		assertEquals("Doctor_Who", new TestScriptFormat("{value.space('_')}").format("Doctor Who"));
		assertEquals("The Day A New Demon Was Born", new TestScriptFormat("{value.upperInitial()}").format("The Day a new Demon was born"));
		assertEquals("Gundam Seed", new TestScriptFormat("{value.lowerTrail()}").format("Gundam SEED"));

		// substring
		assertEquals("first", new TestScriptFormat("{value.before(/[^a-z]/)}").format("first|second"));
		assertEquals("second", new TestScriptFormat("{value.after(/[^a-z]/)}").format("first|second"));

		// replace trailing braces
		assertEquals("The IT Crowd", new TestScriptFormat("{value.replaceTrailingBrackets()}").format("The IT Crowd (UK)"));

		// replace part
		assertEquals("Today Is the Day, Part 1", new TestScriptFormat("{value.replacePart(', Part $1')}").format("Today Is the Day (1)"));
		assertEquals("Today Is the Day, Part 1", new TestScriptFormat("{value.replacePart(', Part $1')}").format("Today Is the Day: part 1"));

		// choice
		assertEquals("not to be", new TestScriptFormat("{value ? 'to be' : 'not to be'}").format(false));
		assertEquals("default", new TestScriptFormat("{value ?: 'default'}").format(false));
	}

	@Test
	public void closures() throws Exception {
		assertEquals("[ant, cat]", new TestScriptFormat("{['ant', 'buffalo', 'cat', 'dinosaur'].findAll{ it.size() <= 3 }}").format(null));
	}

	@Test
	public void illegalSyntax() throws Exception {
		try {
			// will throw exception
			new TestScriptFormat("{value.}");
			// exception must be thrown
			fail("exception expected");
		} catch (ScriptException e) {
			// check message
			assertEquals("SyntaxError: unexpected token: .", e.getMessage());
		}
	}

	@Test
	public void illegalClosingBracket() throws Exception {
		try {
			// will throw exception
			new TestScriptFormat("{{ it -> 'value' }}}");
			// exception must be thrown
			fail("exception expected");
		} catch (ScriptException e) {
			// check message
			assertEquals("SyntaxError: unexpected token: }", e.getMessage());
		}
	}

	@Test
	public void illegalBinding() throws Exception {
		TestScriptFormat format = new TestScriptFormat("{xyz}");
		format.format(new SimpleBindings());

		// check message
		assertEquals("Binding \"xyz\": undefined", format.caughtScriptException().getMessage());
	}

	@Test
	public void illegalProperty() throws Exception {
		TestScriptFormat format = new TestScriptFormat("{value.xyz}");
		format.format("test");

		// check message
		assertEquals("Binding \"xyz\": undefined", format.caughtScriptException().getMessage());
	}

	@Test
	public void episodeBindings() throws Exception {
		SeriesInfo tmdbSeries = new SeriesInfo(WebServices.TheMovieDB_TV, SortOrder.Airdate, Locale.ENGLISH, 76662);
		tmdbSeries.setName("Shakespeare & Hathaway: Private Investigators");
		tmdbSeries.setStartDate(new SimpleDate(2018, 2, 26));

		Episode episode = new Episode("Shakespeare & Hathaway: Private Investigators", 6, 3, "Nothing Else Holds Fashion", null, null, null, tmdbSeries);

		MediaBindingBean bean = new MediaBindingBean(episode, null);
		ExpressionFormat format = new ExpressionFormat("{n.space('.')}.-.[tmdbid-{id}]");
		assertEquals("Shakespeare.&.Hathaway.Private.Investigators.-.[tmdbid-76662]", format.format(bean));

		ExpressionFormat formatTmdbId = new ExpressionFormat("[tmdbid-{tmdbid}]");
		assertEquals("[tmdbid-76662]", formatTmdbId.format(bean));

		SeriesInfo tvmazeSeries = new SeriesInfo(WebServices.TVmaze, SortOrder.Airdate, Locale.ENGLISH, 34567);
		tvmazeSeries.setName("Sample Show");
		Episode tvmazeEpisode = new Episode("Sample Show", 1, 1, "Pilot", null, null, null, tvmazeSeries);
		MediaBindingBean tvmazeBean = new MediaBindingBean(tvmazeEpisode, null);

		ExpressionFormat formatId = new ExpressionFormat("{n}.[id-{id}]");
		assertEquals("Sample Show.[id-34567]", formatId.format(tvmazeBean));
	}

	@Test
	public void movieBindings() throws Exception {
		Movie movie = new Movie("The Homesman", 2014, -1, 264656);
		MediaBindingBean bean = new MediaBindingBean(movie, null);

		ExpressionFormat formatId = new ExpressionFormat("{n}.({y}).[tmdbid-{id}]");
		assertEquals("The Homesman.(2014).[tmdbid-264656]", formatId.format(bean));

		ExpressionFormat formatTmdbId = new ExpressionFormat("{n}.({y}).[tmdbid-{tmdbid}]");
		assertEquals("The Homesman.(2014).[tmdbid-264656]", formatTmdbId.format(bean));
	}

	protected static class TestScriptFormat extends ExpressionFormat {

		public TestScriptFormat(String format) throws ScriptException {
			super(format);
		}

		@Override
		public Bindings getBindings(Object value) {
			Bindings bindings = new SimpleBindings();

			bindings.put("value", value);

			return bindings;
		}

	}

}
