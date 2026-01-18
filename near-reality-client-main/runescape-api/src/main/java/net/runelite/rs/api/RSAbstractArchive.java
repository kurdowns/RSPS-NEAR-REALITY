package net.runelite.rs.api;

import net.runelite.api.IndexDataBase;
import net.runelite.api.mixins.Inject;
import net.runelite.mapping.Import;

import java.util.HashMap;

public interface RSAbstractArchive extends IndexDataBase
{
	@Import("takeFile")
	@Override
	byte[] getConfigData(int archiveId, int fileId);

	@Import("getGroupFileIds")
	@Override
	int[] getFileIds(int groupId);

	@Import("groupCount")
	@Override
	int getGroupCount();

	@Import("fileIds")
	@Override
	int[][] getFileIds();

	@Import("getGroupFileCount")
	@Override
	int getGroupFileCount(int groupId);

	@Import("fileCounts")
	@Override
	int[] getFileCounts();

	@Import("getFile")
	@Override
	byte[] loadData(int groupId, int fileId);

	@Inject
	HashMap<Integer, byte[][]> getOverwriteFilesByGroup();

	@Inject
    HashMap<Integer, Integer> getOverwriteFileCountsByGroup();

	@Inject
	int getOverwriteGroupCount();

	@Inject
	void setOverwriteGroupCount(int count);

	@Inject
	void setOverwriteGroupFileCount(int group, int count);
}
