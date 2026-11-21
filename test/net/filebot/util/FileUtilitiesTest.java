
package net.filebot.util;


import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;

import org.junit.Test;


public class FileUtilitiesTest {

	@Test
	public void hasExtension() {
		assertTrue(FileUtilities.hasExtension("abc.txt", "txt"));
		assertFalse(FileUtilities.hasExtension(".hidden", "txt"));
	}


	@Test
	public void getExtension() {
		assertEquals("txt", FileUtilities.getExtension("abc.txt"));
		assertEquals("out", FileUtilities.getExtension("a.out"));
		assertEquals(null, FileUtilities.getExtension(".hidden"));
		assertEquals(null, FileUtilities.getExtension("a."));

		assertEquals("r00", FileUtilities.getExtension("archive.r00"));
		assertEquals(null, FileUtilities.getExtension("archive.r??"));
		assertEquals(null, FileUtilities.getExtension("archive.invalid extension"));
	}


	@Test
	public void getNameWithoutExtension() {
		assertEquals("abc", FileUtilities.getNameWithoutExtension("abc.txt"));
		assertEquals("a", FileUtilities.getNameWithoutExtension("a.out"));
		assertEquals(".hidden", FileUtilities.getNameWithoutExtension(".hidden"));
		assertEquals("a.", FileUtilities.getNameWithoutExtension("a."));

		assertEquals("archive", FileUtilities.getNameWithoutExtension("archive.r00"));
		assertEquals("archive.r??", FileUtilities.getNameWithoutExtension("archive.r??"));
		assertEquals("archive.invalid extension", FileUtilities.getNameWithoutExtension("archive.invalid extension"));
	}


	@Test
	public void testMoveRenameSets644From600() throws Exception {
		if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
			Path tempDir = Files.createTempDirectory("filebot_test_move_perm");
			try {
				Path src = tempDir.resolve("src.mkv");
				Path dst = tempDir.resolve("dst.mkv");
				Files.writeString(src, "test content");
				// Force 600 permissions on source
				Files.setPosixFilePermissions(src, PosixFilePermissions.fromString("rw-------"));
				assertEquals("rw-------", PosixFilePermissions.toString(Files.getPosixFilePermissions(src)));

				File moved = FileUtilities.moveRename(src.toFile(), dst.toFile());
				assertTrue(moved.exists());
				assertEquals("rw-r--r--", PosixFilePermissions.toString(Files.getPosixFilePermissions(moved.toPath())));
			} finally {
				deleteRecursively(tempDir.toFile());
			}
		}
	}


	@Test
	public void testCopyAsSets644From600() throws Exception {
		if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
			Path tempDir = Files.createTempDirectory("filebot_test_copy_perm");
			try {
				Path src = tempDir.resolve("src.mkv");
				Path dst = tempDir.resolve("dst.mkv");
				Files.writeString(src, "test content");
				// Force 600 permissions on source
				Files.setPosixFilePermissions(src, PosixFilePermissions.fromString("rw-------"));

				File copied = FileUtilities.copyAs(src.toFile(), dst.toFile());
				assertTrue(copied.exists());
				assertEquals("rw-r--r--", PosixFilePermissions.toString(Files.getPosixFilePermissions(copied.toPath())));
			} finally {
				deleteRecursively(tempDir.toFile());
			}
		}
	}


	@Test
	public void testResolveDestinationCreates755Directories() throws Exception {
		if (FileSystems.getDefault().supportedFileAttributeViews().contains("posix")) {
			Path tempDir = Files.createTempDirectory("filebot_test_dirs_perm");
			try {
				Path nestedDir = tempDir.resolve("level1").resolve("level2");
				Path src = tempDir.resolve("src.mkv");
				Files.writeString(src, "test");
				Path dst = nestedDir.resolve("dst.mkv");

				File resolved = FileUtilities.resolveDestination(src.toFile(), dst.toFile());
				assertEquals(dst.toFile(), resolved);
				assertTrue(Files.exists(nestedDir));
				assertEquals("rwxr-xr-x", PosixFilePermissions.toString(Files.getPosixFilePermissions(tempDir.resolve("level1"))));
				assertEquals("rwxr-xr-x", PosixFilePermissions.toString(Files.getPosixFilePermissions(nestedDir)));
			} finally {
				deleteRecursively(tempDir.toFile());
			}
		}
	}


	private void deleteRecursively(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children != null) {
				for (File child : children) {
					deleteRecursively(child);
				}
			}
		}
		file.delete();
	}

}
