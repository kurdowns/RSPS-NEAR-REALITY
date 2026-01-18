/*
 * Copyright (c) 2020, Owain van Brakel <https://github.com/Owain94>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins;

import com.openosrs.client.OpenOSRS;
import com.openosrs.client.config.OpenOSRSConfig;
import com.openosrs.client.util.Groups;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.ConfigChanged;
import org.jgroups.Message;
import org.pf4j.PluginWrapper;
import org.pf4j.update.UpdateRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
@Slf4j
@Singleton
public class OPRSExternalPluginManager
{
	public static final String DEFAULT_PLUGIN_REPOS = "";
	static final String DEVELOPMENT_MANIFEST_PATH = "build/tmp/jar/MANIFEST.MF";

	public static ArrayList<ClassLoader> pluginClassLoaders = new ArrayList<>();
	@Getter(AccessLevel.PUBLIC)
	private org.pf4j.PluginManager externalPluginManager;
	@Getter(AccessLevel.PUBLIC)
	private final List<UpdateRepository> repositories = new ArrayList<>();
	@Inject
	private OpenOSRSConfig openOSRSConfig;
	@Inject
	private ConfigManager configManager;
	private final Map<String, String> pluginsMap = new HashMap<>();
	@Getter(AccessLevel.PUBLIC)
	private static final boolean developmentMode = OpenOSRS.getPluginDevelopmentPath().length > 0;
	@Getter(AccessLevel.PUBLIC)
	private final Map<String, Map<String, String>> pluginsInfoMap = new HashMap<>();
	@Inject
	private Groups groups;
	@Inject
	@Named("safeMode")
	private boolean safeMode;
	@Setter
	boolean isOutdated;



	public static <T> Predicate<T> not(Predicate<T> t)
	{
		return t.negate();
	}



	public void setWarning(boolean val)
	{
		configManager.setConfiguration("openosrs", "warning", val);
	}

	public boolean getWarning()
	{
		return openOSRSConfig.warning();
	}






	public void update()
	{
		if (groups != null && groups.getInstanceCount() > 1)
		{
			// Do not update when there is more than one client open -> api might contain changes
			log.info("Not updating external plugins since there is more than 1 client open");
			return;
		}
		else if (developmentMode)
		{
			log.debug("Not updating because we're running in developer mode");
			return;
		}
	}

}
