package net.filebot.ui.rename;

import static org.junit.Assert.*;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.filebot.StandardRenameAction;

public class RenameActionTest {

	private File tempDir;

	@Before
	public void setUp() throws IOException {
		tempDir = Files.createTempDirectory("filebot_rename_test").toFile();
	}

	@After
	public void tearDown() throws IOException {
		if (tempDir != null && tempDir.exists()) {
			for (File f : tempDir.listFiles()) {
				f.delete();
			}
			tempDir.delete();
		}
	}

	private static class TestableRenameAction extends RenameAction {

		String fatalIssuesResponse = "Continue";
		String overwriteResponse = "Overwrite";
		List<String> lastFatalIssues;
		List<String> lastExistingIssues;
		boolean fatalDialogCalled = false;
		boolean overwriteDialogCalled = false;

		public TestableRenameAction(RenameModel model) {
			super(model);
		}

		@Override
		protected String showFatalIssuesDialog(Window parent, List<String> issues) {
			fatalDialogCalled = true;
			lastFatalIssues = new ArrayList<String>(issues);
			return fatalIssuesResponse;
		}

		@Override
		protected String showOverwriteDialog(Window parent, List<String> issues, boolean canSkip) {
			overwriteDialogCalled = true;
			lastExistingIssues = new ArrayList<String>(issues);
			return overwriteResponse;
		}
	}

	@Test
	public void testCheckRenamePlanNonExistingFile() throws Exception {
		File src = new File(tempDir, "source.txt");
		File dst = new File(tempDir, "dest.txt");
		Files.writeString(src.toPath(), "source content");

		TestableRenameAction action = new TestableRenameAction(new RenameModel());
		List<Entry<File, File>> plan = Collections.singletonList(new SimpleEntry<File, File>(src, dst));

		Map<File, File> map = action.checkRenamePlan(plan, null);
		assertFalse(action.overwriteDialogCalled);
		assertFalse(action.fatalDialogCalled);
		assertEquals(1, map.size());
		assertEquals(dst, map.get(src));
	}

	@Test
	public void testCheckRenamePlanExistingFileOverwrite() throws Exception {
		File src = new File(tempDir, "source.txt");
		File dst = new File(tempDir, "dest.txt");
		Files.writeString(src.toPath(), "source content");
		Files.writeString(dst.toPath(), "old content");

		TestableRenameAction action = new TestableRenameAction(new RenameModel());
		action.overwriteResponse = "Overwrite";

		List<Entry<File, File>> plan = Collections.singletonList(new SimpleEntry<File, File>(src, dst));
		Map<File, File> map = action.checkRenamePlan(plan, null);

		assertTrue(action.overwriteDialogCalled);
		assertEquals(1, action.lastExistingIssues.size());
		assertTrue(action.lastExistingIssues.get(0).contains("File already exists"));
		assertEquals(1, map.size());
		assertEquals(dst, map.get(src));
	}

	@Test
	public void testCheckRenamePlanExistingFileCancel() throws Exception {
		File src = new File(tempDir, "source.txt");
		File dst = new File(tempDir, "dest.txt");
		Files.writeString(src.toPath(), "source content");
		Files.writeString(dst.toPath(), "old content");

		TestableRenameAction action = new TestableRenameAction(new RenameModel());
		action.overwriteResponse = "Cancel";

		List<Entry<File, File>> plan = Collections.singletonList(new SimpleEntry<File, File>(src, dst));
		Map<File, File> map = action.checkRenamePlan(plan, null);

		assertTrue(action.overwriteDialogCalled);
		assertTrue(map.isEmpty());
	}

	@Test
	public void testCheckRenamePlanExistingFileSkip() throws Exception {
		File src1 = new File(tempDir, "source1.txt");
		File dst1 = new File(tempDir, "dest1.txt");
		File src2 = new File(tempDir, "source2.txt");
		File dst2 = new File(tempDir, "dest2.txt");

		Files.writeString(src1.toPath(), "source1 content");
		Files.writeString(src2.toPath(), "source2 content");
		Files.writeString(dst1.toPath(), "existing content"); // dst1 exists, dst2 does not

		TestableRenameAction action = new TestableRenameAction(new RenameModel());
		action.overwriteResponse = "Skip";

		List<Entry<File, File>> plan = new ArrayList<Entry<File, File>>();
		plan.add(new SimpleEntry<File, File>(src1, dst1));
		plan.add(new SimpleEntry<File, File>(src2, dst2));

		Map<File, File> map = action.checkRenamePlan(plan, null);

		assertTrue(action.overwriteDialogCalled);
		assertEquals(1, map.size());
		assertNull(map.get(src1)); // src1 skipped
		assertEquals(dst2, map.get(src2)); // src2 retained
	}

	@Test
	public void testRenameJobOverwritesExistingFile() throws Exception {
		File src = new File(tempDir, "source.txt");
		File dst = new File(tempDir, "dest.txt");
		Files.writeString(src.toPath(), "new content");
		Files.writeString(dst.toPath(), "old content");

		TestableRenameAction action = new TestableRenameAction(new RenameModel());
		RenameAction.RenameJob job = action.new RenameJob(Collections.singletonMap(src, dst), StandardRenameAction.MOVE);

		Map<File, File> result = job.doInBackground();
		assertEquals(1, result.size());
		assertTrue(dst.exists());
		assertFalse(src.exists());
		assertEquals("new content", Files.readString(dst.toPath()));
	}
}

