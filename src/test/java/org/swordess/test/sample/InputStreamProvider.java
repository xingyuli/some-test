package org.swordess.test.sample;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

public class InputStreamProvider {

	private static final Logger DEFAULT_LOG = Logger.getLogger("input-stream-provider");
	
	private Logger log;
	
	private final String path;
	
	/**
	 * Constructs a stream provided with the given file path.
	 * 
	 * @param path
	 *            path of the file, should not be null or empty string
	 * @throws IllegalArgumentException
	 *             if the path is not valid
	 */
	public InputStreamProvider(String path) throws FileNotFoundException {
		if (StringUtils.isBlank(path)) {
			throw new IllegalArgumentException("path should be non-empty string");
		}
		if (!new File(path).exists()) {
			throw new FileNotFoundException(path);
		}

		this.path = path;
		this.log = DEFAULT_LOG;
	}
	
	/**
	 * Consume the stream which provided by this InputStreamProvider with the
	 * specified user(client).
	 * <p>
	 * NOTE:
	 * <ul>
	 * <li>
	 * This method closes the stream implicitly which means the user(client) can
	 * leave the stream open safely in its code.</li>
	 * <li>
	 * This method handles correctly even if the user(client) choose to close
	 * the stream by itself.</li>
	 * </ul>
	 * 
	 * @param inUser
	 *            the user(client) who want to use the stream, should not be
	 *            null
	 */
	public void usedBy(InputStreamUser inUser) {
		InputStream in = null;
		try {
			log.log(Level.INFO, "opening InputStream for " + path + " ...");
			in = new FileInputStream(path);
			inUser.use(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					log.log(Level.INFO, "closing InputStream ...");
					in.close();
					log.log(Level.INFO, "InputStream closed");
				} catch (IOException e) {
					log.log(Level.WARNING, "cannot close InputStream", e);
				}
			}
		}
	}
	
	/**
	 * Consume the stream which provided by this InputStreamProvider with the
	 * specified users(clients). The users(clients) will be used sequentially
	 * according to the iteration order.
	 * 
	 * @see #usedBy(Collection)
	 * 
	 * @param inUsers
	 *            the users(clients) who want to use the stream, should not be
	 *            null
	 */
	public void usedBy(Collection<? extends InputStreamUser> inUsers) {
		BufferedInputStream in  = null;
		try {
			log.log(Level.INFO, "opening BufferedInputStream for " + path + " ...");
			in = new BufferedInputStream(new FileInputStream(path));
			for (InputStreamUser inUser : inUsers) {
				in.mark(Integer.MAX_VALUE);
				inUser.use(in);
				in.reset();
				log.log(Level.INFO, "stream has been reset");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					log.log(Level.INFO, "closing BufferedInputStream ...");
					in.close();
					log.log(Level.INFO, "BufferedInputStream closed");
				} catch (IOException e) {
					log.log(Level.WARNING, "cannot close BufferedInputStream", e);
				}
			}
		}
	}
	
	public void setLogger(Logger log) {
		this.log = log;
	}
	
	public String getPath() {
		return path;
	}
	
	/**
	 * Indicates a client to use a certain InputStream. This client has no
	 * responsibility to close the stream passed in as it will always be closed
	 * by {@link InputStreamProvider}. 
	 */
	public static interface InputStreamUser {

		/**
		 * Specify how to use the stream passed in.
		 * 
		 * @param in
		 * @throws IOException
		 */
		public void use(InputStream in) throws IOException;
		
	}
	
}
