package net.runelite.mixins;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.api.mixins.Shadow;
import net.runelite.api.overlay.OverlayIndex;
import net.runelite.rs.api.RSAbstractArchive;
import net.runelite.rs.api.RSArchive;
import net.runelite.rs.api.RSClient;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Mixin(RSAbstractArchive.class)
public abstract class RSAbstractArchiveMixin implements RSAbstractArchive
{


	@Inject
	private int customGroupCount = -1;

	@Inject
	private HashMap<Integer, Integer> overwriteFileCountsByGroup;

	@Inject
	private HashMap<Integer, byte[][]> overwriteFilesByGroup;

	@Shadow("client")
	private static RSClient client;

	@Shadow("customClientScripts")
	private static Map<Integer, byte[]> customClientScripts;

	@Inject
	private boolean overlayOutdated;

	@Inject
	@Override
	public boolean isOverlayOutdated()
	{
		return overlayOutdated;
	}

	@SuppressWarnings("InfiniteRecursion")
	@Copy("takeFile")
	@Replace("takeFile")
	public byte[] copy$getConfigData(int groupId, int fileId)
	{
		final byte[] rsData = copy$getConfigData(groupId, fileId);
		final int archiveId = ((RSArchive) this).getIndex();

		if (OverlayIndex.hasCacheTransformer(archiveId, groupId))
		{
			return OverlayIndex.getCacheTransformer(archiveId, groupId).apply(rsData);
		}

		if (!OverlayIndex.hasOverlay(archiveId, groupId))
		{
			return rsData;
		}

		final Logger log = client.getLogger();
		final String path = String.format("/runelite/%s/%s", archiveId, groupId);

		// rsData will be null if this script didn't exist at first
		if (rsData != null)
		{
			String overlayHash, originalHash;

			try (final InputStream hashIn = getClass().getResourceAsStream(path + ".hash"))
			{
				overlayHash = CharStreams.toString(new InputStreamReader(hashIn));
				originalHash = Hashing.sha256().hashBytes(rsData).toString();
			}
			catch (IOException e)
			{
				log.warn("Missing overlay hash for {}/{}", archiveId, groupId);
				return rsData;
			}

			// Check if hash is correct first, so we don't have to load the overlay file if it doesn't match
			if (!overlayHash.equalsIgnoreCase(originalHash))
			{
				log.error("Script " + groupId + " is invalid, and will not be overlaid. This will break plugin(s)! Original hash: " + originalHash);
				overlayOutdated = true;
				return rsData;
			}
		}

		if (customClientScripts.containsKey(archiveId << 16 | groupId))
		{
			return customClientScripts.get(archiveId << 16 | groupId);
		}

		try (final InputStream ovlIn = getClass().getResourceAsStream(path))
		{
			return ByteStreams.toByteArray(ovlIn);
		}
		catch (IOException e)
		{
			log.warn("Missing overlay data for {}/{}", archiveId, groupId);
			return rsData;
		}
	}

	@Copy("clearFilesGroup")
	@Replace("clearFilesGroup")
	public void copy$clearFilesGroup(int var1) {
		if (getOverwriteFilesByGroup().containsKey(var1))
			return;
		else
			copy$clearFilesGroup(var1);
	}

	@Copy("tryLoadGroup")
	@Replace("tryLoadGroup")
	public boolean copy$tryLoadGroup(int var1) {
		if (getOverwriteFilesByGroup().containsKey(var1))
			return true;
		else
			return copy$tryLoadGroup(var1);
	}

	@Copy("getGroupFileCount")
	@Replace("getGroupFileCount")
	public int copy$getGroupFileCount(int var1) {
		if (getOverwriteFilesByGroup().containsKey(var1))
			return getOverwriteFilesByGroup().get(var1).length;
		else if (getOverwriteFileCountsByGroup().containsKey(var1))
			return getOverwriteFileCountsByGroup().get(var1);
		else
			return copy$getGroupFileCount(var1);
	}

	@Copy("getGroupCount")
	@Replace("getGroupCount")
	public int copy$getGroupCount() {
		if (getOverwriteGroupCount() > 0)
			return getOverwriteGroupCount();
		else
			return copy$getGroupCount();
	}

	@Inject
	@Override
	public HashMap<Integer, byte[][]> getOverwriteFilesByGroup() {
		if (overwriteFilesByGroup == null)
			overwriteFilesByGroup = new HashMap<>();
		return overwriteFilesByGroup;
	}

	@Inject
	@Override
	public HashMap<Integer, Integer> getOverwriteFileCountsByGroup() {
		if (overwriteFileCountsByGroup == null)
			overwriteFileCountsByGroup = new HashMap<>();
		return overwriteFileCountsByGroup;
	}

	@Inject
	@Override
	public int getOverwriteGroupCount() {
		return customGroupCount;
	}

	@Inject
	@Override
	public void setOverwriteGroupCount(int count) {
		this.customGroupCount = count;
	}

	@Inject
	@Override
	public void setOverwriteGroupFileCount(int group, int count) {
		getOverwriteFileCountsByGroup().put(group, count);
	}
}
