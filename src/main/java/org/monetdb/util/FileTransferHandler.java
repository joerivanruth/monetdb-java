package org.monetdb.util;

import org.monetdb.jdbc.MonetConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Sample implement of ON CLIENT handling
 *
 * Can be registered with {@link MonetConnection#setUploadHandler(MonetConnection.UploadHandler)}
 * and {@link MonetConnection#setDownloadHandler(MonetConnection.DownloadHandler)}.
 * Implements uploads and downloads by reading and writing files on the file system.
 */
public class FileTransferHandler implements MonetConnection.UploadHandler, MonetConnection.DownloadHandler {
	private final Path root;
	private final boolean utf8Encoded;

	/**
	 * Create a new FileTransferHandler which serves the given directory.
	 *
	 * @param dir directory to read and write files from
	 * @param utf8Encoded set this to true if all files in the directory are known to be utf-8 encoded.
	 */
	public FileTransferHandler(Path dir, boolean utf8Encoded) {
		root = dir.toAbsolutePath().normalize();
		this.utf8Encoded = utf8Encoded;
	}

	/**
	 * Create a new FileTransferHandler which serves the given directory.
	 *
	 * @param dir directory to read and write files from
	 * @param utf8Encoded set this to true if all files in the directory are known to be utf-8 encoded.
	 */
	public FileTransferHandler(String dir, boolean utf8Encoded) {
		this(FileSystems.getDefault().getPath(dir), utf8Encoded);
	}

	public void handleUpload(MonetConnection.Upload handle, String name, boolean textMode, int offset) throws IOException {
		Path path = root.resolve(name).normalize();
		if (!path.startsWith(root)) {
			handle.sendError("File is not in upload directory");
			return;
		}
		if (!Files.isReadable(path)) {
			handle.sendError("Cannot read " + name);
			return;
		}
		if (textMode && (offset > 1 || !utf8Encoded)) {
			Charset encoding = utf8Encoded ? StandardCharsets.UTF_8 : Charset.defaultCharset();
			BufferedReader reader = Files.newBufferedReader(path, encoding);
			handle.uploadFrom(reader, offset);
		} else {
			handle.uploadFrom(Files.newInputStream(path));
		}
	}

	public void handleDownload(MonetConnection.Download handle, String name, boolean textMode) throws IOException {
		Path path = root.resolve(name).normalize();
		if (!path.startsWith(root)) {
			handle.sendError("File is not in upload directory");
			return;
		}
		if (!Files.exists(path)) {
			handle.sendError("File exists: " + name);
			return;
		}
		OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW);
	}
}