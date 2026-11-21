package net.filebot;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import org.junit.Test;

import com.sun.jna.Platform;

public class MainTest {

	@Test
	public void testUmaskAndDirectoryPermissions() throws IOException {
		Main.initializePosixEnvironment();

		if (!Platform.isWindows()) {
			Path parent = Files.createTempDirectory("filebot_umask_test");
			try {
				Path subDir = parent.resolve("test_sub_dir");
				Files.createDirectories(subDir);

				Set<PosixFilePermission> perms = Files.getPosixFilePermissions(subDir);
				assertTrue("Directory created by app should have group write permission", perms.contains(PosixFilePermission.GROUP_WRITE));
				assertTrue("Directory created by app should have group read permission", perms.contains(PosixFilePermission.GROUP_READ));
				assertTrue("Directory created by app should have group execute permission", perms.contains(PosixFilePermission.GROUP_EXECUTE));

				File fileObjDir = new File(parent.toFile(), "test_mkdirs_dir");
				assertTrue(fileObjDir.mkdirs());
				Set<PosixFilePermission> fileDirPerms = Files.getPosixFilePermissions(fileObjDir.toPath());
				assertTrue("Directory created via File.mkdirs should have group write permission", fileDirPerms.contains(PosixFilePermission.GROUP_WRITE));

				Path file = Files.createFile(parent.resolve("test_file.txt"));
				Set<PosixFilePermission> filePerms = Files.getPosixFilePermissions(file);
				assertTrue("File created by app should have group write permission", filePerms.contains(PosixFilePermission.GROUP_WRITE));
				assertTrue("File created by app should have group read permission", filePerms.contains(PosixFilePermission.GROUP_READ));
			} finally {
				deleteRecursively(parent.toFile());
			}
		}
	}

	@Test
	public void testLauncherScriptContainsUmask() throws IOException {
		File script = new File("bin/filebot.sh");
		assertTrue("bin/filebot.sh should exist", script.isFile());
		String content = Files.readString(script.toPath());
		assertTrue("bin/filebot.sh should configure umask 002", content.contains("umask 002"));
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

