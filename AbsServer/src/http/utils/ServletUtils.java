package http.utils;

import engine.EngineManager;
import users.UserManager;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static http.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object AbsManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static EngineManager getEngineManager(ServletContext servletContext) {
		synchronized (AbsManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null)
				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, new EngineManager());

		}
		return (EngineManager) servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}
