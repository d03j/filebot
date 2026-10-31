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
	}
}
