package net.runelite.rs.api;

import jdk.jfr.Unsigned;
import net.runelite.api.Animation;
import net.runelite.api.SequenceDefinition;
import net.runelite.mapping.Import;

import java.util.Map;

public interface RSSequenceDefinition extends RSNode, SequenceDefinition, Animation
{
	@Import("frameCount")
	@Override
	int getFrameCount();

	@Import("frameIds")
	@Override
	int[] getFrameIDs();

	@Import("frameLengths")
	@Override
	int[] getFrameLengths();

	@Import("chatFrameIds")
	@Override
	int[] getChatFrameIds();

	@Import("transformSpotAnimationModel")
	RSModel transformSpotAnimationModel(RSModel var1, int var2);

	@Import("isCachedModelIdSet")
	boolean isCachedModelIdSet();

	void setId(int id);

	@Import("decodeNext")
	void decodeNext(RSBuffer var1, int var2);

	@Import("field2290")
	void setMayaSounds(Map var1);

	@Import("soundEffects")
	int[] getSoundEffects();
}
