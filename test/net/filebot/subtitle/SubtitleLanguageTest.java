package net.filebot.subtitle;

import static org.junit.Assert.*;
import java.io.File;
import java.util.Locale;
import net.filebot.Language;
import net.filebot.media.MediaDetection;
import org.junit.Test;

public class SubtitleLanguageTest {

	@Test
	public void testSubtitleLanguageTag() {
		// Standard languages
		File f1 = new File("Avatar.2009.en.srt");
		Locale l1 = MediaDetection.guessLanguageFromSuffix(f1);
		assertNotNull(l1);
		assertEquals("eng", Language.getLanguage(l1).getISO3());

		File f2 = new File("Avatar.2009.spa.srt");
		Locale l2 = MediaDetection.guessLanguageFromSuffix(f2);
		assertNotNull(l2);
		assertEquals("spa", Language.getLanguage(l2).getISO3());

		File f3 = new File("Avatar.2009.fra.forced.srt");
		Locale l3 = MediaDetection.guessLanguageFromSuffix(f3);
		assertNotNull(l3);
		assertEquals("fra", Language.getLanguage(l3).getISO3());

		// Variants for forced and SDH across other ISO codes
		File f4 = new File("Avatar.2009.eng.forced.srt");
		Locale l4 = MediaDetection.guessLanguageFromSuffix(f4);
		assertNotNull(l4);
		assertEquals("eng", Language.getLanguage(l4).getISO3());

		File f5 = new File("Avatar.2009.eng.SDH.srt");
		Locale l5 = MediaDetection.guessLanguageFromSuffix(f5);
		assertNotNull(l5);
		assertEquals("eng", Language.getLanguage(l5).getISO3());

		File f6 = new File("Movie.2024.deu.sdh.srt");
		Locale l6 = MediaDetection.guessLanguageFromSuffix(f6);
		assertNotNull(l6);
		assertEquals("deu", Language.getLanguage(l6).getISO3());

		File f7 = new File("Movie.2024.ita.forced.srt");
		Locale l7 = MediaDetection.guessLanguageFromSuffix(f7);
		assertNotNull(l7);
		assertEquals("ita", Language.getLanguage(l7).getISO3());

		File f8 = new File("Movie.2024.jpn.srt");
		Locale l8 = MediaDetection.guessLanguageFromSuffix(f8);
		assertNotNull(l8);
		assertEquals("jpn", Language.getLanguage(l8).getISO3());
	}
}
