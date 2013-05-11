package com.commafeed.frontend.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commafeed.backend.dao.UserSettingsDAO;
import com.commafeed.backend.model.UserSettings;
import com.commafeed.frontend.CommaFeedSession;

/**
 * Replace variables from templates on the fly in dev mode only. In production
 * the substitution is done at build-time.
 * 
 */
public class InternationalizationDevelopmentFilter implements Filter {

	private static Logger log = LoggerFactory
			.getLogger(InternationalizationDevelopmentFilter.class);

	@Inject
	UserSettingsDAO userSettingsDAO;

	private boolean production = true;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (production) {
			chain.doFilter(request, response);
			return;
		}

		final ServletOutputStream wrapper = new ServletOutputStreamWrapper();
		ServletResponse interceptor = new HttpServletResponseWrapper(
				(HttpServletResponse) response) {

			@Override
			public ServletOutputStream getOutputStream() throws IOException {
				return wrapper;
			}
		};
		chain.doFilter(request, interceptor);

		UserSettings settings = userSettingsDAO.findByUser(CommaFeedSession
				.get().getUser());
		String lang = settings.getLanguage() == null ? "en" : settings
				.getLanguage();

		byte[] bytes = translate(wrapper.toString(), lang).getBytes();
		response.getOutputStream().write(bytes);
		response.setContentLength(bytes.length);
		response.getOutputStream().close();

	}

	private String translate(String content, String lang) {
		Properties props = new Properties();
		InputStream is = null;
		try {
			is = getClass()
					.getResourceAsStream("/i18n/" + lang + ".properties");
			props.load(is);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(is);
		}

		return replace(content, props);
	}

	private static final Pattern PATTERN = Pattern.compile("\\$\\{(.+?)\\}");

	public String replace(String content, Properties props) {
		Matcher m = PATTERN.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String var = m.group(1);
			Object replacement = props.get(var);
			m.appendReplacement(sb, replacement.toString());
		}
		m.appendTail(sb);
		return sb.toString();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String prod = ResourceBundle.getBundle("application").getString(
				"production");
		production = Boolean.valueOf(prod);
	}

	@Override
	public void destroy() {
		// do nothing
	}

	private static class ServletOutputStreamWrapper extends ServletOutputStream {

		private ByteArrayOutputStream baos = new ByteArrayOutputStream();

		@Override
		public void write(int b) throws IOException {
			baos.write(b);
		}

		@Override
		public String toString() {
			return baos.toString();
		}
	}

}
